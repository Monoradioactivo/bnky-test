package mx.simio.pokemonapisoap.pokemon.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.simio.pokemonapisoap.exception.HttpClientException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PokemonApiClient {

  private final Gson gson;
  private final OkHttpClient okHttpClient;

  /**
   * Get data from the Pokémon API for a specific URL.
   *
   * @param url          The URL to get data from
   * @param responseType The class type to parse the response into
   * @param <T>          The class type to parse the response into
   * @return The response data parsed into the specified class type
   * @throws HttpClientException If an error occurs during the HTTP request
   */
  public <T> T getDataFromUrl(String url, Class<T> responseType) throws HttpClientException {
    Request request = new Request.Builder()
        .url(url)
        .build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        log.error("Request failed with status code: {}", response.code());
        throw new HttpClientException(
            "Failed request to " + url + " with status code: " + response.code(),
            response.code());
      }

      String responseBody = response.body() != null ? response.body().string() : "";
      log.debug("Response Body: {}", responseBody);

      return parseResponse(responseBody, responseType);

    } catch (IOException e) {
      log.error("IO exception occurred during request: {}", e.getMessage());
      throw new HttpClientException("Error during request to " + url, e);
    }
  }

  /**
   * Parse the response body into the specified response type.
   *
   * @param responseBody The response body in JSON format
   * @param responseType The class type to parse the response into
   * @param <T>          The class type to parse the response into
   * @return The parsed response
   * @throws IllegalArgumentException If the JSON is malformed or cannot be parsed
   */
  private <T> T parseResponse(String responseBody, Class<T> responseType) {
    try {
      return gson.fromJson(responseBody, responseType);
    } catch (JsonSyntaxException e) {
      log.error("Failed to parse response: {}", e.getMessage());
      throw new IllegalArgumentException("Failed to parse response", e);
    }
  }
}
