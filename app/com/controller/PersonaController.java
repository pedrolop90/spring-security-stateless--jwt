package com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pedro
 */
@RestController
@RequestMapping("personas")
@RequiredArgsConstructor
public class PersonaController {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PERSONA')")
    public ResponseEntity<String> saludoPersona() {
        return ResponseEntity.ok("persona");
    }

}
