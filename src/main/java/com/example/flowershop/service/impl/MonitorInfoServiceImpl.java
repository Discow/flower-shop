package com.example.flowershop.service.impl;

import com.example.flowershop.dto.LoginRecordDto;
import com.example.flowershop.dto.SaleInfoDto;
import com.example.flowershop.dto.SysEnvDto;
import com.example.flowershop.dto.SysInfoDto;
import com.example.flowershop.entity.QFlower;
import com.example.flowershop.entity.QLoginRecord;
import com.example.flowershop.entity.QOrder;
import com.example.flowershop.entity.QUser;
import com.example.flowershop.service.MonitorInfoService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Transactional
@Service
@Log
public class MonitorInfoServiceImpl implements MonitorInfoService {
    @Resource
    JPAQueryFactory jpaQueryFactory;
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${spring.application.version}")
    private String projectVersion;


    @Override
    public Page<LoginRecordDto> findAllLoginRecord(Pageable pageable) {
        //使用QueryDSL查询
        QLoginRecord qLoginRecord = QLoginRecord.loginRecord;
        QUser qUser = QUser.user;
        //构建查询语句
        List<LoginRecordDto> results = jpaQueryFactory.select(Projections.constructor(LoginRecordDto.class,
                        qUser.username, qLoginRecord.ip, qLoginRecord.status, qLoginRecord.time))
                .from(qLoginRecord, qUser)
                .where(qLoginRecord.user.id.eq(qUser.id))
                .orderBy(qLoginRecord.time.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //查询总行数
        Long queryCount = jpaQueryFactory.select(qLoginRecord.count())
                .from(qLoginRecord, qUser)
                .where(qLoginRecord.user.id.eq(qUser.id))
                .fetchOne();
        return PageableExecutionUtils.getPage(results, pageable, () -> queryCount != null ? queryCount : 0L);
    }

    @Override
    public SaleInfoDto findSaleInfo() {
        //总订单数、营业额、总商品数量、待处理订单
        //使用QueryDSL查询
        QOrder qOrder = QOrder.order;
        QFlower qFlower = QFlower.flower;
        Long totalOrders = jpaQueryFactory.select(qOrder.count())
                .from(qOrder)
                .where(qOrder.status.notIn("已取消","unpaid_deleted")) //排除已取消和取消之后已删除的订单
                .fetchOne();

        BigDecimal turnover = jpaQueryFactory.select(qOrder.totalAmount.sum())
                .from(qOrder)
                .where(qOrder.status.notIn("已取消", "unpaid_deleted"))
                .fetchOne();

        Long totalFlower = jpaQueryFactory.select(qFlower.count())
                .from(qFlower)
                .fetchOne();

        Long pendingOrders = jpaQueryFactory.select(qOrder.count())
                .from(qOrder)
                .where(qOrder.status.eq("已支付"))
                .fetchOne();
        return new SaleInfoDto(totalOrders, turnover, totalFlower, pendingOrders);
    }

    @Override
    public SysInfoDto getSysInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        // 获取CPU信息
        CentralProcessor processor = hal.getProcessor();
        double cpuLoad = processor.getSystemCpuLoad(1000) * 100;
//        log.info("CPU负载: " + cpuLoad + "%");

        // 获取内存信息
        GlobalMemory memory = hal.getMemory();
//        log.info("内存总量: " + memory.getTotal() / 1024 / 1024 + "MB");
//        log.info("内存使用量: " + (memory.getTotal() - memory.getAvailable()) / 1024 / 1024 + "MB");
//        log.info("内存剩余量: " + memory.getAvailable() / 1024 / 1024 + "MB");

        //内存使用百分比=已使用容量/内存总容量
        double memPercent = (memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100;
        return new SysInfoDto((int) cpuLoad, (int) memPercent);
    }

    @Override
    public SysEnvDto getSysEnv() {
        //获取spring boot版本
        String springBootVersion = SpringBootVersion.getVersion();
        //MySQL版本
        String dbVersion = (String) entityManager.createNativeQuery("SELECT VERSION()").getSingleResult();
        //操作系统
        String os = System.getProperty("os.name");
        return new SysEnvDto(os, springBootVersion, dbVersion, this.projectVersion);
    }
}
