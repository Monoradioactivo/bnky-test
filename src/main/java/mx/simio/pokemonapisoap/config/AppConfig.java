package mx.simio.pokemonapisoap.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  /**
   * Bean for Gson.
   *
   * @return Gson instance
   */
  @Bean
  public Gson gson() {
    return new Gson();
  }
}
