package com.example.flowershop.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("all")
public interface FlowerAndCategory {
    Integer getId();

    String getName();

    BigDecimal getPrice();

    String getDescription();

    byte[] getPicture();

    Integer getInventory();

    String getStatus();

    @Value("#{target.add_time}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date getAddTime();

    @Value("#{target.category_name}")
    String getCategoryName();
}
