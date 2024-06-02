package com.example.flowershop.dto;

import lombok.*;

/**
 * cpu与内存信息传输对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SysInfoDto {
    private Integer cpu;
    private Integer memory;
}
