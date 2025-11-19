package com.tree.plms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存储停车场楼层信息
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Getter
@Setter
@TableName("t_floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼层唯一标识（如"b1"）
     */
    @TableId("floor_id")
    private String floorId;

    /**
     * 楼层名称（如"地下一层"）
     */
    @TableField("floor_name")
    private String floorName;

    /**
     * 总车位数（每层固定200个车位）
     */
    @TableField("total_spaces")
    private Integer totalSpaces;
}
