package com.example.flowershop.service;

import com.example.flowershop.entity.DbMaintainLog;

import java.util.List;

public interface DbManageService {
    //手动备份
    void manualBackup();

    //查询备份记录
    List<DbMaintainLog> getMaintainLogs();

    //恢复数据库
    void restoreDB(String fileName);
}
