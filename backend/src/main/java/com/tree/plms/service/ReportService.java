package com.tree.plms.service;

import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.DailyStatsDTO;
import com.tree.plms.model.dto.response.MonthlyStatsDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 报表统计服务接口
 */
public interface ReportService {
    
    /**
     * 获取日常统计数据
     * @param date 统计日期
     * @return 日常统计数据
     */
    DailyStatsDTO getDailyStats(LocalDate date);
    
    /**
     * 获取月度统计数据
     * @param year 年份
     * @param month 月份
     * @return 月度统计数据
     */
    MonthlyStatsDTO getMonthlyStats(int year, int month);
    
    /**
     * 获取月卡到期提醒
     * @param days 提前提醒天数
     * @return 到期提醒列表
     */
    List<CardExpiryAlertDTO> getCardExpiryAlerts(int days);
}
