package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.mapper.*;
import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.DailyStatsDTO;
import com.tree.plms.model.dto.response.MonthlyStatsDTO;
import com.tree.plms.model.entity.*;
import com.tree.plms.service.ReportService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 报表统计服务实现类
 */
@Service
public class ReportServiceImpl implements ReportService {
    
    @Resource
    private AccessEventMapper accessEventMapper;
    
    @Resource
    private PaymentMapper paymentMapper;
    
    @Resource
    private ParkingSessionMapper parkingSessionMapper;
    
    @Resource
    private MonthlyCardMapper monthlyCardMapper;
    
    @Resource
    private VehicleMapper vehicleMapper;
    
    @Override
    public DailyStatsDTO getDailyStats(LocalDate date) {
        DailyStatsDTO stats = new DailyStatsDTO();
        stats.setDate(date.toString());
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        // 统计入场车辆数 - 添加类型转换
        QueryWrapper<AccessEvent> entryWrapper = new QueryWrapper<>();
        entryWrapper.eq("event_type", "01")
                   .between("event_time", startOfDay, endOfDay);
        stats.setEntryCount(accessEventMapper.selectCount(entryWrapper).intValue());
        
        // 统计出场车辆数 - 添加类型转换
        QueryWrapper<AccessEvent> exitWrapper = new QueryWrapper<>();
        exitWrapper.eq("event_type", "02")
                  .between("event_time", startOfDay, endOfDay);
        stats.setExitCount(accessEventMapper.selectCount(exitWrapper).intValue());
        
        // 统计当日收入
        QueryWrapper<Payment> paymentWrapper = new QueryWrapper<>();
        paymentWrapper.between("pay_time", startOfDay, endOfDay);
        List<Payment> payments = paymentMapper.selectList(paymentWrapper);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Payment payment : payments) {
            totalRevenue = totalRevenue.add(payment.getAmount());
        }
        stats.setTotalRevenue(totalRevenue);
        
        // 计算平均停车时长
        QueryWrapper<ParkingSession> sessionWrapper = new QueryWrapper<>();
        sessionWrapper.ge("entry_time", startOfDay)
                     .le("entry_time", endOfDay)
                     .isNotNull("exit_time");
        List<ParkingSession> sessions = parkingSessionMapper.selectList(sessionWrapper);
        
        if (!sessions.isEmpty()) {
            long totalMinutes = 0;
            for (ParkingSession session : sessions) {
                if (session.getExitTime() != null) {
                    totalMinutes += session.getEntryTime().until(session.getExitTime(), java.time.temporal.ChronoUnit.MINUTES);
                }
            }
            stats.setAvgParkingMinutes((int) (totalMinutes / sessions.size()));
        } else {
            stats.setAvgParkingMinutes(0);
        }
        
        return stats;
    }
    
    @Override
    public MonthlyStatsDTO getMonthlyStats(int year, int month) {
        MonthlyStatsDTO stats = new MonthlyStatsDTO();
        stats.setMonth(year + "-" + String.format("%02d", month));
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        
        int dayCount = endDate.getDayOfMonth();
        List<DailyStatsDTO> dailyDetails = new ArrayList<>();
        
        int totalEntry = 0;
        int totalExit = 0;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        
        for (int i = 1; i <= dayCount; i++) {
            LocalDate currentDate = LocalDate.of(year, month, i);
            // 跳过非工作日（如果需要）
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }
            
            DailyStatsDTO dailyStats = getDailyStats(currentDate);
            dailyDetails.add(dailyStats);
            totalEntry += dailyStats.getEntryCount();
            totalExit += dailyStats.getExitCount();
            totalRevenue = totalRevenue.add(dailyStats.getTotalRevenue());
        }
        
        int workDayCount = dailyDetails.size();
        if (workDayCount > 0) {
            stats.setAvgDailyEntry(totalEntry / workDayCount);
            stats.setAvgDailyExit(totalExit / workDayCount);
            stats.setMonthlyRevenue(totalRevenue);
        } else {
            stats.setAvgDailyEntry(0);
            stats.setAvgDailyExit(0);
            stats.setMonthlyRevenue(BigDecimal.ZERO);
        }
        
        stats.setDailyDetails(dailyDetails);
        
        return stats;
    }
    
    @Override
    public List<CardExpiryAlertDTO> getCardExpiryAlerts(int days) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiryTime = currentTime.plusDays(days);
        
        QueryWrapper<MonthlyCard> cardWrapper = new QueryWrapper<>();
        cardWrapper.le("end_date", expiryTime)
                   .ge("end_date", currentTime)
                   .eq("status", "01"); // 仅查询启用状态的月卡
        
        List<MonthlyCard> cards = monthlyCardMapper.selectList(cardWrapper);
        List<CardExpiryAlertDTO> alerts = new ArrayList<>();
        
        for (MonthlyCard card : cards) {
            CardExpiryAlertDTO alert = new CardExpiryAlertDTO();
            alert.setCardId(card.getCardId());
            alert.setVehicleId(card.getVehicleId());
            alert.setEndDate(card.getEndDate());
            
            // 计算剩余天数
            int remainingDays = (int) currentTime.until(card.getEndDate(), java.time.temporal.ChronoUnit.DAYS);
            alert.setRemainingDays(remainingDays + 1); // 加1天，确保当天到期的也会被提醒
            
            // 获取车牌号
            Vehicle vehicle = vehicleMapper.selectById(card.getVehicleId());
            if (vehicle != null) {
                alert.setLicensePlate(vehicle.getLicensePlate());
            }
            
            alerts.add(alert);
        }
        
        return alerts;
    }
}