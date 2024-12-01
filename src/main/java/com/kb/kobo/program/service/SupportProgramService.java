package com.kb.kobo.program.service;

import com.kb.kobo.program.domain.SupportProgram;
import com.kb.kobo.program.dto.ProgramResDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportProgramService {

    private final String API_URL = "https://api.odcloud.kr/api/3034791/v1/uddi:80a74cfd-55d2-4dd3-81c7-d01567d0b3c4?serviceKey=dq0FiphIXegKyP%2F5zIDul95IvtalzdhixfdY7Hp9g4Onm%2FX9aCt378S5nVejoKY%2BGFEL5uvq75P1%2FlYuu%2Bf%2BLQ%3D%3D";

    public List<SupportProgram> getFilteredPrograms(String field, String supervisingAgency, String applicationStartDate, String applicationEndDate){
        RestTemplate restTemplate = new RestTemplate();
        ProgramResDto response = restTemplate.getForObject(API_URL, ProgramResDto.class);

        List<SupportProgram> supportPrograms = response.getData();

        if (field != null && !field.isEmpty()) {
            supportPrograms = supportPrograms.stream()
                    .filter(program -> program.getField().equals(field))
                    .collect(Collectors.toList());
        }

        if (supervisingAgency != null && !supervisingAgency.isEmpty()) {
            supportPrograms = supportPrograms.stream()
                    .filter(program -> program.getSupervisingAgency().equals(supervisingAgency))
                    .collect(Collectors.toList());
        }

        LocalDate now = LocalDate.now();
        if (applicationStartDate != null && applicationEndDate != null) {
            supportPrograms = supportPrograms.stream()
                    .filter(program -> {
                        LocalDate startDate = LocalDate.parse(program.getApplicationStartDate());
                        LocalDate endDate = LocalDate.parse(program.getApplicationEndDate());
                        return (startDate.isBefore(now) || startDate.isEqual(now)) && (endDate.isAfter(now) || endDate.isEqual(now));
                    })
                    .collect(Collectors.toList());
        }

        return supportPrograms;
    }
}
