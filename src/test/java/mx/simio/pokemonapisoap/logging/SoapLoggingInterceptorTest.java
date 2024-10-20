package mx.simio.pokemonapisoap.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.lang.reflect.Method;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import javax.xml.transform.Source;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SoapLoggingInterceptorTest {

  @Mock
  private ApiRequestLogService apiRequestLogService;

  @Mock
  private MessageContext messageContext;

  @Mock
  private MethodEndpoint methodEndpoint;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletConnection httpServletConnection;

  @Mock
  private TransportContext transportContext;

  @Mock
  private WebServiceMessage webServiceMessage;

  @Mock
  private Source source;

  @InjectMocks
  private SoapLoggingInterceptor soapLoggingInterceptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void afterCompletion_ShouldLogRequestAndResponse() {
    String xmlPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>test</xml>";
    Source requestSource = new StreamSource(new StringReader("<xml>test</xml>"));
    Source responseSource = new StreamSource(new StringReader("<xml>test</xml>"));

    when(messageContext.getProperty("startTime")).thenReturn(Instant.now().toEpochMilli() - 100);
    when(messageContext.getProperty("ipOrigin")).thenReturn("127.0.0.1");
    when(messageContext.getProperty("method")).thenReturn("Object.toString");
    when(messageContext.getRequest()).thenReturn(mock(WebServiceMessage.class));
    when(messageContext.getResponse()).thenReturn(mock(WebServiceMessage.class));
    when(messageContext.getRequest().getPayloadSource()).thenReturn(requestSource);
    when(messageContext.getResponse().getPayloadSource()).thenReturn(responseSource);

    soapLoggingInterceptor.afterCompletion(messageContext, methodEndpoint, null);

    verify(apiRequestLogService, times(1))
        .logRequest(eq("127.0.0.1"), eq("Object.toString"), eq(xmlPayload), eq(xmlPayload),
            anyLong());
  }

  @Test
  void handleRequest_ShouldLogRequestDetails() throws Exception {
    Method mockMethod = Object.class.getMethod("toString");
    when(methodEndpoint.getMethod()).thenReturn(mockMethod);
    when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

    WebServiceMessage mockWebServiceMessage = mock(WebServiceMessage.class);
    when(messageContext.getRequest()).thenReturn(mockWebServiceMessage);

    TransportContextHolder.setTransportContext(transportContext);
    when(transportContext.getConnection()).thenReturn(httpServletConnection);
    when(httpServletConnection.getHttpServletRequest()).thenReturn(httpServletRequest);

    boolean result = soapLoggingInterceptor.handleRequest(messageContext, methodEndpoint);

    assertTrue(result);
    verify(messageContext).setProperty(eq("startTime"), anyLong());
    verify(messageContext).setProperty(eq("ipOrigin"), eq("127.0.0.1"));
    verify(messageContext).setProperty(eq("method"), eq("Object.toString"));
  }

  @Test
  void handleResponse_ShouldReturnTrue() {
    boolean result = soapLoggingInterceptor.handleResponse(messageContext, methodEndpoint);

    assertTrue(result);
    verify(messageContext, never()).setProperty(anyString(), any());
  }

  @Test
  void handleFault_ShouldReturnTrue() {
    boolean result = soapLoggingInterceptor.handleFault(messageContext, methodEndpoint);

    assertTrue(result);
    verify(messageContext, never()).setProperty(anyString(), any());
  }

  @Test
  void handleRequest_ShouldHandleNonMethodEndpoint() {
    Object nonMethodEndpoint = new Object();
    when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

    TransportContextHolder.setTransportContext(transportContext);
    when(transportContext.getConnection()).thenReturn(httpServletConnection);
    when(httpServletConnection.getHttpServletRequest()).thenReturn(httpServletRequest);

    boolean result = soapLoggingInterceptor.handleRequest(messageContext, nonMethodEndpoint);

    assertTrue(result);

    verify(messageContext).setProperty(eq("startTime"), anyLong());
    verify(messageContext).setProperty(eq("ipOrigin"), eq("127.0.0.1"));
    verify(messageContext).setProperty(eq("method"), eq("Object"));
  }
}
