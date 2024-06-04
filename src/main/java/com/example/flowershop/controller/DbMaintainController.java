package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.DbMaintainLog;
import com.example.flowershop.service.DbManageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/admin/")
@PreAuthorize("hasAuthority('ADMIN')") //只有系统管理员可以维护数据库
public class DbMaintainController {
    @Resource
    DbManageService dbManageService;

    @GetMapping("backup-database")
    public RestBean<?> backupDatabase() {
        dbManageService.manualBackup();
        return RestBean.success("数据库备份成功");
    }

    @GetMapping("get-maintain-logs")
    public RestBean<?> getMaintainLogs() {
        List<DbMaintainLog> maintainLogs = dbManageService.getMaintainLogs();
        return RestBean.success(maintainLogs, maintainLogs.size());
    }

    @GetMapping("restore-database")
    public RestBean<?> restoreDatabase(@RequestParam String fileName) {
        dbManageService.restoreDB(fileName);
        return RestBean.success("数据库恢复成功，已恢复到：" + fileName);
    }
}
