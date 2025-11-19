package com.tree.plms.service.impl;

import com.tree.plms.model.entity.AccessEvent;
import com.tree.plms.mapper.AccessEventMapper;
import com.tree.plms.service.AccessEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录车辆过闸行为 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class AccessEventServiceImpl extends ServiceImpl<AccessEventMapper, AccessEvent> implements AccessEventService {

}
