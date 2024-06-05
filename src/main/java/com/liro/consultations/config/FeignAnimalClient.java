package main.java.com.liro.consultations.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "animals-service")
public interface FeignAnimalClient {
    @RequestMapping(method = RequestMethod.GET, value = "/animals/hasPermissions")
    ResponseEntity<Void> hasPermissions( @RequestParam("animalId") Long animalId,
                                        @RequestParam("needWritePermissions") Boolean needWritePermissions,
                                        @RequestParam("onlyOwner") Boolean onlyOwner,
                                        @RequestParam("vetEnabled") Boolean vetEnabled,
                                        @RequestParam("onlyVet") Boolean onlyVet,
                                        @RequestHeader(name = "Authorization") String token);
}