package edu.ucsb.cs156.gauchoride.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "riderapplication")
public class RiderApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String status;
  private Long userId;
  private String perm_number;

  private Date created_date;
  private Date updated_date;
  private Date cancelled_date;

  private String description;
  private String notes;
}
