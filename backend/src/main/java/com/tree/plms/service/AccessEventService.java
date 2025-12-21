package com.tree.plms.service;

import com.tree.plms.model.entity.AccessEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.vo.AccessEventVO;

import java.util.List;

/**
 * <p>
 * 记录车辆过闸行为 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface AccessEventService extends IService<AccessEvent> {

    /**
     * 获取所有过闸事件列表（包含车牌号）
     * @return 过闸事件VO列表
     */
    List<AccessEventVO> getAllAccessEventsWithLicensePlate();

    /**
     * 根据事件ID获取过闸事件详情（包含车牌号）
     * @param eventId 事件ID
     * @return 过闸事件VO
     */
    AccessEventVO getAccessEventWithLicensePlateById(String eventId);

    /**
     * 新增过闸事件
     * @param accessEvent 过闸事件信息
     * @return 是否成功
     */
    boolean addAccessEvent(AccessEvent accessEvent);

    /**
     * 更新过闸事件信息
     * @param accessEvent 过闸事件信息
     * @return 是否成功
     */
    boolean updateAccessEvent(AccessEvent accessEvent);

    /**
     * 删除过闸事件
     * @param eventId 事件ID
     * @return 是否成功
     */
    boolean deleteAccessEvent(String eventId);

    /**
     * 根据事件类型查询过闸事件列表
     * @param eventType 事件类型
     * @return 过闸事件列表
     */
    List<AccessEvent> getAccessEventsByType(String eventType);

    /**
     * 根据处理状态查询过闸事件列表
     * @param handleStatus 处理状态
     * @return 过闸事件列表
     */
    List<AccessEvent> getAccessEventsByStatus(String handleStatus);
}