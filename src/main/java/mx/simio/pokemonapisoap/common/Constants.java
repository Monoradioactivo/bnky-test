package mx.simio.pokemonapisoap.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

  // Namespace URI for the Pokémon API
  public static final String POKEMON_NAMESPACE_URI = "http://simio.mx/pokemon";

  // Base URL for the Pokémon API
  public static final String POKEMON_API_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
}
