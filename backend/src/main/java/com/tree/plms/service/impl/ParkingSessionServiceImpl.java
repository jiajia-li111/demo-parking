package com.tree.plms.service.impl;

import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.ParkingSession;
import com.tree.plms.mapper.ParkingSessionMapper;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import com.tree.plms.service.ParkingSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 记录完整停车周期数据 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class ParkingSessionServiceImpl extends ServiceImpl<ParkingSessionMapper, ParkingSession> implements ParkingSessionService {

    @Override
    public Result<EntryResultVO> vehicleEntry(String licensePlate, String gateId) {
        // 实际实现会涉及：车辆识别、验证、车位分配、会话创建、过闸事件记录等
        // 这里提供基础框架，具体业务逻辑需要根据实际需求完善
        EntryResultVO resultVO = new EntryResultVO();
        // 设置正确的字段值，根据EntryResultVO的实际定义
        resultVO.setSessionId("s" + System.currentTimeMillis());
        resultVO.setSpaceId("b1_001");
        resultVO.setPass(true); // 使用setPass而不是setAllowed
        resultVO.setFloorName("地下一层");
        resultVO.setEntryTime(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        resultVO.setMessage("进场成功，已分配车位");
        
        return Result.success(resultVO);
    }

    @Override
    public Result<ExitResultVO> vehicleExit(String licensePlate, String gateId, String payMethod) {
        // 实际实现会涉及：车牌查询、会话查询、费用计算、支付处理、状态更新等
        ExitResultVO resultVO = new ExitResultVO();
        // 设置正确的字段值，根据ExitResultVO的实际定义
        resultVO.setSessionId("s" + System.currentTimeMillis());
        resultVO.setParkingHours(3.5);
        resultVO.setPayAmount(new BigDecimal("11.00")); // 使用BigDecimal类型的payAmount
        resultVO.setPayMethod(payMethod);
        resultVO.setPayMethodName(getPayMethodName(payMethod));
        resultVO.setPayStatus("01");
        resultVO.setPayStatusDesc("支付成功");
        resultVO.setExitTime(LocalDateTime.now());
        resultVO.setPayTime(LocalDateTime.now());
        resultVO.setPass(true); // 使用setPass而不是setAllowed
        resultVO.setMessage("出场成功，感谢使用");
        
        return Result.success(resultVO);
    }
    
    /**
     * 根据支付方式代码获取支付方式名称
     * @param payMethod 支付方式代码
     * @return 支付方式名称
     */
    private String getPayMethodName(String payMethod) {
        switch (payMethod) {
            case "01":
                return "微信支付";
            case "02":
                return "支付宝";
            case "03":
                return "现金";
            default:
                return "其他支付方式";
        }
    }

    @Override
    public ParkingSession getParkingSessionById(String sessionId) {
        return baseMapper.selectById(sessionId);
    }

    @Override
    public List<ParkingSession> getAllParkingSessions() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean addParkingSession(ParkingSession parkingSession) {
        return save(parkingSession);
    }

    @Override
    public boolean updateParkingSession(ParkingSession parkingSession) {
        return updateById(parkingSession);
    }

    @Override
    public boolean deleteParkingSession(String sessionId) {
        return removeById(sessionId);
    }

    @Override
    public List<ParkingSession> getParkingSessionsByVehicleId(String vehicleId) {
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_id", vehicleId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ParkingSession> getActiveParkingSessions() {
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("exit_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ParkingSession> getParkingSessionsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("entry_time", startTime, endTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ParkingSession> getParkingSessionsByEntryGateId(String entryGateId) {
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_gate_id", entryGateId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ParkingSession> getParkingSessionsByCardId(String cardId) {
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        return baseMapper.selectList(queryWrapper);
    }
}