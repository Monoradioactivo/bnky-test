package mx.simio.pokemonapisoap.pokemon.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static mx.simio.pokemonapisoap.common.Constants.POKEMON_NAMESPACE_URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "getPokemonRequest", namespace = POKEMON_NAMESPACE_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPokemonRequest {

  @XmlElement
  private String name;
}