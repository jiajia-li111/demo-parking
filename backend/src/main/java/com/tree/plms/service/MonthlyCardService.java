package com.tree.plms.service;

import com.tree.plms.model.dto.response.CardExpiryAlertDTO;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.MonthlyCard;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 存储业主车月卡信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface MonthlyCardService extends IService<MonthlyCard> {

    /**
     * 根据月卡ID查询月卡
     * @param cardId 月卡唯一标识（如"c2024001"）
     * @return 月卡对象
     */
    MonthlyCard getMonthlyCardById(String cardId);

    /**
     * 获取所有月卡列表
     * @return 月卡列表
     */
    List<MonthlyCard> getAllMonthlyCards();

    /**
     * 新增月卡
     * @param monthlyCard 月卡对象
     * @return 是否新增成功
     */
    boolean addMonthlyCard(MonthlyCard monthlyCard);

    /**
     * 更新月卡
     * @param monthlyCard 月卡对象
     * @return 是否更新成功
     */
    boolean updateMonthlyCard(MonthlyCard monthlyCard);

    /**
     * 删除月卡
     * @param cardId 月卡唯一标识（如"c2024001"）
     * @return 是否删除成功
     */
    boolean deleteMonthlyCard(String cardId);

    /**
     * 按月卡状态查询月卡
     * @param status 状态（01=启用，02=挂失，03=过期）
     * @return 符合条件的月卡列表
     */
    List<MonthlyCard> getMonthlyCardsByStatus(String status);

    /**
     * 按绑定车辆ID查询月卡
     * @param vehicleId 绑定车辆ID
     * @return 符合条件的月卡列表
     */
    List<MonthlyCard> getMonthlyCardsByVehicleId(String vehicleId);

    /**
     * 按时间范围查询月卡
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的月卡列表
     */
    List<MonthlyCard> getMonthlyCardsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // 在接口末尾添加以下方法

    /**
     * 获取即将到期的月卡列表
     * @param days 提前天数（如7表示获取7天内到期的月卡）
     * @return 到期提醒列表
     */
    Result<List<CardExpiryAlertDTO>> getExpiringCards(Integer days);
}