package com.tree.plms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.plms.model.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.plms.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 存储操作人员信息 Mapper 接口
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 按用户名查询（登录用）
    SysUser selectByUsername(@Param("username") String username);

    // 分页查询用户（关联角色）
    IPage<SysUserVO> selectUserPage(
            Page<SysUserVO> page,
            @Param("username") String username,
            @Param("roleId") String roleId,
            @Param("status") String status
    );
}
