package mx.simio.pokemonapisoap.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OkHttpConfig {

  /**
   * Configures and provides an {@link OkHttpClient} bean with custom interceptors for logging
   * request and response details.
   *
   * @return an instance of {@link OkHttpClient} configured with request/response logging, timeouts,
   * and retry policy.
   */
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .addInterceptor(logRequestInterceptor())
        .addInterceptor(logResponseInterceptor())
        .connectTimeout(Duration.ofSeconds(10))
        .readTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(15))
        .retryOnConnectionFailure(true)
        .build();
  }

  /**
   * Interceptor that logs HTTP request details such as method, URL, and headers. It logs each
   * request before proceeding with the chain.
   *
   * @return an {@link Interceptor} that logs request details.
   */
  private Interceptor logRequestInterceptor() {
    return chain -> {
      try {
        Request request = chain.request();
        log.debug("Request: {} {}", request.method(), request.url());
        request.headers()
            .forEach(header -> log.debug("{}: {}", header.getFirst(), header.getSecond()));
        return chain.proceed(request);
      } catch (Exception e) {
        log.error("Request failed: ", e);
        throw e;
      }
    };
  }

  /**
   * Interceptor that logs HTTP response details such as status code, headers, and body. If the
   * response has a body, it reads the body, logs it, and then re-wraps it to avoid consumption
   * issues.
   *
   * @return an {@link Interceptor} that logs response details.
   */
  private Interceptor logResponseInterceptor() {
    return chain -> {
      Response response = chain.proceed(chain.request());
      log.debug("Response Status: {}", response.code());
      response.headers()
          .forEach(header -> log.debug("{}: {}", header.getFirst(), header.getSecond()));

      if (response.body() != null) {
        String responseBodyString = response.body().string();
        log.debug("Response Body: {}", responseBodyString);

        response = response.newBuilder()
            .body(ResponseBody.create(responseBodyString, response.body().contentType()))
            .build();
      }

      return response;
    };
  }
}
