package com.tree.plms.controller;

import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.DailyStatsDTO;
import com.tree.plms.model.dto.response.MonthlyStatsDTO;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.service.ReportService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 报表统计控制器
 */
@RestController
@RequestMapping("/reports")
public class ReportController {
    
    @Resource
    private ReportService reportService;
    
    /**
     * 获取日常统计数据
     * @param date 统计日期，格式：yyyy-MM-dd，默认为当天
     * @return 日常统计数据
     */
    @GetMapping("/daily-stats")
    public Result<DailyStatsDTO> getDailyStats(@RequestParam(required = false) String date) {
        LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        DailyStatsDTO stats = reportService.getDailyStats(targetDate);
        return Result.success(stats);
    }
    
    /**
     * 获取月度统计数据
     * @param year 年份
     * @param month 月份
     * @return 月度统计数据
     */
    @GetMapping("/monthly-stats")
    public Result<MonthlyStatsDTO> getMonthlyStats(
            @RequestParam int year,
            @RequestParam int month) {
        MonthlyStatsDTO stats = reportService.getMonthlyStats(year, month);
        return Result.success(stats);
    }
    
    /**
     * 获取月卡到期提醒
     * @param days 提前提醒天数，默认为7天
     * @return 到期提醒列表
     */
    @GetMapping("/card-expiry-alerts")
    public Result<List<CardExpiryAlertDTO>> getCardExpiryAlerts(
            @RequestParam(defaultValue = "7") int days) {
        List<CardExpiryAlertDTO> alerts = reportService.getCardExpiryAlerts(days);
        return Result.success(alerts);
    }
}