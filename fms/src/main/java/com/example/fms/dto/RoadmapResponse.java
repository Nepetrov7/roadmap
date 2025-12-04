package com.example.fms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapResponse {
    private Map<String, List<RoadmapItemDto>> roadmapByChapter;
    private Boolean isProfileComplete;
    private String message;
}

