package com.example.flowershop.service.impl;

import com.example.flowershop.entity.DbMaintainLog;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.service.DbManageService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据库备份与恢复服务
 * TODO 将Redis数据与MySQL同步
 */
@Log
@Transactional
@Service
public class DbManageServiceImpl implements DbManageService {
    //MySQL安装路径
    //如果系统已配置环境变量则可以不设置install-path
    @Value("${spring.datasource.install-path:}")
    String installPath;
    //数据库名称
    @Value("${spring.datasource.database}")
    String database;
    //数据库用户名
    @Value("${spring.datasource.username}")
    String username;
    //数据库密码
    @Value("${spring.datasource.password}")
    String password;
    //备份路径
    @Value("${spring.datasource.backup-path}")
    String backupPath;
    //格式化时间
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
    //用于将数据库维护记录写入redis的模板
    @Resource
    private RedisTemplate<String, DbMaintainLog> redisTemplate;


    @Override
    public void manualBackup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentTime = dateFormat.format(new Date());
        // 调用系统命令执行数据库备份
        String fileName = backupPath + "backup_" + currentTime + ".sql";
        String command = "cmd /c \"" + installPath + "mysqldump\" -u" + username + " -p" + password + " --add-drop-database --databases --hex-blob " + database + " > " + fileName;
        int exitCode = execCommand(command);

        //将操作记录保存到redis
        DbMaintainLog maintainLog = new DbMaintainLog();
        maintainLog.setMaintainer(authentication.getName());
        maintainLog.setTime(currentTime);
        maintainLog.setFileName(fileName);
        maintainLog.setType("数据库备份");
        if (exitCode == 0) {
            maintainLog.setStatus("备份成功");
            redisTemplate.opsForList().rightPush("log", maintainLog);
        } else {
            maintainLog.setStatus("备份失败");
            redisTemplate.opsForList().rightPush("log", maintainLog);
            throw new GeneralException("数据库备份失败！");
        }
    }

    @Override
    public List<DbMaintainLog> getMaintainLogs() {
        //获取第一个到最后一个键为log的元素
        return redisTemplate.opsForList().range("log", 0, -1);
    }

    @Override
    public void restoreDB(String fileName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentTime = dateFormat.format(new Date());
        // 调用系统命令执行数据库恢复
        String command = "cmd /c \"" + installPath + "mysql\" -u" + username + " -p" + password + " " + database + " < " + fileName;
        int exitCode = execCommand(command);

        //将操作记录保存到redis
        DbMaintainLog maintainLog = new DbMaintainLog();
        maintainLog.setMaintainer(authentication.getName());
        maintainLog.setTime(currentTime);
        maintainLog.setFileName(fileName);
        maintainLog.setType("数据库恢复");
        if (exitCode == 0) {
            maintainLog.setStatus("恢复成功");
            redisTemplate.opsForList().rightPush("log", maintainLog);
        } else {
            maintainLog.setStatus("恢复失败");
            redisTemplate.opsForList().rightPush("log", maintainLog);
            throw new GeneralException("数据库恢复失败！");
        }
    }

    //用于执行系统命令行
    @SneakyThrows
    private int execCommand(String command) {
        log.info(command);
        Process process = Runtime.getRuntime().exec(command); //执行命令
        //打印命令行输出到日志
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK"));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
        return process.waitFor(); //获取返回值
    }
}
