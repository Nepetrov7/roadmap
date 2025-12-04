package com.example.fms.controller;

import com.example.fms.dto.RoadmapResponse;
import com.example.fms.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoadmapController {
    private final RoadmapService roadmapService;

    @GetMapping
    public ResponseEntity<RoadmapResponse> getRoadmap() {
        try {
            RoadmapResponse response = roadmapService.getRoadmap();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

