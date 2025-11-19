package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Vehicle;
import com.tree.plms.mapper.VehicleMapper;
import com.tree.plms.service.VehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
