package mx.simio.pokemonapisoap.pokemon.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mx.simio.pokemonapisoap.exception.HttpClientException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PokemonApiClientTest {

  @Mock
  private OkHttpClient okHttpClient;

  @Mock
  private Gson gson;

  @Mock
  private Call call;

  @Mock
  private Response response;

  @Mock
  private ResponseBody responseBody;

  @InjectMocks
  private PokemonApiClient pokemonApiClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetDataFromUrl_Success() throws Exception {
    String url = "https://pokeapi.co/api/v2/pokemon/ditto";
    String responseBodyString = "{\"name\":\"ditto\"}";
    Class<PokemonResponse> responseType = PokemonResponse.class;
    PokemonResponse expectedResponse = new PokemonResponse("ditto");

    when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenReturn(response);
    when(response.isSuccessful()).thenReturn(true);
    when(response.body()).thenReturn(responseBody);
    when(responseBody.string()).thenReturn(responseBodyString);
    when(gson.fromJson(responseBodyString, responseType)).thenReturn(expectedResponse);

    PokemonResponse actualResponse = pokemonApiClient.getDataFromUrl(url, responseType);

    assertEquals(expectedResponse, actualResponse);
    verify(okHttpClient).newCall(any(Request.class));
    verify(call).execute();
    verify(responseBody).string();
    verify(gson).fromJson(responseBodyString, responseType);
  }

  @Test
  void testGetDataFromUrl_HttpError() throws Exception {
    String url = "https://pokeapi.co/api/v2/pokemon/ditto";
    when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenReturn(response);
    when(response.isSuccessful()).thenReturn(false);
    when(response.code()).thenReturn(404);

    HttpClientException exception = assertThrows(HttpClientException.class, () -> {
      pokemonApiClient.getDataFromUrl(url, PokemonResponse.class);
    });

    assertEquals("Failed request to " + url + " with status code: 404", exception.getMessage());
    assertEquals(404, exception.getStatusCode());
    verify(okHttpClient).newCall(any(Request.class));
    verify(call).execute();
  }

  @Test
  void testGetDataFromUrl_IOException() throws Exception {
    String url = "https://pokeapi.co/api/v2/pokemon/ditto";
    when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenThrow(new IOException("Network error"));

    HttpClientException exception = assertThrows(HttpClientException.class, () -> {
      pokemonApiClient.getDataFromUrl(url, PokemonResponse.class);
    });

    assertEquals("Error during request to " + url, exception.getMessage());
    verify(okHttpClient).newCall(any(Request.class));
    verify(call).execute();
  }

  @Test
  void testGetDataFromUrl_JsonSyntaxException() throws Exception {
    String url = "https://pokeapi.co/api/v2/pokemon/ditto";
    String responseBodyString = "{\"invalid_json\":}";
    Class<PokemonResponse> responseType = PokemonResponse.class;

    when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenReturn(response);
    when(response.isSuccessful()).thenReturn(true);

    ResponseBody localResponseBody = mock(ResponseBody.class);
    when(response.body()).thenReturn(localResponseBody);
    when(localResponseBody.string()).thenReturn(responseBodyString);

    when(gson.fromJson(responseBodyString, responseType)).thenThrow(
        new JsonSyntaxException("Malformed JSON"));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      pokemonApiClient.getDataFromUrl(url, responseType);
    });

    assertEquals("Failed to parse response", exception.getMessage());

    verify(gson).fromJson(responseBodyString, responseType);
  }

  @Setter
  @Getter
  @AllArgsConstructor
  static class PokemonResponse {

    private String name;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PokemonResponse that = (PokemonResponse) o;
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
}
