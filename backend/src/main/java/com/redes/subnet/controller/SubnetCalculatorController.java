package com.redes.subnet.controller;

import com.redes.subnet.model.SubnetCalculatorModel;
import com.redes.subnet.model.ErrorResponse;
import com.redes.subnet.service.SubnetCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/subnet")
@Validated
public class SubnetCalculatorController {

    @Autowired
    private SubnetCalculatorService service;

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateSubnet(
            @RequestParam @NotBlank @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}$", 
                message = "El formato de red debe ser como: 192.168.1.0/24") String baseNetwork,
            @RequestParam @Min(value = 1, message = "El número de hosts debe ser mayor a 0") int requiredHosts,
            @RequestParam @Min(value = 1, message = "El número de subred debe ser mayor a 0") int subnetNumber) {
        
        try {
            service.validateParametersWithDetail(baseNetwork, requiredHosts, subnetNumber);
            SubnetCalculatorModel result = service.calculateSubnet(baseNetwork, requiredHosts, subnetNumber);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                    "Error de validación",
                    "VALIDATION_ERROR",
                    e.getMessage()
                ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                    "Error en el cálculo",
                    "CALCULATION_ERROR",
                    e.getMessage()
                ));
        }
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(jakarta.validation.ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
            .map(violation -> violation.getMessage())
            .findFirst()
            .orElse("Error de validación");
            
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(
                "Error de validación",
                "VALIDATION_ERROR",
                message
            ));
    }
} 