package com.tree.plms.service.impl;

import com.tree.plms.model.entity.ParkingSpace;
import com.tree.plms.mapper.ParkingSpaceMapper;
import com.tree.plms.service.ParkingSpaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * <p>
 * 存储单个车位信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace> implements ParkingSpaceService {

    /**
     * 根据车位ID查询车位信息
     * @param spaceId 车位唯一标识
     * @return 车位信息
     */
    @Override
    public ParkingSpace getParkingSpaceById(String spaceId) {
        return baseMapper.selectById(spaceId);
    }

    /**
     * 获取所有车位信息列表
     * @return 车位信息列表
     */
    @Override
    public List<ParkingSpace> getAllParkingSpaces() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增车位信息
     * @param parkingSpace 车位信息
     * @return 是否添加成功
     */
    @Override
    public boolean addParkingSpace(ParkingSpace parkingSpace) {
        return save(parkingSpace);
    }

    /**
     * 更新车位信息
     * @param parkingSpace 车位信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateParkingSpace(ParkingSpace parkingSpace) {
        return updateById(parkingSpace);
    }

    /**
     * 根据车位ID删除车位信息
     * @param spaceId 车位唯一标识
     * @return 是否删除成功
     */
    @Override
    public boolean deleteParkingSpace(String spaceId) {
        return removeById(spaceId);
    }

    /**
     * 根据楼层ID查询车位信息
     * @param floorId 所属楼层ID
     * @return 车位信息列表
     */
    @Override
    public List<ParkingSpace> getParkingSpacesByFloorId(String floorId) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("floor_id", floorId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据车位状态查询车位信息
     * @param status 状态（01=空闲，02=占用，03=故障）
     * @return 车位信息列表
     */
    @Override
    public List<ParkingSpace> getParkingSpacesByStatus(String status) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询固定车位列表
     * @param isFixed 是否固定车位（01=是，02=否）
     * @return 车位信息列表
     */
    @Override
    public List<ParkingSpace> getParkingSpacesByIsFixed(String isFixed) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_fixed", isFixed);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据业主ID查询关联的固定车位
     * @param ownerId 关联业主ID
     * @return 车位信息
     */
    @Override
    public ParkingSpace getParkingSpaceByOwnerId(String ownerId) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 查询空闲的非固定车位
     * @return 空闲非固定车位列表
     */
    @Override
    public List<ParkingSpace> getAvailableNonFixedSpaces() {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "01") // 状态为空闲
                   .eq("is_fixed", "02"); // 非固定车位
        return baseMapper.selectList(queryWrapper);
    }
}