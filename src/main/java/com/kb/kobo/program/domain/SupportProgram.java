package com.kb.kobo.program.domain;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String businessName;
    private String field;
    private String applicationStartDate;
    private String applicationEndDate;
    private String supervisingAgency;
    private String implementingAgency;
    private String detailURL;
}
