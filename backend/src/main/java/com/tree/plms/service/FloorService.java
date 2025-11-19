package com.tree.plms.service;

import com.tree.plms.model.entity.Floor;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储停车场楼层信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface FloorService extends IService<Floor> {

    /**
     * 根据楼层ID查询楼层
     * @param floorId 楼层唯一标识（如"b1"）
     * @return 楼层对象
     */
    Floor getFloorById(String floorId);

    /**
     * 获取所有楼层列表
     * @return 楼层列表
     */
    List<Floor> getAllFloors();

    /**
     * 新增楼层
     * @param floor 楼层对象
     * @return 是否新增成功
     */
    boolean addFloor(Floor floor);

    /**
     * 更新楼层
     * @param floor 楼层对象
     * @return 是否更新成功
     */
    boolean updateFloor(Floor floor);

    /**
     * 删除楼层
     * @param floorId 楼层唯一标识（如"b1"）
     * @return 是否删除成功
     */
    boolean deleteFloor(String floorId);

    /**
     * 按楼层名称查询楼层
     * @param floorName 楼层名称（如"地下一层"）
     * @return 符合条件的楼层列表
     */
    List<Floor> getFloorsByFloorName(String floorName);

    /**
     * 按总车位数范围查询楼层
     * @param minSpaces 最小车位数
     * @param maxSpaces 最大车位数
     * @return 符合条件的楼层列表
     */
    List<Floor> getFloorsByTotalSpacesRange(Integer minSpaces, Integer maxSpaces);
}