package com.example.flowershop.service;

import com.example.flowershop.dto.LoginRecordDto;
import com.example.flowershop.dto.SaleInfoDto;
import com.example.flowershop.dto.SysEnvDto;
import com.example.flowershop.dto.SysInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonitorInfoService {
    //获取最近的登录日志
    Page<LoginRecordDto> findAllLoginRecord(Pageable pageable);

    //获取销售信息
    SaleInfoDto findSaleInfo();

    //获取cpu与内存信息
    SysInfoDto getSysInfo();

    //获取系统版本信息
    SysEnvDto getSysEnv();
}
