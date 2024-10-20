package mx.simio.pokemonapisoap.logging;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_request_log")
public class ApiRequestLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ip_origin", nullable = false)
  private String ipOrigin;

  @Column(name = "request_date", nullable = false)
  private Instant requestDate;

  @Column(name = "endpoint_method", nullable = false)
  private String endpointMethod;

  @Column(name = "duration_ms")
  private Long durationMs;

  @Lob
  @Column(name = "request_body")
  private String requestBody;

  @Lob
  @Column(name = "response_body")
  private String responseBody;
}