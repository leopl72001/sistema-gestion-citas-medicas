package com.mediconnect.specialty.api;
import java.util.List;
import com.mediconnect.specialty.application.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/specialties") @Tag(name="Specialties")
public class SpecialtyController { private final SpecialtyService service; public SpecialtyController(SpecialtyService service){this.service=service;} @GetMapping @Operation(summary="List medical specialties",description="Public catalog used during patient acquisition and appointment booking.") public List<SpecialtyResponse> list(){return service.list();} }
