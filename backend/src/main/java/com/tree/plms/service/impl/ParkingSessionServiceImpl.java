package com.tree.plms.service.impl;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.DailyStatsDTO;
import com.tree.plms.model.dto.response.MonthlyStatsDTO;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.*;
import com.tree.plms.mapper.ParkingSessionMapper;
import com.tree.plms.mapper.PaymentMapper;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import com.tree.plms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private FeeRuleService feeRuleService;

    @Autowired
    private GateService gateService;

    @Resource
    private PaymentService paymentService;


    @Resource
    private PaymentMapper paymentMapper;


    @Override
    @Transactional
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

        if(gateService.getGateById(gateId) == null){
            accessEventService.addAccessEvent(accessEvent);
            return Result.fail(ResultCodeEnum.GATE_NOT_FOUND);
        }

        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
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
            tempVehicle.setIsParking("02");// 初始状态为未在停车场内

            // 添加临时车辆
            boolean addSuccess = vehicleService.addVehicle(tempVehicle);
            // 添加临时车辆失败
            if (!addSuccess) {
                accessEventService.addAccessEvent(accessEvent);
                return Result.fail(ResultCodeEnum.TEMP_VEHICLE_CREATE_FAILED);
            }
            // 获取刚添加的临时车辆
            vehicle = tempVehicle;

        }else{
            if (!vehicle.getIsParking().equals("02")) {
                accessEventService.addAccessEvent(accessEvent);
                return Result.fail(ResultCodeEnum.VEHICLE_ALREADY_PARKING);
            }
        }

        // 设置车辆ID到过闸事件
        accessEvent.setVehicleId(vehicle.getVehicleId());

        // 4. 检查是否有空闲车位
        List<ParkingSpace> availableSpaces = parkingSpaceService.getAvailableNonFixedSpaces();
        if (availableSpaces.isEmpty()) {
            accessEventService.addAccessEvent(accessEvent);
            return Result.fail(ResultCodeEnum.PARKING_FULL);
        }

        // 5. 更新车辆状态为在停车场内
        vehicle.setIsParking("01");
        vehicleService.updateVehicle(vehicle);


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
            System.out.println("检查是否有有效的月卡");
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
    public Result<ExitResultVO> calculateFee(String licensePlate, String gateId) {
        if(gateService.getGateById(gateId) == null){
            return Result.fail(ResultCodeEnum.GATE_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();

        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return Result.fail(ResultCodeEnum.VEHICLE_NOT_EXIST);
        }
        if(!"01".equals(vehicle.getIsParking())){
            return Result.fail(ResultCodeEnum.VEHICLE_NOT_PARKING);
        }


        // 3. 查询未结束的停车会话
        QueryWrapper<ParkingSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_id", vehicle.getVehicleId());
        queryWrapper.isNull("exit_time");
        ParkingSession parkingSession = baseMapper.selectOne(queryWrapper);

        if (parkingSession == null) {
            return Result.fail(ResultCodeEnum.SESSION_NOT_FOUND);
        }

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

        FeeRule feeRule = null;
        // 根据月卡状态选择计费方式
        if (isMonthlyCardValid) {
            // 有效月卡，使用优惠规则
            // 查询月卡优惠规则（小型车）
            QueryWrapper<FeeRule> feeQuery = new QueryWrapper<>();
            feeQuery.eq("apply_to", "02")  // 使用月卡规则作为优惠规则
                    .eq("vehicle_type", "01")  // 小型车
                    .eq("status", "01");// 有效规则

            feeRule = feeRuleService.getOne(feeQuery);

        } else {
            // 月卡无效，使用标准计费
            // 简单的费用计算逻辑：首小时5元，后续每小时3元，每日封顶30元
            QueryWrapper<FeeRule> feeQuery = new QueryWrapper<>();
            feeQuery.eq("apply_to", "01")  // 使用临时车规则作为优惠规则
                    .eq("vehicle_type", "01")  // 小型车
                    .eq("status", "01");// 有效规则



            feeRule = feeRuleService.getOne(feeQuery);
        }

        if (feeRule != null) {
            // 应用优惠规则计算费用
            if (hours <= 1) {
                payAmount = feeRule.getFirstHourFee();
            } else {
                BigDecimal firstHourFee = feeRule.getFirstHourFee();
                BigDecimal nextHourFee = feeRule.getNextHourFee().multiply(new BigDecimal(hours - 1));
                payAmount = firstHourFee.add(nextHourFee);
            }
            // 应用每日封顶
            if (payAmount.compareTo(feeRule.getDailyCap()) > 0) {
                payAmount = feeRule.getDailyCap();
            }
        }else{
            return Result.fail(ResultCodeEnum.FEE_RULE_NOT_FOUND);
        }


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

        // 查询停车会话
        ParkingSession parkingSession = getParkingSessionById(sessionId);
        // 未查询到有效的停车会话
        if (parkingSession == null) {
            return Result.fail(ResultCodeEnum.SESSION_NOT_FOUND);
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
        accessEvent.setRecognitionResult("01");

        // 模拟支付处理
        boolean paymentSuccess = true; // 实际项目中这里应该调用支付接口

        // 支付失败
        if (!paymentSuccess) {
            accessEventService.addAccessEvent(accessEvent);
            return Result.fail(ResultCodeEnum.PAYMENT_FAILED);
        }

        // file:///D:/xwechat_files/wxid_o2yhw66eockq22_29c5/temp/RWTemp/2025-12/5a62bdba12ee494fe8648c50a64b304b/index.jsx更新停车会话
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
        FeeRule feeRule = null;
        // 根据月卡状态选择计费方式
        if (isMonthlyCardValid) {
            // 有效月卡，使用优惠规则
            // 查询月卡优惠规则（小型车）
            QueryWrapper<FeeRule> feeQuery = new QueryWrapper<>();
            feeQuery.eq("apply_to", "02")  // 使用月卡规则作为优惠规则
                    .eq("vehicle_type", "01")  // 小型车
                    .eq("status", "01");// 有效规则



            feeRule = feeRuleService.getOne(feeQuery);

        } else {
            // 月卡无效，使用标准计费
            // 简单的费用计算逻辑：首小时5元，后续每小时3元，每日封顶30元
            QueryWrapper<FeeRule> feeQuery = new QueryWrapper<>();
            feeQuery.eq("apply_to", "01")  // 使用临时车规则作为优惠规则
                    .eq("vehicle_type", "01")  // 小型车
                    .eq("status", "01");// 有效规则



            feeRule = feeRuleService.getOne(feeQuery);
        }

        if (feeRule != null) {
            // 应用优惠规则计算费用
            if (hours <= 1) {
                payAmount = feeRule.getFirstHourFee();
            } else {
                BigDecimal firstHourFee = feeRule.getFirstHourFee();
                BigDecimal nextHourFee = feeRule.getNextHourFee().multiply(new BigDecimal(hours - 1));
                payAmount = firstHourFee.add(nextHourFee);
            }
            // 应用每日封顶
            if (payAmount.compareTo(feeRule.getDailyCap()) > 0) {
                payAmount = feeRule.getDailyCap();
            }
        }else{
            return Result.fail(ResultCodeEnum.FEE_RULE_NOT_FOUND);
        }

        //返回支付结果前，修改车辆数据为未在停车场内
        Vehicle vehicle = vehicleService.getVehicleById(parkingSession.getVehicleId());
        vehicle.setIsParking("02");
        vehicleService.updateVehicle(vehicle);

        // 生成账单记录
        Payment payment = new Payment();
        String paymentId = "p" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        payment.setPaymentId(paymentId);
        payment.setSessionId(parkingSession.getSessionId());
        payment.setAmount(payAmount);
        payment.setPayMethod(getPayMethodID(payMethod));
        payment.setPayTime(now);
        payment.setStatus("01"); // 支付成功
        payment.setTransactionId("txn" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        paymentService.addPayment(payment);

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
    private String getPayMethodID(String payMethod) {
        return switch (payMethod) {
            case "微信支付" -> "01";
            case "支付宝" -> "02";
            case "现金" -> "03";
            default -> "03";
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

    // 在类末尾添加以下方法

    @Override
    public Result<DailyStatsDTO> getDailyStats(String date) {
        // 处理日期参数
        LocalDate statsDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        LocalDateTime startOfDay = statsDate.atStartOfDay();
        LocalDateTime endOfDay = statsDate.plusDays(1).atStartOfDay().minusSeconds(1);

        // 查询当日入场记录
        QueryWrapper<AccessEvent> entryQuery = new QueryWrapper<>();
        entryQuery.eq("event_type", "01") // 01=进场事件
                .between("event_time", startOfDay, endOfDay);
        long entryCount = accessEventService.count(entryQuery);

        // 查询当日出场记录
        QueryWrapper<AccessEvent> exitQuery = new QueryWrapper<>();
        exitQuery.eq("event_type", "02") // 02=出场事件
                .between("event_time", startOfDay, endOfDay);
        long exitCount = accessEventService.count(exitQuery);

        // 使用 PaymentService 计算当日收入
        BigDecimal totalRevenue = paymentService.calculateTotalAmountByTimeRange(startOfDay, endOfDay);

        // 查询当日时长
        QueryWrapper<ParkingSession> sessionQuery = new QueryWrapper<>();
        sessionQuery.between("exit_time", startOfDay, endOfDay);
        List<ParkingSession> sessions = baseMapper.selectList(sessionQuery);

        long totalMinutes = 0;
        int completedSessions = 0;

        for (ParkingSession session : sessions) {
            if (session.getExitTime() != null) {
                // 计算停车时长
                long minutes = ChronoUnit.MINUTES.between(session.getEntryTime(), session.getExitTime());
                totalMinutes += minutes;
                completedSessions++;
            }
        }

        // 计算平均停车时长
        int avgParkingMinutes = completedSessions > 0 ? (int)(totalMinutes / completedSessions) : 0;

        // 构建响应
        DailyStatsDTO dailyStats = new DailyStatsDTO();
        dailyStats.setDate(statsDate.toString());
        dailyStats.setEntryCount(entryCount);
        dailyStats.setExitCount(exitCount);
        dailyStats.setTotalRevenue(totalRevenue);
        dailyStats.setAvgParkingMinutes(avgParkingMinutes);

        return Result.success(dailyStats);
    }

    @Override
    public Result<MonthlyStatsDTO> getMonthlyStats(Integer year, Integer month) {
        // 处理年月参数
        LocalDate statsDate = LocalDate.of(year, month, 1);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        LocalDateTime startOfMonth = statsDate.atStartOfDay();
        LocalDateTime endOfMonthTime = endOfMonth.plusDays(1).atStartOfDay().minusSeconds(1);

        // 直接使用 PaymentService 计算整个月的收入，而不是逐日累加
        BigDecimal totalRevenue = paymentService.calculateTotalAmountByTimeRange(startOfMonth, endOfMonthTime);

        // 查询当月所有日期的统计数据
        List<DailyStatsDTO> dailyDetails = new ArrayList<>();
        int totalEntry = 0;
        int totalExit = 0;

        LocalDate currentDate = statsDate;
        while (!currentDate.isAfter(endOfMonth)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.plusDays(1).atStartOfDay().minusSeconds(1);

            // 查询当日入场记录
            QueryWrapper<AccessEvent> entryQuery = new QueryWrapper<>();
            entryQuery.eq("event_type", "01") // 01=进场事件
                    .between("event_time", dayStart, dayEnd);
            long entryCount = accessEventService.count(entryQuery);

            // 查询当日出场记录
            QueryWrapper<AccessEvent> exitQuery = new QueryWrapper<>();
            exitQuery.eq("event_type", "02") // 02=出场事件
                    .between("event_time", dayStart, dayEnd);
            long exitCount = accessEventService.count(exitQuery);

            // 查询当日收入（如果需要每日明细的话）
            BigDecimal dayRevenue = paymentService.calculateTotalAmountByTimeRange(dayStart, dayEnd);

            // 查询当日时长信息
            QueryWrapper<ParkingSession> sessionQuery = new QueryWrapper<>();
            sessionQuery.between("exit_time", dayStart, dayEnd);
            List<ParkingSession> sessions = baseMapper.selectList(sessionQuery);

            long dayMinutes = 0;
            int completedSessions = 0;

            for (ParkingSession session : sessions) {
                if (session.getExitTime() != null) {
                    long minutes = ChronoUnit.MINUTES.between(session.getEntryTime(), session.getExitTime());
                    dayMinutes += minutes;
                    completedSessions++;
                }
            }

            int avgParkingMinutes = completedSessions > 0 ? (int)(dayMinutes / completedSessions) : 0;

            // 添加到每日明细
            DailyStatsDTO dailyStats = new DailyStatsDTO();
            dailyStats.setDate(currentDate.toString());
            dailyStats.setEntryCount(entryCount);
            dailyStats.setExitCount(exitCount);
            dailyStats.setTotalRevenue(dayRevenue);
            dailyStats.setAvgParkingMinutes(avgParkingMinutes);
            dailyDetails.add(dailyStats);

            // 累加总数
            totalEntry += (int) entryCount;
            totalExit += (int) exitCount;

            currentDate = currentDate.plusDays(1);
        }

        // 计算月均数据
        int daysInMonth = yearMonth.lengthOfMonth();
        int avgDailyEntry = daysInMonth > 0 ? (totalExit + daysInMonth - 1) / daysInMonth : 0;
        int avgDailyExit = daysInMonth > 0 ? (totalExit + daysInMonth - 1) / daysInMonth : 0;

        // 构建响应
        MonthlyStatsDTO monthlyStats = new MonthlyStatsDTO();
        monthlyStats.setMonth(yearMonth.toString());
        monthlyStats.setAvgDailyEntry(avgDailyEntry);
        monthlyStats.setAvgDailyExit(avgDailyExit);
        monthlyStats.setMonthlyRevenue(totalRevenue);
        monthlyStats.setDailyDetails(dailyDetails);

        return Result.success(monthlyStats);
    }
}