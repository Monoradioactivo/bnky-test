package mx.simio.pokemonapisoap.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

  private String message;
  private int statusCode;
  private LocalDateTime timestamp;

  public ErrorResponse(String message, int statusCode) {
    this.message = message;
    this.statusCode = statusCode;
    this.timestamp = LocalDateTime.now();
  }
}
