package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.mapper.AccessEventMapper;
import com.tree.plms.model.entity.AccessEvent;
import com.tree.plms.service.AccessEventService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 记录车辆过闸行为 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class AccessEventServiceImpl extends ServiceImpl<AccessEventMapper, AccessEvent> implements AccessEventService {

    /**
     * 根据ID获取过闸事件信息
     * @param eventId 事件ID
     * @return 过闸事件信息
     */
    @Override
    public AccessEvent getAccessEventById(String eventId) {
        return baseMapper.selectById(eventId);
    }

    /**
     * 获取所有过闸事件列表
     * @return 过闸事件列表
     */
    @Override
    public List<AccessEvent> getAllAccessEvents() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增过闸事件
     * @param accessEvent 过闸事件信息
     * @return 是否成功
     */
    @Override
    public boolean addAccessEvent(AccessEvent accessEvent) {
        return save(accessEvent);
    }

    /**
     * 更新过闸事件信息
     * @param accessEvent 过闸事件信息
     * @return 是否成功
     */
    @Override
    public boolean updateAccessEvent(AccessEvent accessEvent) {
        return updateById(accessEvent);
    }

    /**
     * 删除过闸事件
     * @param eventId 事件ID
     * @return 是否成功
     */
    @Override
    public boolean deleteAccessEvent(String eventId) {
        return removeById(eventId);
    }

    /**
     * 根据事件类型查询过闸事件列表
     * @param eventType 事件类型
     * @return 过闸事件列表
     */
    @Override
    public List<AccessEvent> getAccessEventsByType(String eventType) {
        QueryWrapper<AccessEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_type", eventType);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据处理状态查询过闸事件列表
     * @param handleStatus 处理状态
     * @return 过闸事件列表
     */
    @Override
    public List<AccessEvent> getAccessEventsByStatus(String handleStatus) {
        QueryWrapper<AccessEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("handle_status", handleStatus);
        return baseMapper.selectList(queryWrapper);
    }
}