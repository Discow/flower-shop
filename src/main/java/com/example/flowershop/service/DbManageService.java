package com.example.flowershop.service;

import com.example.flowershop.entity.DbMaintainLog;

import java.util.List;

public interface DbManageService {
    //手动备份
    boolean manualBackup();

    //查询备份记录
    List<DbMaintainLog> getMaintainLogs();

    //恢复数据库
    boolean restore(String fileName);
}
