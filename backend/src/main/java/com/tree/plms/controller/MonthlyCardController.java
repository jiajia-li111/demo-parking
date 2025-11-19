package com.tree.plms.controller;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.MonthlyCard;
import com.tree.plms.service.MonthlyCardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 月卡管理控制器
 */
@RestController
@RequestMapping("/monthly-cards")
public class MonthlyCardController {
    
    @Resource
    private MonthlyCardService monthlyCardService;
    
    /**
     * 获取月卡列表
     */
    @GetMapping
    public Result<List<MonthlyCard>> getMonthlyCardList() {
        List<MonthlyCard> cards = monthlyCardService.getAllMonthlyCards();
        return Result.success(cards);
    }
    
    /**
     * 创建月卡
     */
    @PostMapping
    public Result<Boolean> createMonthlyCard(@RequestBody MonthlyCard monthlyCard) {
        boolean success = monthlyCardService.addMonthlyCard(monthlyCard);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建失败");
    }
    
    /**
     * 获取月卡详情
     */
    @GetMapping("/{cardId}")
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
    public Result<Boolean> deleteMonthlyCard(@PathVariable String cardId) {
        boolean success = monthlyCardService.deleteMonthlyCard(cardId);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "删除失败");
    }
}