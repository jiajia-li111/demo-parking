package com.tree.plms.controller;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.AccessEvent;
import com.tree.plms.service.AccessEventService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 过闸事件控制器
 */
@RestController
@RequestMapping("/access-events")
public class AccessEventController {
    
    @Resource
    private AccessEventService accessEventService;
    
    /**
     * 获取过闸事件列表
     */
    @GetMapping
    public Result<List<AccessEvent>> getAccessEventList() {
        List<AccessEvent> events = accessEventService.getAllAccessEvents();
        return Result.success(events);
    }
    
    /**
     * 创建过闸事件
     */
    @PostMapping
    public Result<Boolean> createAccessEvent(@RequestBody AccessEvent accessEvent) {
        boolean success = accessEventService.addAccessEvent(accessEvent);
        return success ? Result.success(true) : Result.fail(ResultCodeEnum.SYSTEM_ERROR, "创建失败");
    }
    
    /**
     * 获取过闸事件详情
     */
    @GetMapping("/{eventId}")
    public Result<AccessEvent> getAccessEventById(@PathVariable String eventId) {
        AccessEvent event = accessEventService.getAccessEventById(eventId);
        if (event == null) {
            return Result.fail(ResultCodeEnum.NOT_FOUND, "过闸事件不存在");
        }
        return Result.success(event);
    }
}