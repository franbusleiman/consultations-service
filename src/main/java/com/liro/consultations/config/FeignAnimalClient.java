package com.liro.consultations.config;

import com.liro.consultations.dtos.RecordDTO;
import com.liro.consultations.dtos.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "animals-service")
public interface FeignAnimalClient {
    @RequestMapping(method = RequestMethod.GET, value = "/animals/hasPermissions")
    ResponseEntity<Void> hasPermissions( @RequestParam("animalId") Long animalId,
                                        @RequestParam("needWritePermissions") Boolean needWritePermissions,
                                        @RequestParam("onlyOwner") Boolean onlyOwner,
                                        @RequestParam("onlyVet") Boolean onlyVet,
                                        @RequestHeader("clinicId") Long clinicId,
                                        @RequestHeader(name = "Authorization") String token);

    @RequestMapping(method = RequestMethod.POST, value = "/records/")
    ResponseEntity<ApiResponse> createRecord(@Valid @RequestBody RecordDTO recordDto,
                                             @RequestHeader("clinicId") Long clinicId,
                                             @RequestHeader(name = "Authorization") String token);

    @RequestMapping(method = RequestMethod.POST, value = "/records/migrate")
    ResponseEntity<ApiResponse> migrateRecords(@Valid @RequestBody List<RecordDTO> recordDto,
                                               @RequestParam("vetUserId") Long vetUserId);

}