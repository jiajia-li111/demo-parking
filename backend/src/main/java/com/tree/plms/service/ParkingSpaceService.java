package com.tree.plms.service;

import com.tree.plms.model.entity.ParkingSpace;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储单个车位信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface ParkingSpaceService extends IService<ParkingSpace> {

    /**
     * 根据车位ID查询车位信息
     * @param spaceId 车位唯一标识
     * @return 车位信息
     */
    ParkingSpace getParkingSpaceById(String spaceId);

    /**
     * 获取所有车位信息列表
     * @return 车位信息列表
     */
    List<ParkingSpace> getAllParkingSpaces();

    /**
     * 新增车位信息
     * @param parkingSpace 车位信息
     * @return 是否添加成功
     */
    boolean addParkingSpace(ParkingSpace parkingSpace);

    /**
     * 更新车位信息
     * @param parkingSpace 车位信息
     * @return 是否更新成功
     */
    boolean updateParkingSpace(ParkingSpace parkingSpace);

    /**
     * 根据车位ID删除车位信息
     * @param spaceId 车位唯一标识
     * @return 是否删除成功
     */
    boolean deleteParkingSpace(String spaceId);

    /**
     * 根据楼层ID查询车位信息
     * @param floorId 所属楼层ID
     * @return 车位信息列表
     */
    List<ParkingSpace> getParkingSpacesByFloorId(String floorId);

    /**
     * 根据车位状态查询车位信息
     * @param status 状态（01=空闲，02=占用，03=故障）
     * @return 车位信息列表
     */
    List<ParkingSpace> getParkingSpacesByStatus(String status);

    /**
     * 查询固定车位列表
     * @param isFixed 是否固定车位（01=是，02=否）
     * @return 车位信息列表
     */
    List<ParkingSpace> getParkingSpacesByIsFixed(String isFixed);

    /**
     * 根据业主ID查询关联的固定车位
     * @param ownerId 关联业主ID
     * @return 车位信息
     */
    ParkingSpace getParkingSpaceByOwnerId(String ownerId);

    /**
     * 查询空闲的非固定车位
     * @return 空闲非固定车位列表
     */
    List<ParkingSpace> getAvailableNonFixedSpaces();
}