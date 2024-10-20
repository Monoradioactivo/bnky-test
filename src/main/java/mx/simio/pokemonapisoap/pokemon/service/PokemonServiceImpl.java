package mx.simio.pokemonapisoap.pokemon.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.simio.pokemonapisoap.pokemon.client.PokemonApiClient;
import mx.simio.pokemonapisoap.pokemon.model.Ability;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;
import mx.simio.pokemonapisoap.pokemon.model.HeldItem;
import mx.simio.pokemonapisoap.pokemon.model.LocationArea;
import org.springframework.stereotype.Service;

import static mx.simio.pokemonapisoap.common.Constants.POKEMON_API_BASE_URL;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokemonServiceImpl implements PokemonService {

  private final PokemonApiClient pokemonApiClient;

  /**
   * Fetches the details of a Pokémon by its name, including abilities, base experience, held items,
   * ID, name, and location area encounters.
   *
   * @param name The name of the Pokémon to fetch.
   * @return A {@link GetPokemonResponse} containing the Pokémon's details.
   * @throws IllegalArgumentException If the JSON response from the API cannot be parsed.
   */
  @Override
  public GetPokemonResponse getPokemonDetails(String name) {
    JsonObject jsonObject = pokemonApiClient.getDataFromUrl(POKEMON_API_BASE_URL + name,
        JsonObject.class);

    GetPokemonResponse.GetPokemonResponseBuilder responseBuilder = GetPokemonResponse.builder();

    try {
      // Extract abilities
      List<Ability> abilities = new ArrayList<>();
      JsonArray abilitiesArray = jsonObject.getAsJsonArray("abilities");
      for (JsonElement abilityElement : abilitiesArray) {
        JsonObject abilityObject = abilityElement.getAsJsonObject();
        String abilityName = abilityObject.getAsJsonObject("ability").get("name").getAsString();
        boolean hidden = abilityObject.get("is_hidden").getAsBoolean();
        int slot = abilityObject.get("slot").getAsInt();

        Ability ability = Ability.builder()
            .name(abilityName)
            .hidden(hidden)
            .slot(slot)
            .build();
        abilities.add(ability);
      }

      // Extract base experience
      int baseExperience = jsonObject.get("base_experience").getAsInt();

      // Extract held items
      List<HeldItem> heldItems = new ArrayList<>();
      JsonArray heldItemsArray = jsonObject.getAsJsonArray("held_items");
      for (JsonElement heldItemElement : heldItemsArray) {
        JsonObject heldItemObject = heldItemElement.getAsJsonObject();
        String itemName = heldItemObject.getAsJsonObject("item").get("name").getAsString();
        HeldItem heldItem = new HeldItem(itemName);
        heldItems.add(heldItem);
      }

      // Extract ID and name
      int id = jsonObject.get("id").getAsInt();
      String pokemonName = jsonObject.get("name").getAsString();

      // Extract location_area_encounters (as a String)
      String locationAreaEncountersUrl = jsonObject.get("location_area_encounters").getAsString();

      // Make a second API request to fetch the location area encounter names
      List<LocationArea> locationAreas = getLocationAreas(locationAreaEncountersUrl);

      return responseBuilder
          .abilities(abilities)
          .baseExperience(baseExperience)
          .heldItems(heldItems)
          .id(id)
          .name(pokemonName)
          .locationAreaEncounters(locationAreas)
          .build();

    } catch (JsonSyntaxException e) {
      log.error("Error parsing JSON response: {}", e.getMessage());
      throw new IllegalArgumentException("Failed to parse response", e);
    }
  }

  /**
   * Fetches the location areas where a Pokémon can be encountered using a second API call.
   *
   * @param locationAreaEncountersUrl The URL to fetch the location area encounters from.
   * @return A list of {@link LocationArea} representing the locations where the Pokémon can be
   * found.
   * @throws IllegalArgumentException If the JSON response from the API cannot be parsed.
   */
  private List<LocationArea> getLocationAreas(String locationAreaEncountersUrl) {
    JsonArray locationArray = pokemonApiClient.getDataFromUrl(locationAreaEncountersUrl,
        JsonArray.class);

    List<LocationArea> locationAreas = new ArrayList<>();

    try {
      for (JsonElement locationElement : locationArray) {
        JsonObject locationObject = locationElement.getAsJsonObject();
        String locationName = locationObject.getAsJsonObject("location_area").get("name")
            .getAsString();
        locationAreas.add(new LocationArea(locationName));
      }
    } catch (JsonSyntaxException e) {
      log.error("Error parsing location area encounters JSON: {}", e.getMessage());
      throw new IllegalArgumentException("Failed to parse location area encounters response", e);
    }
    return locationAreas;
  }
}
