Feature: Fetching Pokémon details using SOAP service

  Scenario: Successfully fetch details of Pokémon Pikachu
    Given the Pokemon SOAP service is available
    When I send a SOAP request to get the details of the pokemon with name "pikachu"
    Then I receive a SOAP response with the pokemon name "pikachu"
    And the abilities should include "static" and "lightning-rod"
    And the base experience should be 112
    And the held items should include "oran-berry" and "light-ball"
    And the ID should be 25
    And the locations should include "trophy-garden-area" and "pallet-town-area"

  Scenario: Request for a non-existent Pokémon returns a SOAP fault
    Given the Pokemon SOAP service is available
    When I send a SOAP request to get the details of the pokemon with name "voldemort"
    Then I should receive a SOAP fault with status code 404 and message "Failed request to https://pokeapi.co/api/v2/pokemon/voldemort with status code: 404"
