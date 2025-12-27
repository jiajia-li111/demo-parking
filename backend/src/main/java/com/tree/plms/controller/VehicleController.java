package com.tree.plms.controller;

import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.Vehicle;
import com.tree.plms.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 存储车辆基本信息 前端控制器
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@RestController
@RequestMapping("/vehicle")
@Tag(name = "车辆管理")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * 获取所有车辆信息
     */
    @GetMapping
    @Operation(summary = "获取所有车辆信息", description = "返回所有车辆信息列表")
    public Result<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return Result.success(vehicles);
    }

    /**
     * 根据车辆ID获取车辆信息
     */
    @GetMapping("/{vehicleId}")
    @Operation(summary = "根据ID获取车辆信息", description = "根据车辆唯一标识获取车辆详细信息")
    public Result<Vehicle> getVehicleById(@PathVariable String vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle != null) {
            return Result.success(vehicle);
        } else {
            return Result.fail(ResultCodeEnum.VEHICLE_NOT_FOUND);
        }
    }

    /**
     * 新增车辆信息
     */
    @PostMapping
    @Operation(summary = "新增车辆信息", description = "添加新的车辆信息")
    public Result<String> addVehicle(@RequestBody Vehicle vehicle) {
        boolean success = vehicleService.addVehicle(vehicle);
        if (success) {
            return Result.success("车辆添加成功");
        } else {
            return Result.fail(ResultCodeEnum.VEHICLE_ADD_FAILED);
        }
    }

    /**
     * 更新车辆信息
     */
    @PutMapping
    @Operation(summary = "更新车辆信息", description = "更新已存在的车辆信息")
    public Result<String> updateVehicle(@RequestBody Vehicle vehicle) {
        boolean success = vehicleService.updateVehicle(vehicle);
        if (success) {
            return Result.success("车辆更新成功");
        } else {
            return Result.fail(ResultCodeEnum.VEHICLE_UPDATE_FAILED);
        }
    }

    /**
     * 删除车辆信息
     */
    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "删除车辆信息", description = "根据车辆唯一标识删除车辆信息")
    public Result<String> deleteVehicle(@PathVariable String vehicleId) {
        boolean success = vehicleService.deleteVehicle(vehicleId);
        if (success) {
            return Result.success("车辆删除成功");
        } else {
            return Result.fail(ResultCodeEnum.VEHICLE_DELETE_FAILED);
        }
    }
}
