package com.example.flowershop.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库维护信息
 */
@Data
public class DbMaintainLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private String time; //维护时间
    private String maintainer; //维护者
    private String type; //维护类型
    private String fileName; //备份的文件名
    private String status; //状态（操作成功/失败）
}
