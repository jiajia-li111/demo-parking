package com.tree.plms.mapper;

import com.tree.plms.model.entity.AccessEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 记录车辆过闸行为 Mapper 接口
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Mapper
public interface AccessEventMapper extends BaseMapper<AccessEvent> {

}
