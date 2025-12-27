package com.tree.plms.controller;

import com.tree.plms.service.ParkingSessionService;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 出闸入闸管理控制器
 */
@RestController
@RequestMapping("/parking-session")
@Tag(name = "出闸入闸管理")
public class AccessController {

    private final ParkingSessionService parkingSessionService;

    public AccessController(ParkingSessionService parkingSessionService) {
        this.parkingSessionService = parkingSessionService;
    }


    /**
     * 车辆进场接口（识别车牌）
     */
    @PostMapping("/entry")
    @Operation(summary = "车辆进场接口", description = "识别车牌号，进行车辆进场处理")
    public Result<EntryResultVO> vehicleEntry(@RequestParam String licensePlate, @RequestParam String gateId) {
        return parkingSessionService.vehicleEntry(licensePlate, gateId);
    }

    /**
     * 计算停车费用接口（两阶段出场第一阶段）
     */
    @PostMapping("/calculate-fee")
    @Operation(summary = "计算停车费用接口", description = "计算车辆停车费用")
    public Result<ExitResultVO> calculateFee(@RequestParam String licensePlate, @RequestParam String gateId) {
        return parkingSessionService.calculateFee(licensePlate, gateId);
    }

    /**
     * 处理停车支付接口（两阶段出场第二阶段）
     */
    @PostMapping("/process-payment")
    @Operation(summary = "处理停车支付接口", description = "处理车辆停车支付，记录过闸事件")
    public Result<ExitResultVO> processPayment(@RequestParam String sessionId, @RequestParam String gateId, @RequestParam String payMethod) {
        return parkingSessionService.processPayment(sessionId, gateId, payMethod);
    }
}