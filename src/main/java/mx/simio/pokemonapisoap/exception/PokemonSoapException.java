package mx.simio.pokemonapisoap.exception;

import lombok.Getter;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@Getter
@SoapFault(faultCode = FaultCode.CLIENT)
public class PokemonSoapException extends RuntimeException {

  private final int statusCode;

  public PokemonSoapException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
