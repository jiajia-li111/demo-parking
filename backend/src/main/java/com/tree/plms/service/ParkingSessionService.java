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
     * 车辆进场（识别车牌→验证车辆→分配车位→创建会话）
     * @param licensePlate 车牌号
     * @param gateId 进场通道ID（如"in1"）
     * @return 进场结果（会话ID、车位ID、是否放行）
     */
    Result<EntryResultVO> vehicleEntry(String licensePlate, String gateId);


    /**
     * 计算停车费用（两阶段出场第一阶段）
     * @param licensePlate 车牌号
     * @param gateId 出场通道ID
     * @return 费用计算结果
     */
    Result<ExitResultVO> calculateFee(String licensePlate, String gateId);

    /**
     * 处理停车支付（两阶段出场第二阶段）
     * @param sessionId 停车会话ID
     * @param gateId 出场通道ID
     * @param payMethod 支付方式
     * @return 支付结果
     */
    Result<ExitResultVO> processPayment(String sessionId, String gateId, String payMethod);

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