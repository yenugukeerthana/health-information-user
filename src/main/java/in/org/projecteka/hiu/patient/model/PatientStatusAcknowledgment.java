package in.org.projecteka.hiu.patient.model;

import in.org.projecteka.hiu.consent.model.consentmanager.ConsentAcknowledgementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PatientStatusAcknowledgment {
    private ConsentAcknowledgementStatus status;
}
