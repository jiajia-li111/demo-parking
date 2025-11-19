package com.tree.plms.service;

import com.tree.plms.model.entity.Owner;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 存储小区业主信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface OwnerService extends IService<Owner> {

    /**
     * 根据业主ID查询业主信息
     * @param ownerId 业主唯一标识
     * @return 业主信息
     */
    Owner getOwnerById(String ownerId);

    /**
     * 获取所有业主信息列表
     * @return 业主信息列表
     */
    List<Owner> getAllOwners();

    /**
     * 新增业主信息
     * @param owner 业主信息
     * @return 是否添加成功
     */
    boolean addOwner(Owner owner);

    /**
     * 更新业主信息
     * @param owner 业主信息
     * @return 是否更新成功
     */
    boolean updateOwner(Owner owner);

    /**
     * 根据业主ID删除业主信息
     * @param ownerId 业主唯一标识
     * @return 是否删除成功
     */
    boolean deleteOwner(String ownerId);

    /**
     * 根据业主姓名查询业主信息
     * @param name 业主姓名
     * @return 业主信息列表
     */
    List<Owner> getOwnersByName(String name);

    /**
     * 根据房号查询业主信息
     * @param roomNo 房号
     * @return 业主信息
     */
    Owner getOwnerByRoomNo(String roomNo);

    /**
     * 根据联系电话查询业主信息
     * @param phone 联系电话
     * @return 业主信息
     */
    Owner getOwnerByPhone(String phone);
}