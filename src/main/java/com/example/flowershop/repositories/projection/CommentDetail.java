package com.example.flowershop.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@SuppressWarnings("all")
public interface CommentDetail {
    String getContent();

    Integer getRating();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date getTime();

    @Value("#{target.username}")
    String getUsername();
}
