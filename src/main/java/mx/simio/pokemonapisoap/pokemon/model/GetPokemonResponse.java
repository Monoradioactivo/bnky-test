package mx.simio.pokemonapisoap.pokemon.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static mx.simio.pokemonapisoap.common.Constants.POKEMON_NAMESPACE_URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "getPokemonResponse", namespace = POKEMON_NAMESPACE_URI)
@XmlType(name = "getPokemonResponse", namespace = POKEMON_NAMESPACE_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPokemonResponse {

  @XmlElementWrapper(name = "abilities")
  @XmlElement(name = "ability")
  private List<Ability> abilities;

  @XmlElement
  private int baseExperience;

  @XmlElementWrapper(name = "heldItems")
  @XmlElement(name = "heldItem")
  private List<HeldItem> heldItems;

  @XmlElement
  private int id;

  @XmlElement
  private String name;

  @XmlElementWrapper(name = "locationAreas")
  @XmlElement(name = "locationArea")
  private List<LocationArea> locationAreaEncounters;
}
