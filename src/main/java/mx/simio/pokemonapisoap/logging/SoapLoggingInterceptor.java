package mx.simio.pokemonapisoap.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class SoapLoggingInterceptor implements EndpointInterceptor {

  private final ApiRequestLogService apiRequestLogService;

  /**
   * Handles the SOAP request and logs the start time, client IP, and method name. This method is
   * triggered when a SOAP request is received, capturing relevant information to be used later in
   * the response for logging and tracking.
   *
   * @param messageContext the context of the SOAP message, which contains information about the
   *                       request and response.
   * @param endpoint       the target endpoint that handles the SOAP request. This could be an
   *                       instance of {@link MethodEndpoint} or another object.
   * @return {@code true} to continue processing the request, {@code false} to stop further handling
   * of the request.
   */
  @Override
  public boolean handleRequest(MessageContext messageContext, Object endpoint) {
    log.info("SOAP request received.");
    long startTime = Instant.now().toEpochMilli();
    messageContext.setProperty("startTime", startTime);

    String ipOrigin = getClientIp();
    String methodName;

    if (endpoint instanceof MethodEndpoint methodEndpoint) {
      Method method = methodEndpoint.getMethod();
      methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
    } else {
      methodName = endpoint.getClass().getSimpleName();
    }

    messageContext.setProperty("ipOrigin", ipOrigin);
    messageContext.setProperty("method", methodName);

    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext, Object endpoint) {
    log.info("SOAP response handled.");
    return true;
  }

  @Override
  public boolean handleFault(MessageContext messageContext, Object endpoint) {
    log.error("SOAP fault handled.");
    return true;
  }

  /**
   * Logs details after completing SOAP request processing. This method captures the time taken for
   * the request, client IP, request and response payloads, and logs these details via the
   * ApiRequestLogService.
   *
   * @param messageContext the context of the SOAP message, containing request and response
   *                       details.
   * @param endpoint       the endpoint that handled the SOAP request.
   * @param ex             the exception thrown during request processing, or {@code null} if none
   *                       occurred.
   */
  @Override
  public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) {
    log.info("Completed SOAP request processing.");

    long startTime = (long) messageContext.getProperty("startTime");
    long duration = Instant.now().toEpochMilli() - startTime;

    String ipOrigin = (String) messageContext.getProperty("ipOrigin");
    String method = (String) messageContext.getProperty("method");
    String requestData = transformSourceToString(messageContext.getRequest().getPayloadSource());
    String responseData = transformSourceToString(messageContext.getResponse().getPayloadSource());

    apiRequestLogService.logRequest(ipOrigin, method, requestData, responseData, duration);
  }

  /**
   * Transforms a {@link Source} object (typically an XML payload) into its string representation.
   * This method uses a {@link Transformer} to convert the {@code Source} to a string.
   *
   * @param source the {@code Source} object representing the XML payload.
   * @return the string representation of the XML payload, or an error message if transformation
   * fails.
   */
  private String transformSourceToString(Source source) {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      transformer.transform(source, result);
      return writer.toString();
    } catch (Exception e) {
      log.error("Error transforming XML Source to String: {}", e.getMessage());
      return "Error transforming payload";
    }
  }

  /**
   * Retrieves the client's IP address from the incoming HTTP request. If the request passed through
   * a proxy, the IP address is retrieved from the 'X-Forwarded-For' header. Otherwise, it falls
   * back to {@code request.getRemoteAddr()}.
   *
   * @return the client IP address as a string.
   */
  private String getClientIp() {
    TransportContext transportContext = TransportContextHolder.getTransportContext();
    HttpServletConnection connection = (HttpServletConnection) transportContext.getConnection();
    HttpServletRequest request = connection.getHttpServletRequest();
    String ipOrigin = request.getHeader("X-Forwarded-For");

    if (ipOrigin == null || ipOrigin.isEmpty()) {
      ipOrigin = request.getRemoteAddr();
    }

    return ipOrigin;
  }
}
