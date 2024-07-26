package pl.selfcloud.announcement.api.detail;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class CustomerDetails<Long, String>{

  private Long customerId;
  private String email;

}
