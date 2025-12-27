package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Owner;
import com.tree.plms.mapper.OwnerMapper;
import com.tree.plms.service.OwnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.plms.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * <p>
 * 存储小区业主信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService {

    /**
     * 根据业主ID查询业主信息
     * @param ownerId 业主唯一标识
     * @return 业主信息
     */
    @Override
    public Owner getOwnerById(String ownerId) {
        return baseMapper.selectById(ownerId);
    }

    @Autowired
    private VehicleService vehicleService;

    /**
     * 获取所有业主信息列表
     * @return 业主信息列表
     */
    @Override
    public List<Owner> getAllOwners() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增业主信息
     * @param owner 业主信息
     * @return 是否添加成功
     */
    @Override
    public boolean addOwner(Owner owner) {
        return save(owner);
    }

    /**
     * 更新业主信息
     * @param owner 业主信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateOwner(Owner owner) {
        return updateById(owner);
    }

    /**
     * 根据业主ID删除业主信息
     * @param ownerId 业主唯一标识
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOwner(String ownerId) {
        // 删除车主前先删除其所有车辆
        vehicleService.deleteByOwnerId(ownerId);
        return removeById(ownerId);
    }

    /**
     * 根据业主姓名查询业主信息
     * @param name 业主姓名
     * @return 业主信息列表
     */
    @Override
    public List<Owner> getOwnersByName(String name) {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据房号查询业主信息
     * @param roomNo 房号
     * @return 业主信息
     */
    @Override
    public Owner getOwnerByRoomNo(String roomNo) {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_no", roomNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据联系电话查询业主信息
     * @param phone 联系电话
     * @return 业主信息
     */
    @Override
    public Owner getOwnerByPhone(String phone) {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return baseMapper.selectOne(queryWrapper);
    }
}