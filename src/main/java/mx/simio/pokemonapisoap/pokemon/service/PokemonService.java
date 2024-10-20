package mx.simio.pokemonapisoap.pokemon.service;

import mx.simio.pokemonapisoap.pokemon.model.GetPokemonResponse;

public interface PokemonService {

  GetPokemonResponse getPokemonDetails(String name);
}
