package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.MonthlyCard;
import com.tree.plms.model.entity.SysUser;
import com.tree.plms.service.MonthlyCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "创建月卡", description = "创建一个新的月卡")
    public Result<Boolean> createMonthlyCard(@RequestBody MonthlyCard monthlyCard) {
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