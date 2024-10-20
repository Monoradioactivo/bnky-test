package mx.simio.pokemonapisoap.logging;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequestLogServiceImpl implements ApiRequestLogService {

  private final ApiRequestLogRepository apiRequestLogRepository;

  /**
   * Logs the details of an API request, including the client IP, method, request and response
   * payloads, and the duration of the request. This data is persisted into the database through
   * {@link ApiRequestLogRepository}.
   *
   * @param ipOrigin     the IP address of the client making the request.
   * @param method       the name of the endpoint method that was executed.
   * @param requestData  the payload of the request (XML or JSON, depending on the SOAP request).
   * @param responseData the payload of the response (XML or JSON, depending on the SOAP response).
   * @param duration     the duration of the request processing in milliseconds.
   */
  @Transactional
  @Override
  public void logRequest(String ipOrigin, String method, String requestData, String responseData,
      long duration) {
    log.debug("Logging request to database: IP={}, Method={}, Duration={}ms", ipOrigin, method,
        duration);
    ApiRequestLog logToSave = ApiRequestLog.builder()
        .ipOrigin(ipOrigin)
        .requestDate(Instant.now())
        .endpointMethod(method)
        .requestBody(requestData)
        .responseBody(responseData)
        .durationMs(duration)
        .build();

    apiRequestLogRepository.save(logToSave);
    log.debug("Request logged successfully");
  }
}