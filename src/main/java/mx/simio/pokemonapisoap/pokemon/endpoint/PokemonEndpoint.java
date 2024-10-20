package mx.simio.pokemonapisoap.pokemon.endpoint;

import lombok.RequiredArgsConstructor;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonRequest;
import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;
import mx.simio.pokemonapisoap.pokemon.service.PokemonService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static mx.simio.pokemonapisoap.common.Constants.POKEMON_NAMESPACE_URI;

@Endpoint
@RequiredArgsConstructor
public class PokemonEndpoint {

  private final PokemonService pokemonService;

  @PayloadRoot(namespace = POKEMON_NAMESPACE_URI, localPart = "getPokemonRequest")
  @ResponsePayload
  public GetPokemonResponse getPokemon(@RequestPayload GetPokemonRequest request) {
    return pokemonService.getPokemonDetails(request.getName());
  }
}
