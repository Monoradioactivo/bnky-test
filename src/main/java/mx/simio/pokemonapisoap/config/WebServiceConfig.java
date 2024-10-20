package mx.simio.pokemonapisoap.config;

import java.util.List;
import mx.simio.pokemonapisoap.logging.SoapLoggingInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import static mx.simio.pokemonapisoap.common.Constants.POKEMON_NAMESPACE_URI;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  private final SoapLoggingInterceptor soapLoggingInterceptor;

  public WebServiceConfig(SoapLoggingInterceptor soapLoggingInterceptor) {
    this.soapLoggingInterceptor = soapLoggingInterceptor;
  }

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      ApplicationContext context) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(context);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "pokemon")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema pokemonSchema) {
    String locationUri = "/ws";
    String portTypeName = "PokemonPort";

    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName(portTypeName);
    wsdl11Definition.setLocationUri(locationUri);
    wsdl11Definition.setTargetNamespace(POKEMON_NAMESPACE_URI);
    wsdl11Definition.setSchema(pokemonSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema pokemonSchema() {
    return new SimpleXsdSchema(new ClassPathResource("wsdl/pokemon.xsd"));
  }

  @Override
  public void addInterceptors(List<EndpointInterceptor> interceptors) {
    interceptors.add(soapLoggingInterceptor);
  }
}
