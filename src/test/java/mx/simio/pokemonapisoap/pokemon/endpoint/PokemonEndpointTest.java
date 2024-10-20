package mx.simio.pokemonapisoap.pokemon.endpoint;

import mx.simio.pokemonapisoap.pokemon.model.GetPokemonRequest;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;
import mx.simio.pokemonapisoap.pokemon.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PokemonEndpointTest {

  @Mock
  private PokemonService pokemonService;

  @InjectMocks
  private PokemonEndpoint pokemonEndpoint;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetPokemon_Success() {
    GetPokemonRequest request = new GetPokemonRequest();
    request.setName("raichu");

    GetPokemonResponse expectedResponse = GetPokemonResponse.builder()
        .name("raichu")
        .baseExperience(243)
        .id(26)
        .build();

    when(pokemonService.getPokemonDetails(anyString())).thenReturn(expectedResponse);

    GetPokemonResponse actualResponse = pokemonEndpoint.getPokemon(request);

    assertEquals(expectedResponse, actualResponse);
    verify(pokemonService, times(1)).getPokemonDetails("raichu");
  }

  @Test
  void testGetPokemon_NotFound() {
    GetPokemonRequest request = new GetPokemonRequest();
    request.setName("voldemort");

    when(pokemonService.getPokemonDetails(anyString())).thenReturn(null);

    GetPokemonResponse actualResponse = pokemonEndpoint.getPokemon(request);

    assertNull(actualResponse);
    verify(pokemonService, times(1)).getPokemonDetails("voldemort");
  }
}
