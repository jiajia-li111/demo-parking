package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.mapper.GateMapper;
import com.tree.plms.model.entity.Gate;
import com.tree.plms.service.GateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 存储出入口通道信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class GateServiceImpl extends ServiceImpl<GateMapper, Gate> implements GateService {

    /**
     * 根据ID获取通道信息
     * @param gateId 通道ID
     * @return 通道信息
     */
    @Override
    public Gate getGateById(String gateId) {
        return baseMapper.selectById(gateId);
    }

    /**
     * 获取所有通道列表
     * @return 通道列表
     */
    @Override
    public List<Gate> getAllGates() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增通道
     * @param gate 通道信息
     * @return 是否成功
     */
    @Override
    public boolean addGate(Gate gate) {
        return save(gate);
    }

    /**
     * 更新通道信息
     * @param gate 通道信息
     * @return 是否成功
     */
    @Override
    public boolean updateGate(Gate gate) {
        return updateById(gate);
    }

    /**
     * 删除通道
     * @param gateId 通道ID
     * @return 是否成功
     */
    @Override
    public boolean deleteGate(String gateId) {
        return removeById(gateId);
    }

    /**
     * 根据通道类型查询通道列表
     * @param gateType 通道类型（01=入口，02=出口）
     * @return 通道列表
     */
    @Override
    public List<Gate> getGatesByType(String gateType) {
        QueryWrapper<Gate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gate_type", gateType);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据状态查询通道列表
     * @param status 状态（01=正常，02=故障）
     * @return 通道列表
     */
    @Override
    public List<Gate> getGatesByStatus(String status) {
        QueryWrapper<Gate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }
}