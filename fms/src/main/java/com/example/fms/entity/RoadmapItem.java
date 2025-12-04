package com.example.fms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roadmap_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String chapter;

  @Column(name = "order_in_chapter", nullable = false)
  private Integer orderInChapter;
}
