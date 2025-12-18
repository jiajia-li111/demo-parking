package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.FeeRule;
import com.tree.plms.service.FeeRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收费规则管理控制器
 */
@RestController
@RequestMapping("/fee-rule")
@Tag(name = "收费规则管理")
public class FeeRuleController {

    @Resource
    private FeeRuleService feeRuleService;

    /**
     * 获取单个收费规则
     */
    @GetMapping("/{ruleId}")
    @Operation(summary = "获取单个收费规则", description = "根据规则ID查询收费规则详情")
    public Result<FeeRule> getFeeRuleById(@PathVariable String ruleId) {
        FeeRule feeRule = feeRuleService.getFeeRuleById(ruleId);
        if (feeRule == null) {
            return Result.fail(ResultCodeEnum.FEE_RULE_NOT_FOUND);
        }
        return Result.success(feeRule);
    }

    /**
     * 获取所有收费规则列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有收费规则列表", description = "查询所有收费规则")
    public Result<List<FeeRule>> getAllFeeRules() {
        List<FeeRule> feeRules = feeRuleService.getAllFeeRules();
        return Result.success(feeRules);
    }

    /**
     * 创建收费规则
     */
    @PostMapping("/create")
    @Operation(summary = "创建收费规则", description = "创建新的收费规则")
    public Result<Boolean> createFeeRule(@RequestBody FeeRule feeRule) {

        if (feeRule.getRuleId() == null) {
            // 查询当前最大的月卡ID
            QueryWrapper<FeeRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("rule_id");
            queryWrapper.last("LIMIT 1");
            FeeRule lastRule = feeRuleService.getBaseMapper().selectOne(queryWrapper);
            String newRuleId;
            if (lastRule != null) {
                // 提取数字部分并加1
                String lastId = lastRule.getRuleId();
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newRuleId = String.format("r%05d", num);
            } else {
                newRuleId = "r00001";
            }
            feeRule.setRuleId(newRuleId);
        }
        boolean success = feeRuleService.addFeeRule(feeRule);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建收费规则失败");
    }

    /**
     * 更新收费规则
     */
    @PutMapping("/{ruleId}")
    @Operation(summary = "更新收费规则", description = "更新指定的收费规则")
    public Result<Boolean> updateFeeRule(@PathVariable String ruleId, @RequestBody FeeRule feeRule) {
        // 确保路径参数和请求体中的ID一致
        if (!ruleId.equals(feeRule.getRuleId())) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "收费规则ID不匹配");
        }
        // 检查规则是否存在
        if (feeRuleService.getFeeRuleById(ruleId) == null) {
            return Result.fail(ResultCodeEnum.FEE_RULE_NOT_FOUND);
        }
        boolean success = feeRuleService.updateFeeRule(feeRule);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新收费规则失败");
    }

    /**
     * 删除收费规则
     */
    @DeleteMapping("/{ruleId}")
    @Operation(summary = "删除收费规则", description = "删除指定的收费规则")
    public Result<Boolean> deleteFeeRule(@PathVariable String ruleId) {
        // 检查规则是否存在
        if (feeRuleService.getFeeRuleById(ruleId) == null) {
            return Result.fail(ResultCodeEnum.FEE_RULE_NOT_FOUND);
        }
        boolean success = feeRuleService.deleteFeeRule(ruleId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除收费规则失败");
    }

    /**
     * 根据车辆类型查询收费规则
     */
    @GetMapping("/by-vehicle-type/{vehicleType}")
    @Operation(summary = "根据车辆类型查询收费规则", description = "根据车辆类型查询适用的收费规则")
    public Result<List<FeeRule>> getFeeRulesByVehicleType(@PathVariable String vehicleType) {
        List<FeeRule> feeRules = feeRuleService.getFeeRulesByVehicleType(vehicleType);
        return Result.success(feeRules);
    }
}