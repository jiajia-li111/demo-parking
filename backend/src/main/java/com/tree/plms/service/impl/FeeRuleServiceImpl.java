package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.model.entity.FeeRule;
import com.tree.plms.mapper.FeeRuleMapper;
import com.tree.plms.service.FeeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
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
    public boolean addFeeRule(FeeRule feeRule) {
        return this.save(feeRule);
    }

    /**
     * 更新计费规则
     * @param feeRule 计费规则对象
     * @return 是否更新成功
     */
    @Override
    public boolean updateFeeRule(FeeRule feeRule) {
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



}