package com.example.flowershop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FlowerAndCategoryDto {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private byte[] picture;
    private Integer inventory;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;
    private String categoryName;
}
