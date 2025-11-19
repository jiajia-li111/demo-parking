package com.tree.plms.controller;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.Gate;
import com.tree.plms.service.GateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通道管理控制器
 */
@RestController
@RequestMapping("/gates")
public class GateController {
    
    @Resource
    private GateService gateService;
    
    /**
     * 获取通道列表
     */
    @GetMapping
    public Result<List<Gate>> getGateList() {
        List<Gate> gates = gateService.getAllGates();
        return Result.success(gates);
    }
    
    /**
     * 创建通道
     */
    @PostMapping
    public Result<Boolean> createGate(@RequestBody Gate gate) {
        boolean success = gateService.addGate(gate);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建失败");
    }
    
    /**
     * 更新通道
     */
    @PutMapping("/{gateId}")
    public Result<Boolean> updateGate(@PathVariable String gateId, @RequestBody Gate gate) {
        // 确保路径参数和请求体中的ID一致
        if (!gateId.equals(gate.getGateId())) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "通道ID不匹配");
        }
        
        boolean success = gateService.updateGate(gate);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新失败");
    }
    
    /**
     * 删除通道
     */
    @DeleteMapping("/{gateId}")
    public Result<Boolean> deleteGate(@PathVariable String gateId) {
        boolean success = gateService.deleteGate(gateId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }
}