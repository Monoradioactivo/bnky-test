package mx.simio.pokemonapisoap.pokemon.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "ability")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ability {

  @XmlElement
  private String name;

  @XmlElement(name = "hidden")
  private boolean hidden;

  @XmlElement
  private int slot;
}
