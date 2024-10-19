package com.liro.consultations.controllers;

import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.responses.ApiResponse;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.dtos.responses.LastConsultationResponse;
import com.liro.consultations.service.ConsultationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.List;

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
                                                                @RequestHeader(name = "clinicId", required = false) Long clinicId,
                                                                @RequestHeader(name = "Authorization",  required = false) String token ) {
        return ResponseEntity.ok(consultationService.getConsultationResponse(consultationId, token, clinicId));
    }

    @ApiPageable
    @GetMapping(value = "/findAll", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultationResponse>> getAll(@RequestParam("animalId") Long animalId,
                                                             Pageable pageable,
                                                             @RequestHeader(name = "clinicId", required = false) Long clinicId,
                                                             @RequestHeader(name = "Authorization",  required = false) String token) {
        return ResponseEntity.ok(consultationService.findAllByAnimalId(animalId, pageable, token, clinicId));
    }

    @GetMapping(value = "/findLast", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LastConsultationResponse> getLast(@RequestParam("animalId") Long animalId,
                                                            @RequestHeader(name = "clinicId", required = false) Long clinicId,
                                                            @RequestHeader(name = "Authorization", required = false)String token){
        return ResponseEntity.ok(consultationService.getLastConsultationResponse(animalId, token, clinicId));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> createConsultation(@Valid @RequestBody ConsultationDTO consultationDto,
                                                          @RequestHeader(name = "clinicId", required = false) Long clinicId,
                                                          @RequestHeader(name = "Authorization",  required = false) String token)  {
        ConsultationResponse consultationResponse = consultationService.createConsultation(consultationDto, token, clinicId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/consultations/{consultationId}")
                .buildAndExpand(consultationResponse.getId()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "Consultation created successfully"));
    }

    @PostMapping(value = "/migrate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> migrateConsultations(@Valid @RequestBody List<ConsultationDTO> consultationDTOS,
                                                            @RequestParam("vetClinicId") Long vetClinicId,
                                                            @RequestParam(name = "vetUserId") Long vetUserId)  {

         consultationService.migrateConsultations(consultationDTOS, vetClinicId, vetUserId);


        return ResponseEntity.ok().build();
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateConsultation(@Valid @RequestBody ConsultationDTO consultationDto,
                                            @RequestParam("consultationId") Long consultationId,
                                                   @RequestHeader(name = "clinicId", required = false) Long clinicId,
                                                   @RequestHeader(name = "Authorization",  required = false) String token)  {
        consultationService.updateConsultation(consultationDto, consultationId, token, clinicId);

        return ResponseEntity.ok().build();
    }

    @Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. " + "Multiple sort criteria are supported.") })
    @interface ApiPageable {
    }
}