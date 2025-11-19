package com.tree.plms.service;

import com.tree.plms.model.entity.FeeRule;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储计费标准 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface FeeRuleService extends IService<FeeRule> {

    /**
     * 根据ID获取计费规则信息
     * @param ruleId 规则ID
     * @return 计费规则信息
     */
    FeeRule getFeeRuleById(String ruleId);

    /**
     * 获取所有计费规则列表
     * @return 计费规则列表
     */
    List<FeeRule> getAllFeeRules();

    /**
     * 新增计费规则
     * @param feeRule 计费规则信息
     * @return 是否成功
     */
    boolean addFeeRule(FeeRule feeRule);

    /**
     * 更新计费规则信息
     * @param feeRule 计费规则信息
     * @return 是否成功
     */
    boolean updateFeeRule(FeeRule feeRule);

    /**
     * 删除计费规则
     * @param ruleId 规则ID
     * @return 是否成功
     */
    boolean deleteFeeRule(String ruleId);

    /**
     * 根据车辆类型查询计费规则列表
     * @param vehicleType 车辆类型
     * @return 计费规则列表
     */
    List<FeeRule> getFeeRulesByVehicleType(String vehicleType);

    /**
     * 根据规则状态查询计费规则列表
     * @param status 状态
     * @return 计费规则列表
     */
    List<FeeRule> getFeeRulesByStatus(String status);
}