package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.request.CreateMonthlyCardReq;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.MonthlyCard;
import com.tree.plms.model.entity.Vehicle;
import com.tree.plms.service.MonthlyCardService;
import com.tree.plms.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 月卡管理控制器
 */
@RestController
@RequestMapping("/monthly-cards")
@Tag(name = "月卡管理", description = "提供月卡相关的操作接口")
public class MonthlyCardController {
    
    @Resource
    private MonthlyCardService monthlyCardService;

    @Resource
    private VehicleService vehicleService;
    
    /**
     * 获取月卡列表
     */
    @GetMapping
    @Operation(summary = "获取月卡列表", description = "返回所有月卡的列表")
    public Result<List<MonthlyCard>> getMonthlyCardList() {
        List<MonthlyCard> cards = monthlyCardService.getAllMonthlyCards();
        return Result.success(cards);
    }
    
    /**
     * 创建月卡
     */
    @PostMapping
    @Operation(summary = "创建月卡", description = "根据车牌号创建月卡，仅支持业主车")
    public Result<Boolean> createMonthlyCard(@RequestBody CreateMonthlyCardReq request) {
        // 1. 验证请求参数中的车牌号
        String licensePlate = request.getLicensePlate();
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "车牌号不能为空");
        }
        
        // 2. 根据车牌号查询车辆信息
        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return Result.fail(ResultCodeEnum.NOT_FOUND, "该车牌号未绑定车辆");
        }
        
        // 3. 验证车辆是否为业主车
        if (!"01".equals(vehicle.getIsOwnerCar())) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "只有业主车才能办理月卡");
        }
        
        // 4. 检查该车辆是否已有有效的月卡
        List<MonthlyCard> existingCards = monthlyCardService.getMonthlyCardsByVehicleId(vehicle.getVehicleId());
        if (!existingCards.isEmpty()) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "该车辆已有月卡，请勿重复办理");
        }

        MonthlyCard monthlyCard = new MonthlyCard();
        
        // 5. 生成月卡ID
        if (monthlyCard.getCardId() == null) {
            // 查询当前最大的月卡ID
            QueryWrapper<MonthlyCard> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("card_id");
            queryWrapper.last("LIMIT 1");
            MonthlyCard lastCard = monthlyCardService.getBaseMapper().selectOne(queryWrapper);

            String newCardId;
            if (lastCard != null) {
                // 提取数字部分并加1
                String lastId = lastCard.getCardId();
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newCardId = String.format("c%05d", num);
            } else {
                // 第一个月卡
                newCardId = "c00001";
            }
            monthlyCard.setCardId(newCardId);
        }

        // 6. 设置车辆ID到月卡对象
        monthlyCard.setIssuerId(request.getIssuerId());
        monthlyCard.setStatus("01"); // 初始状态为启用
        monthlyCard.setVehicleId(vehicle.getVehicleId());
        monthlyCard.setStartDate(LocalDateTime.now());
        monthlyCard.setEndDate(request.getEndDate());

        // 7. 创建月卡
        boolean success = monthlyCardService.addMonthlyCard(monthlyCard);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建失败");
    }
    
    /**
     * 获取月卡详情
     */
    @GetMapping("/{cardId}")
    @Operation(summary = "获取月卡详情", description = "根据ID获取月卡的详情")
    public Result<MonthlyCard> getMonthlyCardById(@PathVariable String cardId) {
        MonthlyCard card = monthlyCardService.getMonthlyCardById(cardId);
        if (card == null) {
            return Result.fail(ResultCodeEnum.NOT_FOUND, "月卡不存在");
        }
        return Result.success(card);
    }
    
    /**
     * 更新月卡
     */
    @PutMapping("/{cardId}")
    @Operation(summary = "更新月卡", description = "根据ID更新月卡的详细信息")
    public Result<Boolean> updateMonthlyCard(@PathVariable String cardId, @RequestBody MonthlyCard monthlyCard) {
        // 确保路径参数和请求体中的ID一致
        if (!cardId.equals(monthlyCard.getCardId())) {
            return Result.fail(ResultCodeEnum.PARAM_ERROR, "月卡ID不匹配");
        }
        
        boolean success = monthlyCardService.updateMonthlyCard(monthlyCard);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "更新失败");
    }
    
    /**
     * 删除月卡
     */
    @DeleteMapping("/{cardId}")
    @Operation(summary = "删除月卡", description = "根据ID删除月卡")
    public Result<Boolean> deleteMonthlyCard(@PathVariable String cardId) {
        boolean success = monthlyCardService.deleteMonthlyCard(cardId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }
}