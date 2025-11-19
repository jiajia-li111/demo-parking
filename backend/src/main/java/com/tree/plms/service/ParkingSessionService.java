package com.tree.plms.service;

import com.tree.plms.model.entity.ParkingSession;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;

/**
 * <p>
 * 记录完整停车周期数据 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface ParkingSessionService extends IService<ParkingSession> {
    /**
     * 车辆进场（识别车牌→验证车辆→分配车位→创建会话→记录过闸事件）
     * @param licensePlate 车牌号
     * @param gateId 进场通道ID（如"in1"）
     * @return 进场结果（会话ID、车位ID、是否放行）
     */
    Result<EntryResultVO> vehicleEntry(String licensePlate, String gateId);

    /**
     * 车辆出场（识别车牌→查询会话→计算费用→生成支付单→更新状态→记录过闸事件）
     * @param licensePlate 车牌号
     * @param gateId 出场通道ID（如"out1"）
     * @param payMethod 支付方式（01=微信，02=支付宝，03=现金）
     * @return 出场结果（应付金额、支付状态、是否放行）
     */
    Result<ExitResultVO> vehicleExit(String licensePlate, String gateId, String payMethod);
}
