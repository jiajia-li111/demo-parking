package com.tree.plms.service;

import com.tree.plms.model.entity.AccessEvent;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * 根据ID获取过闸事件信息
     * @param eventId 事件ID
     * @return 过闸事件信息
     */
    AccessEvent getAccessEventById(String eventId);

    /**
     * 获取所有过闸事件列表
     * @return 过闸事件列表
     */
    List<AccessEvent> getAllAccessEvents();

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