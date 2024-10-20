package mx.simio.pokemonapisoap.logging;

public interface ApiRequestLogService {

  void logRequest(String ipOrigin, String method, String requestData, String responseData,
      long duration);
}
