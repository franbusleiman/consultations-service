package com.liro.consultations.controllers;

import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.responses.ApiResponse;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.liro.consultations.util.Util.getUser;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @Autowired
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping(value = "/{consultationId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConsultationResponse> getConsultation(@PathVariable("consultationId") Long consultationId,
                                                                @RequestHeader(name = "Authorization",  required = false) String token ) {
        return ResponseEntity.ok(consultationService.getConsultationResponse(consultationId, token));
    }

    @GetMapping(value = "/findAll", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultationResponse>> getAll(@RequestParam("animalId") Long animalId,
                                                             Pageable pageable,
                                                             @RequestHeader(name = "Authorization",  required = false) String token) {
        return ResponseEntity.ok(consultationService.findAllByAnimalId(animalId, pageable, token));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> createConsultation(@Valid @RequestBody ConsultationDTO consultationDto,
                                                          @RequestHeader(name = "Authorization",  required = false) String token)  {
        ConsultationResponse consultationResponse = consultationService.createConsultation(consultationDto, token);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/consultations/{consultationId}")
                .buildAndExpand(consultationResponse.getId()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "Consultation created successfully"));
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateConsultation(@Valid @RequestBody ConsultationDTO consultationDto,
                                            @RequestParam("consultationId") Long consultationId,
                                                   @RequestHeader(name = "Authorization",  required = false) String token)  {
        consultationService.updateConsultation(consultationDto, consultationId, token);

        return ResponseEntity.ok().build();
    }
}