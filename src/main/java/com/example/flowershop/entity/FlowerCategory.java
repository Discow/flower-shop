package com.example.flowershop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerCategory {
    private Integer id;
    private String name;
    private String description;
    private List<Flower> flowers;
}
