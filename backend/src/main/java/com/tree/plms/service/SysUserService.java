package com.tree.plms.service;

import com.tree.plms.model.dto.request.LoginReq;
import com.tree.plms.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.plms.model.vo.LoginVO;

/**
 * <p>
 * 存储操作人员信息 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface SysUserService extends IService<SysUser> {

    LoginVO login(LoginReq req);
}
