package mx.simio.pokemonapisoap.exception;

import lombok.Getter;

@Getter
public class HttpClientException extends RuntimeException {

  private final int statusCode;

  public HttpClientException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public HttpClientException(String message, Throwable cause) {
    super(message, cause);
    this.statusCode = 500;
  }
}
