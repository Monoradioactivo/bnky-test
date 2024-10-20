package mx.simio.pokemonapisoap.pokemon.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonRequest;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;
import mx.simio.pokemonapisoap.pokemon.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PokemonApiSteps {

  @Autowired
  private PokemonService pokemonService;

  private GetPokemonResponse pokemonResponse;
  private String faultString;

  @Given("the Pokemon SOAP service is available")
  public void thePokemonSoapServiceIsAvailable() {
    // Empty because the service is always available
  }

  @When("I send a SOAP request to get the details of the pokemon with name {string}")
  public void iSendASoapRequestToGetTheDetailsOfThePokemonWithName(String pokemonName) {
    try {
      GetPokemonRequest request = new GetPokemonRequest();
      request.setName(pokemonName);

      pokemonResponse = pokemonService.getPokemonDetails(request.getName());
    } catch (Exception e) {
      faultString = e.getMessage();
    }
  }

  @Then("I receive a SOAP response with the pokemon name {string}")
  public void iReceiveASoapResponseWithThePokemonName(String expectedPokemonName) {
    assertNotNull(pokemonResponse, "The response should not be null");
    assertEquals(expectedPokemonName, pokemonResponse.getName(), "The Pokémon name does not match");
  }

  @Then("the abilities should include {string} and {string}")
  public void theAbilitiesShouldInclude(String ability1, String ability2) {
    assertNotNull(pokemonResponse.getAbilities(), "Abilities should not be null");
    assertEquals(2, pokemonResponse.getAbilities().size(), "There should be 2 abilities");
    assertEquals(ability1, pokemonResponse.getAbilities().get(0).getName(),
        "The first ability does not match");
    assertEquals(ability2, pokemonResponse.getAbilities().get(1).getName(),
        "The second ability does not match");
  }

  @Then("the base experience should be {int}")
  public void theBaseExperienceShouldBe(int expectedBaseExperience) {
    assertEquals(expectedBaseExperience, pokemonResponse.getBaseExperience(),
        "The base experience does not match");
  }

  @Then("the held items should include {string} and {string}")
  public void theHeldItemsShouldInclude(String item1, String item2) {
    assertNotNull(pokemonResponse.getHeldItems(), "Held items should not be null");
    assertEquals(2, pokemonResponse.getHeldItems().size(), "There should be 2 held items");
    assertEquals(item1, pokemonResponse.getHeldItems().get(0).getName(),
        "The first held item does not match");
    assertEquals(item2, pokemonResponse.getHeldItems().get(1).getName(),
        "The second held item does not match");
  }

  @Then("the ID should be {int}")
  public void theIdShouldBe(int expectedId) {
    assertEquals(expectedId, pokemonResponse.getId(), "The Pokémon ID does not match");
  }

  @Then("the locations should include {string} and {string}")
  public void theLocationsShouldInclude(String location1, String location2) {
    assertNotNull(pokemonResponse.getLocationAreaEncounters(), "Location areas should not be null");
    assertEquals(location1, pokemonResponse.getLocationAreaEncounters().get(0).getName(),
        "The first location does not match");
    assertEquals(location2, pokemonResponse.getLocationAreaEncounters().get(1).getName(),
        "The second location does not match");
  }

  @Then("I should receive a SOAP fault with status code {int} and message {string}")
  public void iShouldReceiveASoapFaultWithStatusCodeAndMessage(int expectedStatusCode,
      String expectedMessage) {
    assertNotNull(faultString, "A SOAP fault was expected");
    assertTrue(faultString.contains("Failed request"),
        "The fault message should indicate a failed request");
    assertTrue(faultString.contains(String.valueOf(expectedStatusCode)),
        "The fault message should contain the status code");
    assertTrue(faultString.contains(expectedMessage),
        "The fault message should contain the expected message");
  }
}
