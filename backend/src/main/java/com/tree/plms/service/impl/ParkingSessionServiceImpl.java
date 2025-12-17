package com.tree.plms.service.impl;

import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.*;
import com.tree.plms.mapper.ParkingSessionMapper;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import com.tree.plms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Resource
    private VehicleService vehicleService;

    @Resource
    private ParkingSpaceService parkingSpaceService;

    @Resource
    private MonthlyCardService monthlyCardService;

    @Resource
    private FloorService floorService;

    @Resource
    private AccessEventService accessEventService;

    @Resource
    private FeeRuleService feeRuleService; // 新增：注入FeeRuleService

    @Override
    public Result<EntryResultVO> vehicleEntry(String licensePlate, String gateId) {
        LocalDateTime now = LocalDateTime.now();
        String eventId = "e" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        // 1. 记录进场过闸事件（初始状态为拦截）
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setEventId(eventId);
        accessEvent.setGateId(gateId);
        accessEvent.setEventTime(now);
        accessEvent.setRecognitionResult("01"); // 识别成功
        accessEvent.setEventType("01"); // 进场事件
        accessEvent.setHandleStatus("02"); // 初始状态为拦截
        
        Vehicle vehicle;
        
        // 2. 检查车牌号是否存在于数据库中
        vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        
        // 3. 如果车辆不存在（包括车牌号为空或不在数据库中），则创建临时车辆
        if (vehicle == null) {
            // 3.1 创建临时车辆记录
            Vehicle tempVehicle = new Vehicle();
            
            // 如果车牌号不为空，则使用实际车牌号，否则生成临时车牌号
            if (licensePlate != null && !licensePlate.trim().isEmpty()) {
                tempVehicle.setLicensePlate(licensePlate);
            } else {
                tempVehicle.setLicensePlate("临时车辆-" + System.currentTimeMillis());
            }
            
            tempVehicle.setVehicleType("01"); // 默认为小型车
            tempVehicle.setIsOwnerCar("02"); // 非业主车
            
            // 添加临时车辆
            boolean addSuccess = vehicleService.addVehicle(tempVehicle);
            if (!addSuccess) {
                accessEventService.addAccessEvent(accessEvent);
                EntryResultVO resultVO = new EntryResultVO();
                resultVO.setPass(false);
                resultVO.setMessage("创建临时车辆失败，请重试");
                return Result.success(resultVO);
            }
            
            // 获取刚添加的临时车辆
            vehicle = tempVehicle;
        }
        
        // 设置车辆ID到过闸事件
        accessEvent.setVehicleId(vehicle.getVehicleId());
    
        // 4. 检查是否有空闲车位
        List<ParkingSpace> availableSpaces = parkingSpaceService.getAvailableNonFixedSpaces();
        if (availableSpaces.isEmpty()) {
            accessEventService.addAccessEvent(accessEvent);
            EntryResultVO resultVO = new EntryResultVO();
            resultVO.setPass(false);
            resultVO.setMessage("停车场已满，无法入场");
            return Result.success(resultVO);
        }
    
        // 选择第一个空闲车位
        ParkingSpace selectedSpace = availableSpaces.get(0);
    
        // 5. 更新车位状态为占用
        selectedSpace.setStatus("02");
        parkingSpaceService.updateParkingSpace(selectedSpace);
    
        // 6. 获取楼层名称
        Floor floor = floorService.getFloorById(selectedSpace.getFloorId());
        String floorName = floor != null ? floor.getFloorName() : "未知楼层";
    
        // 7. 获取适用的计费规则ID
        String ruleId = getAppropriateFeeRuleId(vehicle.getVehicleType());
    
        // 8. 创建停车会话
        ParkingSession parkingSession = new ParkingSession();
        String sessionId = "s" + System.currentTimeMillis();
        parkingSession.setSessionId(sessionId);
        parkingSession.setVehicleId(vehicle.getVehicleId()); // 使用车辆ID
        parkingSession.setEntryTime(now);
        parkingSession.setEntryGateId(gateId);
        parkingSession.setSpaceId(selectedSpace.getSpaceId());
        parkingSession.setRuleId(ruleId); // 新增：设置ruleId字段
    
        // 检查是否有有效的月卡（只有业主车才有月卡）
        if ("01".equals(vehicle.getIsOwnerCar())) {
            List<MonthlyCard> monthlyCards = monthlyCardService.getMonthlyCardsByVehicleId(vehicle.getVehicleId());
            if (!monthlyCards.isEmpty()) {
                MonthlyCard monthlyCard = monthlyCards.get(0);
                if ("01".equals(monthlyCard.getStatus()) && now.isBefore(monthlyCard.getEndDate())) {
                    parkingSession.setCardId(monthlyCard.getCardId());
                }
            }
        }
    
        save(parkingSession);
    
        // 9. 更新过闸事件为放行状态
        accessEvent.setSessionId(sessionId);
        accessEvent.setHandleStatus("01"); // 放行状态
        accessEventService.addAccessEvent(accessEvent);
    
        // 10. 返回进场结果
        EntryResultVO resultVO = new EntryResultVO();
        resultVO.setSessionId(sessionId);
        resultVO.setSpaceId(selectedSpace.getSpaceId());
        resultVO.setPass(true);
        resultVO.setFloorName(floorName);
        resultVO.setEntryTime(now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        resultVO.setMessage("进场成功，已分配车位");
    
        return Result.success(resultVO);
    }

    /**
     * 获取合适的计费规则ID
     * @param vehicleType 车辆类型
     * @return 计费规则ID
     */
    private String getAppropriateFeeRuleId(String vehicleType) {
        // 默认规则ID
        String defaultRuleId = "r00001";
        
        try {
            // 根据车辆类型查询计费规则
            List<FeeRule> feeRules = feeRuleService.getFeeRulesByVehicleType(vehicleType);
            
            if (!feeRules.isEmpty()) {
                // 返回第一条规则的ID
                return feeRules.get(0).getRuleId();
            }
            
            // 如果没有找到对应车辆类型的规则，返回默认规则ID
            return defaultRuleId;
        } catch (Exception e) {
            // 发生异常时返回默认规则ID
            return defaultRuleId;
        }
    }

    @Override
    public Result<ExitResultVO> vehicleExit(String licensePlate, String gateId, String payMethod) {
        // 直接调用新的两阶段接口实现
        Result<ExitResultVO> feeResult = calculateFee(licensePlate, gateId);
        if (!feeResult.isSuccess() || feeResult.getData() == null) {
            return feeResult;
        }
        return processPayment(feeResult.getData().getSessionId(), gateId, payMethod);
    }

    @Override
    public Result<ExitResultVO> calculateFee(String licensePlate, String gateId) {
        LocalDateTime now = LocalDateTime.now();
        String eventId = "e" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        // 1. 记录出场过闸事件（初始状态为拦截）
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setEventId(eventId);
        accessEvent.setGateId(gateId);
        accessEvent.setEventTime(now);
        accessEvent.setEventType("02"); // 出场事件
        accessEvent.setHandleStatus("02"); // 初始状态为拦截

        // 2. 查询车辆信息
        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            accessEventService.addAccessEvent(accessEvent);
            ExitResultVO resultVO = new ExitResultVO();
            resultVO.setPass(false);
            resultVO.setMessage("车辆不存在");
            return Result.success(resultVO);
        }
        accessEvent.setVehicleId(vehicle.getVehicleId());
        accessEvent.setRecognitionResult("01");

        // 3. 查询未结束的停车会话
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_id", vehicle.getVehicleId());
        queryWrapper.isNull("exit_time");
        ParkingSession parkingSession = baseMapper.selectOne(queryWrapper);

        if (parkingSession == null) {
            accessEventService.addAccessEvent(accessEvent);
            ExitResultVO resultVO = new ExitResultVO();
            resultVO.setPass(false);
            resultVO.setMessage("未查询到有效的停车会话");
            return Result.success(resultVO);
        }
        accessEvent.setSessionId(parkingSession.getSessionId());

        // 4. 计算停车时长
        long minutes = ChronoUnit.MINUTES.between(parkingSession.getEntryTime(), now);
        double hours = Math.ceil(minutes / 60.0); // 向上取整到小时

        // 5. 计算费用
        BigDecimal payAmount = BigDecimal.ZERO;
        boolean isMonthlyCardValid = false;

        // 检查月卡是否有效
        if (parkingSession.getCardId() != null) {
            MonthlyCard monthlyCard = monthlyCardService.getMonthlyCardById(parkingSession.getCardId());
            if (monthlyCard != null && "01".equals(monthlyCard.getStatus()) && now.isBefore(monthlyCard.getEndDate())) {
                isMonthlyCardValid = true;
            }
        }

        // 如果月卡无效，计算临时停车费用
        if (!isMonthlyCardValid) {
            // 简单的费用计算逻辑：首小时5元，后续每小时3元，每日封顶30元
            if (hours <= 1) {
                payAmount = new BigDecimal("5.00");
            } else {
                BigDecimal firstHourFee = new BigDecimal("5.00");
                BigDecimal nextHourFee = new BigDecimal("3.00");
                BigDecimal totalFee = firstHourFee.add(nextHourFee.multiply(new BigDecimal(hours - 1)));
                // 每日封顶30元
                payAmount = totalFee.compareTo(new BigDecimal("30.00")) > 0 ? new BigDecimal("30.00") : totalFee;
            }
        }

        // 保存过闸事件，但不更新状态（仅用于记录）
        accessEventService.addAccessEvent(accessEvent);

        // 6. 返回费用计算结果
        ExitResultVO resultVO = new ExitResultVO();
        resultVO.setSessionId(parkingSession.getSessionId());
        resultVO.setParkingHours(hours);
        resultVO.setPayAmount(payAmount);
        resultVO.setPass(false); // 仅计算费用，暂不放行
        resultVO.setMessage("费用计算成功，请进行支付");

        return Result.success(resultVO);
    }

    @Override
    public Result<ExitResultVO> processPayment(String sessionId, String gateId, String payMethod) {
        LocalDateTime now = LocalDateTime.now();
        String eventId = "e" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        // 1. 查询停车会话
        ParkingSession parkingSession = getParkingSessionById(sessionId);
        if (parkingSession == null) {
            ExitResultVO resultVO = new ExitResultVO();
            resultVO.setPass(false);
            resultVO.setMessage("未查询到有效的停车会话");
            return Result.success(resultVO);
        }

        // 2. 记录出场过闸事件
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setEventId(eventId);
        accessEvent.setGateId(gateId);
        accessEvent.setEventTime(now);
        accessEvent.setVehicleId(parkingSession.getVehicleId());
        accessEvent.setSessionId(sessionId);
        accessEvent.setEventType("02"); // 出场事件
        accessEvent.setHandleStatus("02"); // 初始状态为拦截

        // 3. 模拟支付处理
        boolean paymentSuccess = true; // 实际项目中这里应该调用支付接口

        if (!paymentSuccess) {
            accessEventService.addAccessEvent(accessEvent);
            ExitResultVO resultVO = new ExitResultVO();
            resultVO.setSessionId(sessionId);
            resultVO.setPass(false);
            resultVO.setMessage("支付失败，请重新支付");
            return Result.success(resultVO);
        }

        // 4. 更新停车会话
        parkingSession.setExitTime(now);
        parkingSession.setExitGateId(gateId);
        updateById(parkingSession);

        // 5. 更新车位状态为空闲
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpaceById(parkingSession.getSpaceId());
        if (parkingSpace != null) {
            parkingSpace.setStatus("01");
            parkingSpaceService.updateParkingSpace(parkingSpace);
        }

        // 6. 更新过闸事件为放行状态
        accessEvent.setHandleStatus("01"); // 放行状态
        accessEventService.addAccessEvent(accessEvent);

        // 7. 计算停车时长
        long minutes = ChronoUnit.MINUTES.between(parkingSession.getEntryTime(), now);
        double hours = Math.ceil(minutes / 60.0); // 向上取整到小时

        // 8. 计算费用（再次计算以确保准确性）
        BigDecimal payAmount = BigDecimal.ZERO;
        boolean isMonthlyCardValid = false;

        // 检查月卡是否有效
        if (parkingSession.getCardId() != null) {
            MonthlyCard monthlyCard = monthlyCardService.getMonthlyCardById(parkingSession.getCardId());
            if (monthlyCard != null && "01".equals(monthlyCard.getStatus()) && now.isBefore(monthlyCard.getEndDate())) {
                isMonthlyCardValid = true;
            }
        }

        // 如果月卡无效，计算临时停车费用
        if (!isMonthlyCardValid) {
            // 简单的费用计算逻辑：首小时5元，后续每小时3元，每日封顶30元
            if (hours <= 1) {
                payAmount = new BigDecimal("5.00");
            } else {
                BigDecimal firstHourFee = new BigDecimal("5.00");
                BigDecimal nextHourFee = new BigDecimal("3.00");
                BigDecimal totalFee = firstHourFee.add(nextHourFee.multiply(new BigDecimal(hours - 1)));
                // 每日封顶30元
                payAmount = totalFee.compareTo(new BigDecimal("30.00")) > 0 ? new BigDecimal("30.00") : totalFee;
            }
        }

        //返回支付结果前，删除临时车辆数据（仅非业主车）
        Vehicle vehicle = vehicleService.getVehicleById(parkingSession.getVehicleId());
        if (vehicle != null && "02".equals(vehicle.getIsOwnerCar())) {
            vehicleService.deleteVehicle(vehicle.getVehicleId());
        }

        // 9. 返回支付结果
        ExitResultVO resultVO = new ExitResultVO();
        resultVO.setSessionId(parkingSession.getSessionId());
        resultVO.setParkingHours(hours);
        resultVO.setPayAmount(payAmount);
        resultVO.setPayMethod(payMethod);
        resultVO.setPayMethodName(getPayMethodName(payMethod));
        resultVO.setPayStatus("01");
        resultVO.setPayStatusDesc("支付成功");
        resultVO.setExitTime(now);
        resultVO.setPayTime(now);
        resultVO.setPass(true);
        resultVO.setMessage("支付成功，出场成功");

        return Result.success(resultVO);
    }

    /**
     * 根据支付方式代码获取支付方式名称
     * @param payMethod 支付方式代码
     * @return 支付方式名称
     */
    private String getPayMethodName(String payMethod) {
        return switch (payMethod) {
            case "01" -> "微信支付";
            case "02" -> "支付宝";
            case "03" -> "现金";
            default -> "其他支付方式";
        };
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