package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.model.entity.MonthlyCard;
import com.tree.plms.mapper.MonthlyCardMapper;
import com.tree.plms.service.MonthlyCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
        return this.removeById(cardId);
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
     * 按绑定车辆ID查询月卡
     * @param vehicleId 绑定车辆ID
     * @return 符合条件的月卡列表
     */
    @Override
    public List<MonthlyCard> getMonthlyCardsByVehicleId(String vehicleId) {
        QueryWrapper<MonthlyCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_id", vehicleId);
        return baseMapper.selectList(queryWrapper);
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
}