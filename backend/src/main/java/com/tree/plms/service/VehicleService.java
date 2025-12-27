package com.tree.plms.service;

import com.tree.plms.model.entity.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储车辆基本信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface VehicleService extends IService<Vehicle> {

    /**
     * 根据车辆ID查询车辆信息
     * @param vehicleId 车辆唯一标识
     * @return 车辆信息
     */
    Vehicle getVehicleById(String vehicleId);

    /**
     * 获取所有车辆信息
     * @return 车辆信息列表
     */
    List<Vehicle> getAllVehicles();

    /**
     * 新增车辆信息
     * @param vehicle 车辆信息
     * @return 是否新增成功
     */
    boolean addVehicle(Vehicle vehicle);

    /**
     * 更新车辆信息
     * @param vehicle 车辆信息
     * @return 是否更新成功
     */
    boolean updateVehicle(Vehicle vehicle);

    /**
     * 删除车辆信息
     * @param vehicleId 车辆唯一标识
     * @return 是否删除成功
     */
    boolean deleteVehicle(String vehicleId);

    /**
     * 根据车牌号查询车辆信息
     * @param licensePlate 车牌号
     * @return 车辆信息
     */
    Vehicle getVehicleByLicensePlate(String licensePlate);

    /**
     * 根据车型查询车辆信息
     * @param vehicleType 车型（01=小型车，02=大型车）
     * @return 车辆信息列表
     */
    List<Vehicle> getVehiclesByVehicleType(String vehicleType);

    /**
     * 根据是否业主车查询车辆信息
     * @param isOwnerCar 是否业主车（01=是，02=否）
     * @return 车辆信息列表
     */
    List<Vehicle> getVehiclesByIsOwnerCar(String isOwnerCar);

    /**
     * 根据业主ID查询关联的车辆信息
     * @param ownerId 关联业主ID
     * @return 车辆信息列表
     */
    List<Vehicle> getVehiclesByOwnerId(String ownerId);

    /**
     * 根据车牌号模糊查询车辆信息
     * @param licensePlate 车牌号关键字
     * @return 车辆信息列表
     */
    List<Vehicle> searchVehiclesByLicensePlate(String licensePlate);

    void deleteByOwnerId(String ownerId);
}