package com.example.flowershop.controller;

import com.example.flowershop.dto.*;
import com.example.flowershop.service.MonitorInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 监控面板控制器
 */
@RestController
@RequestMapping("/api/admin/")
public class MonitorInfoController {
    @Resource
    MonitorInfoService service;

    //获取最近登录记录
    @GetMapping("getAll-login-record")
    public RestBean<?> getAllLoginRecord(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<LoginRecordDto> allLoginRecord = service.findAllLoginRecord(pageable);
        return RestBean.success(allLoginRecord.getContent(), (int) allLoginRecord.getTotalElements());
    }

    //获取销售信息
    @GetMapping("get-saleInfo")
    public RestBean<?> getSaleInfo() {
        SaleInfoDto saleInfo = service.findSaleInfo();
        return RestBean.success(saleInfo, null);
    }

    //获取cpu与内存使用率
    @GetMapping("get-cpuMemInfo")
    public RestBean<?> getCpuMemInfo() {
        SysInfoDto sysInfo = service.getSysInfo();
        return RestBean.success(sysInfo, null);
    }

    //获取系统环境信息
    @GetMapping("get-sysEnv")
    public RestBean<?> getSysEnv() {
        SysEnvDto sysEnv = service.getSysEnv();
        return RestBean.success(sysEnv, null);
    }
}
