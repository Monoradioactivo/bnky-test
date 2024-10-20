package mx.simio.pokemonapisoap.pokemon.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mx.simio.pokemonapisoap.pokemon.client.PokemonApiClient;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PokemonServiceImplTest {

  @Mock
  private PokemonApiClient pokemonApiClient;

  @InjectMocks
  private PokemonServiceImpl pokemonServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetPokemonDetails_Success() {
    JsonObject jsonObject = new JsonObject();
    JsonArray abilitiesArray = new JsonArray();
    JsonObject abilityObj1 = new JsonObject();
    abilityObj1.add("ability", createJsonObjectWithField("static"));
    abilityObj1.addProperty("is_hidden", false);
    abilityObj1.addProperty("slot", 1);
    abilitiesArray.add(abilityObj1);
    JsonObject abilityObj2 = new JsonObject();
    abilityObj2.add("ability", createJsonObjectWithField("lightning-rod"));
    abilityObj2.addProperty("is_hidden", true);
    abilityObj2.addProperty("slot", 3);
    abilitiesArray.add(abilityObj2);
    jsonObject.add("abilities", abilitiesArray);
    jsonObject.addProperty("base_experience", 243);
    JsonArray heldItemsArray = new JsonArray();
    JsonObject heldItemObj = createJsonObjectWithField("oran-berry");
    JsonObject itemObj = new JsonObject();
    itemObj.add("item", heldItemObj);
    heldItemsArray.add(itemObj);
    jsonObject.add("held_items", heldItemsArray);
    jsonObject.addProperty("id", 26);
    jsonObject.addProperty("name", "raichu");
    jsonObject.addProperty("location_area_encounters",
        "https://pokeapi.co/api/v2/pokemon/26/location-area-encounters");

    JsonArray locationArray = new JsonArray();
    JsonObject locationObj1 = createJsonObjectWithField("cerulean-cave-1f");
    JsonObject locationAreaObj1 = new JsonObject();
    locationAreaObj1.add("location_area", locationObj1);
    locationArray.add(locationAreaObj1);
    JsonObject locationObj2 = createJsonObjectWithField("power-plant-area");
    JsonObject locationAreaObj2 = new JsonObject();
    locationAreaObj2.add("location_area", locationObj2);
    locationArray.add(locationAreaObj2);

    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonObject.class))).thenReturn(jsonObject);
    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonArray.class))).thenReturn(
        locationArray);

    GetPokemonResponse response = pokemonServiceImpl.getPokemonDetails("raichu");

    assertNotNull(response);
    assertEquals("raichu", response.getName());
    assertEquals(243, response.getBaseExperience());
    assertEquals(26, response.getId());
    assertEquals(2, response.getAbilities().size());
    assertEquals(1, response.getHeldItems().size());
    assertEquals(2, response.getLocationAreaEncounters().size());

    verify(pokemonApiClient, times(2)).getDataFromUrl(anyString(), any());
  }

  @Test
  void testGetPokemonDetails_JsonSyntaxException() {
    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonObject.class)))
        .thenThrow(new JsonSyntaxException("Malformed JSON"));

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
      pokemonServiceImpl.getPokemonDetails("raichu");
    });

    assertEquals("Malformed JSON", exception.getMessage());
  }


  @Test
  void testGetPokemonDetails_LocationAreas_Success() {
    JsonObject jsonObject = new JsonObject();
    JsonArray abilitiesArray = new JsonArray();
    jsonObject.add("abilities", abilitiesArray);
    jsonObject.addProperty("base_experience", 243);
    jsonObject.addProperty("id", 26);
    jsonObject.addProperty("name", "raichu");

    JsonArray heldItemsArray = new JsonArray();
    jsonObject.add("held_items", heldItemsArray);

    jsonObject.addProperty("location_area_encounters",
        "https://pokeapi.co/api/v2/pokemon/26/location-area-encounters");

    JsonArray locationArray = new JsonArray();
    JsonObject locationObj1 = createJsonObjectWithField("cerulean-cave-1f");
    JsonObject locationAreaObj1 = new JsonObject();
    locationAreaObj1.add("location_area", locationObj1);
    locationArray.add(locationAreaObj1);
    JsonObject locationObj2 = createJsonObjectWithField("power-plant-area");
    JsonObject locationAreaObj2 = new JsonObject();
    locationAreaObj2.add("location_area", locationObj2);
    locationArray.add(locationAreaObj2);

    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonObject.class))).thenReturn(jsonObject);
    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonArray.class))).thenReturn(
        locationArray);

    GetPokemonResponse response = pokemonServiceImpl.getPokemonDetails("raichu");

    assertNotNull(response);
    assertEquals(26, response.getId());
    assertEquals("raichu", response.getName());
    assertNotNull(response.getLocationAreaEncounters());
    assertEquals(2, response.getLocationAreaEncounters().size());
    assertEquals("cerulean-cave-1f", response.getLocationAreaEncounters().get(0).getName());
    assertEquals("power-plant-area", response.getLocationAreaEncounters().get(1).getName());

    verify(pokemonApiClient, times(2)).getDataFromUrl(anyString(), any());
  }


  @Test
  void testGetPokemonDetails_LocationAreas_JsonSyntaxException() {
    JsonObject jsonObject = new JsonObject();
    JsonArray abilitiesArray = new JsonArray();
    jsonObject.add("abilities", abilitiesArray);
    jsonObject.addProperty("base_experience", 243);
    jsonObject.addProperty("id", 26);
    jsonObject.addProperty("name", "raichu");

    JsonArray heldItemsArray = new JsonArray();
    jsonObject.add("held_items", heldItemsArray);

    jsonObject.addProperty("location_area_encounters",
        "https://pokeapi.co/api/v2/pokemon/26/location-area-encounters");

    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonObject.class))).thenReturn(jsonObject);

    when(pokemonApiClient.getDataFromUrl(anyString(), eq(JsonArray.class)))
        .thenThrow(new JsonSyntaxException("Malformed JSON"));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      pokemonServiceImpl.getPokemonDetails("raichu");
    });

    assertEquals("Failed to parse response", exception.getMessage());
  }

  private JsonObject createJsonObjectWithField(String fieldValue) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", fieldValue);
    return jsonObject;
  }
}
