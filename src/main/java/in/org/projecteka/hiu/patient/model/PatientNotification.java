package in.org.projecteka.hiu.patient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PatientNotification {
    public Status status;
    public NotifyPatient patient;
}
