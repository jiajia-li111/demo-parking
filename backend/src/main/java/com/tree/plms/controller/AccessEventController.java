package com.tree.plms.controller;

/**
 * @author SuohaChan
 * @data 2025/12/21
 */

import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.AccessEvent;
import com.tree.plms.model.vo.AccessEventVO;
import com.tree.plms.service.AccessEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 过闸事件管理控制器
 */
@RestController
@RequestMapping("/access-events")
@Tag(name = "过闸事件管理", description = "处理过闸事件的查询、创建等操作")
public class AccessEventController {

    @Autowired
    private AccessEventService accessEventService;

    /**
     * 获取所有过闸事件列表（包含车牌号）
     */
    @GetMapping
    @Operation(summary = "获取过闸事件列表", description = "获取所有过闸事件的列表（包含车辆车牌号）")
    public Result<List<AccessEventVO>> getAccessEvents() {
        List<AccessEventVO> accessEvents = accessEventService.getAllAccessEventsWithLicensePlate();
        return Result.success(accessEvents);
    }

    /**
     * 根据事件ID获取单个过闸事件（包含车牌号）
     */
    @GetMapping("/{eventId}")
    @Operation(summary = "获取单个过闸事件", description = "根据事件ID获取单个过闸事件的详细信息（包含车辆车牌号）")
    public Result<AccessEventVO> getAccessEvent(@PathVariable String eventId) {
        AccessEventVO accessEvent = accessEventService.getAccessEventWithLicensePlateById(eventId);
        return Result.success(accessEvent);
    }

}
