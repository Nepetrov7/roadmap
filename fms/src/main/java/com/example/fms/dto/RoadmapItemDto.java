package com.example.fms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapItemDto {
    private Long id;
    private String name;
    private String chapter;
    private Integer orderInChapter;
}

