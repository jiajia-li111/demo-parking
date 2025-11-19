package com.tree.plms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.model.entity.Floor;
import com.tree.plms.mapper.FloorMapper;
import com.tree.plms.service.FloorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 存储停车场楼层信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {

    /**
     * 根据楼层ID查询楼层
     * @param floorId 楼层唯一标识（如"b1"）
     * @return 楼层对象
     */
    @Override
    public Floor getFloorById(String floorId) {
        return baseMapper.selectById(floorId);
    }

    /**
     * 获取所有楼层列表
     * @return 楼层列表
     */
    @Override
    public List<Floor> getAllFloors() {
        return baseMapper.selectList(null);
    }

    /**
     * 新增楼层
     * @param floor 楼层对象
     * @return 是否新增成功
     */
    @Override
    public boolean addFloor(Floor floor) {
        return this.save(floor);
    }

    /**
     * 更新楼层
     * @param floor 楼层对象
     * @return 是否更新成功
     */
    @Override
    public boolean updateFloor(Floor floor) {
        return this.updateById(floor);
    }

    /**
     * 删除楼层
     * @param floorId 楼层唯一标识（如"b1"）
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFloor(String floorId) {
        return this.removeById(floorId);
    }

    /**
     * 按楼层名称查询楼层
     * @param floorName 楼层名称（如"地下一层"）
     * @return 符合条件的楼层列表
     */
    @Override
    public List<Floor> getFloorsByFloorName(String floorName) {
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("floor_name", floorName);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按总车位数范围查询楼层
     * @param minSpaces 最小车位数
     * @param maxSpaces 最大车位数
     * @return 符合条件的楼层列表
     */
    @Override
    public List<Floor> getFloorsByTotalSpacesRange(Integer minSpaces, Integer maxSpaces) {
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        if (minSpaces != null) {
            queryWrapper.ge("total_spaces", minSpaces);
        }
        if (maxSpaces != null) {
            queryWrapper.le("total_spaces", maxSpaces);
        }
        return baseMapper.selectList(queryWrapper);
    }
}