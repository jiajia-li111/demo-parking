package com.tree.plms.controller;

import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.DailyStatsDTO;
import com.tree.plms.model.dto.response.MonthlyStatsDTO;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.service.MonthlyCardService;
import com.tree.plms.service.ParkingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报表管理控制器
 */
@RestController
@RequestMapping("/reports")
@Tag(name = "报表管理")
public class ReportController {

    @Autowired
    private ParkingSessionService parkingSessionService;

    @Autowired
    private MonthlyCardService monthlyCardService;

    /**
     * 获取每日统计数据
     * @param date 统计日期（格式：YYYY-MM-DD，可选）
     * @return 日统计数据
     */
    @GetMapping("/daily-stats")
    @Operation(summary = "获取每日统计数据", description = "获取指定日期的停车统计数据，包括入场数、出场数、收入等")
    public Result<DailyStatsDTO> getDailyStats(@RequestParam(required = false) String date) {
        return parkingSessionService.getDailyStats(date);
    }

    /**
     * 获取月度统计数据
     * @param year 年份
     * @param month 月份（1-12）
     * @return 月统计数据
     */
    @GetMapping("/monthly-stats")
    @Operation(summary = "获取月度统计数据", description = "获取指定月份的停车统计数据，包括月均入场数、月均出场数、月总收入等")
    public Result<MonthlyStatsDTO> getMonthlyStats(@RequestParam Integer year, @RequestParam Integer month) {
        return parkingSessionService.getMonthlyStats(year, month);
    }

    /**
     * 获取月卡到期提醒
     * @param days 提前天数（默认7天）
     * @return 到期提醒列表
     */
    @GetMapping("/card-expiry-alerts")
    @Operation(summary = "获取月卡到期提醒", description = "获取指定天数内即将到期的月卡信息")
    public Result<List<CardExpiryAlertDTO>> getCardExpiryAlerts(@RequestParam(defaultValue = "7") Integer days) {
        return monthlyCardService.getExpiringCards(days);
    }
}