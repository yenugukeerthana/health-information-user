package in.org.projecteka.hiu.patient.model;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@ToString
@Data
@Builder
public class HiuPatientStatusNotification {
    public UUID requestId;
    public String timestamp;
    public PatientNotification notification;
}
