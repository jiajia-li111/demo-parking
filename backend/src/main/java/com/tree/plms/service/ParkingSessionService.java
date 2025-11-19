package com.tree.plms.service;

import com.tree.plms.model.entity.ParkingSession;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import java.time.LocalDateTime;
import java.util.List;

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


    /**
     * 根据会话ID查询停车会话信息
     * @param sessionId 会话标识
     * @return 停车会话信息
     */
    ParkingSession getParkingSessionById(String sessionId);
    
    /**
     * 获取所有停车会话列表
     * @return 停车会话列表
     */
    List<ParkingSession> getAllParkingSessions();
    
    /**
     * 新增停车会话
     * @param parkingSession 停车会话信息
     * @return 是否添加成功
     */
    boolean addParkingSession(ParkingSession parkingSession);
    
    /**
     * 更新停车会话
     * @param parkingSession 停车会话信息
     * @return 是否更新成功
     */
    boolean updateParkingSession(ParkingSession parkingSession);
    
    /**
     * 根据会话ID删除停车会话
     * @param sessionId 会话标识
     * @return 是否删除成功
     */
    boolean deleteParkingSession(String sessionId);
    
    /**
     * 根据车辆ID查询停车会话列表
     * @param vehicleId 关联车辆ID
     * @return 停车会话列表
     */
    List<ParkingSession> getParkingSessionsByVehicleId(String vehicleId);
    
    /**
     * 查询当前在场的车辆会话（exitTime为null）
     * @return 在场车辆会话列表
     */
    List<ParkingSession> getActiveParkingSessions();
    
    /**
     * 根据时间范围查询停车会话
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 停车会话列表
     */
    List<ParkingSession> getParkingSessionsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据进场通道ID查询停车会话
     * @param entryGateId 进场通道ID
     * @return 停车会话列表
     */
    List<ParkingSession> getParkingSessionsByEntryGateId(String entryGateId);
    
    /**
     * 根据月卡ID查询相关的停车会话
     * @param cardId 关联月卡ID
     * @return 停车会话列表
     */
    List<ParkingSession> getParkingSessionsByCardId(String cardId);
}