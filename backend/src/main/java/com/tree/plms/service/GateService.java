package com.tree.plms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.entity.Gate;

import java.util.List;

/**
 * <p>
 * 存储出入口通道信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface GateService extends IService<Gate> {

    /**
     * 根据ID获取通道信息
     * @param gateId 通道ID
     * @return 通道信息
     */
    Gate getGateById(String gateId);

    /**
     * 获取所有通道列表
     * @return 通道列表
     */
    List<Gate> getAllGates();

    /**
     * 新增通道
     * @param gate 通道信息
     * @return 是否成功
     */
    boolean addGate(Gate gate);

    /**
     * 更新通道信息
     * @param gate 通道信息
     * @return 是否成功
     */
    boolean updateGate(Gate gate);

    /**
     * 删除通道
     * @param gateId 通道ID
     * @return 是否成功
     */
    boolean deleteGate(String gateId);

    /**
     * 根据通道类型查询通道列表
     * @param gateType 通道类型（01=入口，02=出口）
     * @return 通道列表
     */
    List<Gate> getGatesByType(String gateType);

    /**
     * 根据状态查询通道列表
     * @param status 状态（01=正常，02=故障）
     * @return 通道列表
     */
    List<Gate> getGatesByStatus(String status);
}