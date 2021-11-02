package in.org.projecteka.hiu.patient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.org.projecteka.hiu.common.GatewayResponse;
import in.org.projecteka.hiu.common.RespError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PatientStatusNotification {
    private UUID requestId;
    private LocalDateTime timestamp;
    private PatientStatusAcknowledgment acknowledgement;
    private RespError error;
    private GatewayResponse resp;

}
