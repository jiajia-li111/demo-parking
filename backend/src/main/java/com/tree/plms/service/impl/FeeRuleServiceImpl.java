package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.model.entity.FeeRule;
import com.tree.plms.mapper.FeeRuleMapper;
import com.tree.plms.service.FeeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 存储计费标准 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class FeeRuleServiceImpl extends ServiceImpl<FeeRuleMapper, FeeRule> implements FeeRuleService {

    /**
     * 根据ID查询计费规则
     * @param ruleId 计费规则ID
     * @return 计费规则对象
     */

    @Override
    public FeeRule getFeeRuleById(String ruleId) {
        return baseMapper.selectById(ruleId);
    }

    /**
     * 获取所有计费规则列表
     * @return 计费规则列表
     */
    @Override
    public List<FeeRule> getAllFeeRules() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增计费规则
     * @param feeRule 计费规则对象
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFeeRule(FeeRule feeRule) {
        // 如果未指定状态，默认设置为禁用
        if (feeRule.getStatus() == null) {
            feeRule.setStatus("02");
        }
        
        // 如果要启用新规则，先禁用同类型的其他规则
        if ("01".equals(feeRule.getStatus())) {
            disableOtherRules(feeRule.getApplyTo(), feeRule.getVehicleType(), null);
        }
        
        return this.save(feeRule);
    }

    /**
     * 更新计费规则
     * @param feeRule 计费规则对象
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFeeRule(FeeRule feeRule) {
        // 检查是否要启用当前规则
        if ("01".equals(feeRule.getStatus())) {
            // 禁用同类型的其他规则（排除当前规则）
            disableOtherRules(feeRule.getApplyTo(), feeRule.getVehicleType(), feeRule.getRuleId());
        }
        
        return this.updateById(feeRule);
    }

    /**
     * 删除计费规则
     * @param ruleId 计费规则ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFeeRule(String ruleId) {
        return this.removeById(ruleId);
    }

    /**
     * 按车辆类型查询计费规则
     * @param vehicleType 车辆类型
     * @return 符合条件的计费规则列表
     */
    @Override
    public List<FeeRule> getFeeRulesByVehicleType(String vehicleType) {
        QueryWrapper<FeeRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_type", vehicleType);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按规则状态查询计费规则
     * @param status 规则状态
     * @return 符合条件的计费规则列表
     */
    @Override
    public List<FeeRule> getFeeRulesByStatus(String status) {
        QueryWrapper<FeeRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 禁用同类型的其他规则
     * @param applyTo 适用对象
     * @param vehicleType 车辆类型
     * @param excludeRuleId 要排除的规则ID
     */
    private void disableOtherRules(String applyTo, String vehicleType, String excludeRuleId) {
        QueryWrapper<FeeRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apply_to", applyTo)
                   .eq("vehicle_type", vehicleType)
                   .eq("status", "01");
        
        // 排除当前规则（如果是更新操作）
        if (excludeRuleId != null) {
            queryWrapper.ne("rule_id", excludeRuleId);
        }
        
        List<FeeRule> existingRules = baseMapper.selectList(queryWrapper);
        
        // 将其他启用的规则禁用
        for (FeeRule rule : existingRules) {
            rule.setStatus("02");
            baseMapper.updateById(rule);
        }
    }
}