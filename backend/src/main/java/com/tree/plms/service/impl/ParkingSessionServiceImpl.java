package com.tree.plms.service.impl;

import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.ParkingSession;
import com.tree.plms.mapper.ParkingSessionMapper;
import com.tree.plms.model.vo.EntryResultVO;
import com.tree.plms.model.vo.ExitResultVO;
import com.tree.plms.service.ParkingSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录完整停车周期数据 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class ParkingSessionServiceImpl extends ServiceImpl<ParkingSessionMapper, ParkingSession> implements ParkingSessionService {

    @Override
    public Result<EntryResultVO> vehicleEntry(String licensePlate, String gateId) {
        return null;
    }

    @Override
    public Result<ExitResultVO> vehicleExit(String licensePlate, String gateId, String payMethod) {
        return null;
    }
}
