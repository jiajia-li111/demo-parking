package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储出入口通道信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_gate")
public class Gate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通道唯一标识（如"in1"）
     */
    @TableId("gate_id")
    private String gateId;

    /**
     * 通道类型（01=入口，02=出口）
     */
    @TableField("gate_type")
    private String gateType;

    /**
     * 所属出入口（01=1号出入口，02=2号出入口）
     */
    @TableField("entrance_id")
    private String entranceId;

    /**
     * 状态（01=正常，02=故障）
     */
    @TableField("status")
    private String status;
}
