package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Vehicle;
import com.tree.plms.mapper.VehicleMapper;
import com.tree.plms.service.VehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * <p>
 * 存储车辆基本信息 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    public Vehicle getVehicleById(String vehicleId) {
        return baseMapper.selectById(vehicleId);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleId() == null) {
            // 查询当前最大的车辆ID
            QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("vehicle_id");
            queryWrapper.last("LIMIT 1");
            Vehicle lastVehicle = baseMapper.selectOne(queryWrapper);

            String newVehicleId;
            if (lastVehicle != null) {
                // 提取数字部分并加1
                String lastId = lastVehicle.getVehicleId();
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newVehicleId = String.format("v%05d", num);
            } else {
                // 第一辆车
                newVehicleId = "v00001";
            }
            vehicle.setVehicleId(newVehicleId);
        }

        return save(vehicle);
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        return updateById(vehicle);
    }

    @Override
    public boolean deleteVehicle(String vehicleId) {
        return removeById(vehicleId);
    }

    @Override
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("license_plate", licensePlate);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Vehicle> getVehiclesByVehicleType(String vehicleType) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_type", vehicleType);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Vehicle> getVehiclesByIsOwnerCar(String isOwnerCar) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_owner_car", isOwnerCar);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Vehicle> getVehiclesByOwnerId(String ownerId) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Vehicle> searchVehiclesByLicensePlate(String licensePlate) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("license_plate", licensePlate);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取车型名称
     * @param vehicleType 车型代码
     * @return 车型名称
     */
    private String getVehicleTypeName(String vehicleType) {
        switch (vehicleType) {
            case "01":
                return "小型车";
            case "02":
                return "大型车";
            default:
                return "其他车型";
        }
    }

    /**
     * 获取是否业主车描述
     * @param isOwnerCar 是否业主车代码
     * @return 是否业主车描述
     */
    private String getIsOwnerCarName(String isOwnerCar) {
        switch (isOwnerCar) {
            case "01":
                return "是";
            case "02":
                return "否";
            default:
                return "未知";
        }
    }
}