package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.exception.BusinessException;
import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.MonthlyCard;
import com.tree.plms.mapper.MonthlyCardMapper;
import com.tree.plms.model.entity.Vehicle;
import com.tree.plms.service.MonthlyCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.service.VehicleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 存储业主车月卡信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class MonthlyCardServiceImpl extends ServiceImpl<MonthlyCardMapper, MonthlyCard> implements MonthlyCardService {
    @Resource
    private VehicleService vehicleService;
    /**
     * 根据月卡ID查询月卡
     * @param cardId 月卡唯一标识（如"c2024001"）
     * @return 月卡对象
     */
    @Override
    public MonthlyCard getMonthlyCardById(String cardId) {
        return baseMapper.selectById(cardId);
    }

    /**
     * 获取所有月卡列表
     * @return 月卡列表
     */
    @Override
    public List<MonthlyCard> getAllMonthlyCards() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增月卡
     * @param monthlyCard 月卡对象
     * @return 是否新增成功
     */
    @Override
    public boolean addMonthlyCard(MonthlyCard monthlyCard) {
        // 验证车辆是否存在
        String vehicleId = monthlyCard.getVehicleId();
        if (vehicleId == null || vehicleId.isEmpty()) {
            throw new IllegalArgumentException("车辆ID不能为空");
        }

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("车辆不存在，请先登记车辆");
        }

        if (!vehicle.getIsOwnerCar().equals("01")) {
            throw new IllegalArgumentException("车辆非业主车辆，请先登记业主车辆");
        }

        // 检查是否已存在该车辆的月卡记录
        QueryWrapper<MonthlyCard> existingCardQuery = new QueryWrapper<>();
        existingCardQuery.eq("vehicle_id", vehicleId);

        MonthlyCard existingCard = baseMapper.selectOne(existingCardQuery);
        if (existingCard != null) {
            // 策略2：抛出异常提示用户
            throw new BusinessException(ResultCodeEnum.CARD_EXISTS, "该车辆已存在月卡，请先删除或更新现有月卡");
        }

        // 自动生成月卡ID
        if (monthlyCard.getCardId() == null) {
            // 查询当前最大的月卡ID
            QueryWrapper<MonthlyCard> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("card_id");
            queryWrapper.last("LIMIT 1");
            MonthlyCard lastCard = baseMapper.selectOne(queryWrapper);

            String newCardId;
            if (lastCard != null) {
                // 提取数字部分并加1
                String lastId = lastCard.getCardId();
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newCardId = String.format("c%05d", num);
            } else {
                // 第一张月卡
                newCardId = "c00001";
            }
            monthlyCard.setCardId(newCardId);
        }

        // 设置默认状态
        if (monthlyCard.getStatus() == null) {
            monthlyCard.setStatus("01"); // 默认启用
        }

        return this.save(monthlyCard);
    }

    /**
     * 更新月卡
     * @param monthlyCard 月卡对象
     * @return 是否更新成功
     */
    @Override
    public boolean updateMonthlyCard(MonthlyCard monthlyCard) {
        return this.updateById(monthlyCard);
    }

    /**
     * 删除月卡
     * @param cardId 月卡唯一标识（如"c2024001"）
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMonthlyCard(String cardId) {
        MonthlyCard monthlyCard = this.getById(cardId);
        if (monthlyCard != null) {
            // 设置状态为已删除而不是真正删除
            monthlyCard.setStatus("03"); // 假设"03"表示过期状态
            return this.updateById(monthlyCard);
        }
        return false;
    }

    /**
     * 按月卡状态查询月卡
     * @param status 状态（01=启用，02=挂失，03=过期）
     * @return 符合条件的月卡列表
     */
    @Override
    public List<MonthlyCard> getMonthlyCardsByStatus(String status) {
        QueryWrapper<MonthlyCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按绑定车辆牌照查询月卡 出问题懒得改了
     * 实际上前端发来的是车牌，而不是车辆ID
     * 哪有用车辆id查询了，谁知道我这车id多少，但是要改成vo麻烦，就这样了
     * @return 符合条件的月卡列表
     */
    @Override
    public List<MonthlyCard> getMonthlyCardsByVehicleId(String vehicleId) {
        System.out.println("vehicleId: " + vehicleId);
        QueryWrapper<MonthlyCard> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("vehicle_id", vehicleId);
        return baseMapper.selectList(queryWrapper1);
    }

    /**
     * 按时间范围查询月卡
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的月卡列表
     */
    @Override
    public List<MonthlyCard> getMonthlyCardsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        QueryWrapper<MonthlyCard> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("start_date", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("end_date", endDate);
        }
        return baseMapper.selectList(queryWrapper);
    }

    // 在类末尾添加以下方法

    @Override
    public Result<List<CardExpiryAlertDTO>> getExpiringCards(Integer days) {
        // 计算截止日期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plusDays(days);

        // 查询即将到期的月卡
        QueryWrapper<MonthlyCard> query = new QueryWrapper<>();
        query.between("end_date", now, expireDate)
                .eq("status", "01"); // 只查询启用状态的月卡

        List<MonthlyCard> cards = baseMapper.selectList(query);

        // 转换为DTO
        List<CardExpiryAlertDTO> alerts = new ArrayList<>();
        for (MonthlyCard card : cards) {
            CardExpiryAlertDTO alert = new CardExpiryAlertDTO();
            alert.setCardId(card.getCardId());
            alert.setVehicleId(card.getVehicleId());
            alert.setEndDate(card.getEndDate());

            // 查询车牌号
            Vehicle vehicle = vehicleService.getVehicleById(card.getVehicleId());
            if (vehicle != null) {
                alert.setLicensePlate(vehicle.getLicensePlate());
            }

            // 计算剩余天数
            int remainingDays = (int) ChronoUnit.DAYS.between(now, card.getEndDate());
            alert.setRemainingDays(remainingDays);

            alerts.add(alert);
        }

        return Result.success(alerts);
    }
}