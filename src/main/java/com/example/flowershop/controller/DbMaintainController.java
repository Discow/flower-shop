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
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')") //只有系统管理员可以维护数据库
public class DbMaintainController {
    @Resource
    DbManageService dbManageService;

    @GetMapping("backup-database")
    public RestBean<?> backupDatabase() {
        if (dbManageService.manualBackup()) {
            return RestBean.success("数据库备份成功");
        } else {
            return RestBean.failure("数据库备份失败");
        }
    }

    @GetMapping("get-maintain-logs")
    public RestBean<?> getMaintainLogs() {
        List<DbMaintainLog> maintainLogs = dbManageService.getMaintainLogs();
        if (maintainLogs != null) {
            return RestBean.success(maintainLogs, maintainLogs.size());
        } else {
            return RestBean.failure("数据库维护日志查询失败");
        }
    }

    @GetMapping("restore-database")
    public RestBean<?> restoreDatabase(@RequestParam String fileName) {
        if (dbManageService.restore(fileName)) {
            return RestBean.success("数据库恢复成功，已恢复到：" + fileName);
        } else {
            return RestBean.failure("数据库恢复失败");
        }
    }
}
