package mx.simio.pokemonapisoap.logging;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApiRequestLogServiceImplTest {

  @Mock
  private ApiRequestLogRepository apiRequestLogRepository;

  @InjectMocks
  private ApiRequestLogServiceImpl apiRequestLogService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void logRequest_ShouldLogCorrectly() {
    String ipOrigin = "192.168.0.1";
    String method = "getPokemon";
    String requestData = "<request>data</request>";
    String responseData = "<response>data</response>";
    long duration = 200;

    apiRequestLogService.logRequest(ipOrigin, method, requestData, responseData, duration);

    ArgumentCaptor<ApiRequestLog> logCaptor = ArgumentCaptor.forClass(ApiRequestLog.class);
    verify(apiRequestLogRepository, times(1)).save(logCaptor.capture());

    ApiRequestLog savedLog = logCaptor.getValue();
    assertEquals(ipOrigin, savedLog.getIpOrigin());
    assertEquals(method, savedLog.getEndpointMethod());
    assertEquals(requestData, savedLog.getRequestBody());
    assertEquals(responseData, savedLog.getResponseBody());
    assertEquals(duration, savedLog.getDurationMs());
    assertEquals(Instant.now().getEpochSecond(), savedLog.getRequestDate().getEpochSecond(), 1);
  }
}
