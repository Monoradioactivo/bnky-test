package mx.simio.pokemonapisoap.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpClientException.class)
  public ResponseEntity<ErrorResponse> handleHttpClientException(HttpClientException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),
        HttpStatus.BAD_GATEWAY.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(PokemonSoapException.class)
  public void handlePokemonSoapException(PokemonSoapException ex, HttpServletResponse response) {
    response.setStatus(ex.getStatusCode());

    try {
      response.getWriter().write(
          "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
              "<SOAP-ENV:Body>" +
              "<SOAP-ENV:Fault>" +
              "<faultcode>SOAP-ENV:Client</faultcode>" +
              "<faultstring>" + ex.getMessage() + "</faultstring>" +
              "</SOAP-ENV:Fault>" +
              "</SOAP-ENV:Body>" +
              "</SOAP-ENV:Envelope>"
      );
      response.getWriter().flush();
    } catch (IOException e) {
      log.error("Error writing response: {}", e.getMessage());
    }
  }
}
