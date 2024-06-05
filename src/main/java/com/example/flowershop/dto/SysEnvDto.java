package com.example.flowershop.dto;

import lombok.*;

/**
 * 系统环境版本信息传输对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SysEnvDto {
    private String os; //操作系统
    private String springVersion; //spring boot版本
    private String dbVersion; //数据库版本
    private String backendVersion; //当前项目版本
}
