package com.tree.plms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.plms.enums.ResultCodeEnum;
import com.tree.plms.model.dto.response.Result;
import com.tree.plms.model.entity.Owner;
import com.tree.plms.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SuohaChan
 * @data 2025/12/23
 */
@RestController
@RequestMapping("/owner")
@Tag(name = "业主管理", description = "业主信息的增删改查操作")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * 获取所有业主信息
     */
    @GetMapping
    @Operation(summary = "获取所有业主信息", description = "返回所有业主信息列表")
    public Result<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        return Result.success(owners);
    }

    /**
     * 根据业主ID获取业主信息
     */
    @GetMapping("/{ownerId}")
    @Operation(summary = "根据ID获取业主信息", description = "根据业主唯一标识获取业主详细信息")
    public Result getOwnerById(@PathVariable String ownerId) {
        Owner owner = ownerService.getOwnerById(ownerId);
        if (owner != null) {
            return Result.success(owner);
        } else {
            return Result.fail(ResultCodeEnum.OWNER_NOT_FOUND);
        }
    }

    /**
     * 新增业主信息
     */
    /**
     * 新增业主信息
     */
    @PostMapping
    @Operation(summary = "新增业主信息", description = "添加新的业主信息")
    public Result<String> addOwner(@RequestBody Owner owner) {
        // 1. 检查联系电话是否已存在
        QueryWrapper<Owner> phoneQuery = new QueryWrapper<>();
        phoneQuery.eq("phone", owner.getPhone());
        Owner existingOwner = ownerService.getBaseMapper().selectOne(phoneQuery);
        if (existingOwner != null) {
            return Result.fail(ResultCodeEnum.OWNER_PHONE_EXISTS);
        }

        // 2. 检查房号是否已存在
        QueryWrapper<Owner> roomQuery = new QueryWrapper<>();
        roomQuery.eq("room_no", owner.getRoomNo());
        existingOwner = ownerService.getBaseMapper().selectOne(roomQuery);
        if (existingOwner != null) {
            return Result.fail(ResultCodeEnum.ROOM_NO_EXISTS);
        }

        if (owner.getOwnerId() == null) {
            // 修复ID生成逻辑以避免重复
            String newOwnerId = generateUniqueOwnerId();
            owner.setOwnerId(newOwnerId);
        }

        boolean success = ownerService.addOwner(owner);
        if (success) {
            return Result.success("业主添加成功");
        } else {
            return Result.fail(ResultCodeEnum.OWNER_ADD_FAILED);
        }
    }




    /**
     * 更新业主信息
     */
    @PutMapping
    @Operation(summary = "更新业主信息", description = "更新已存在的业主信息")
    public Result<String> updateOwner(@RequestBody Owner owner) {
        boolean success = ownerService.updateOwner(owner);
        if (success) {
            return Result.success("业主更新成功");
        } else {
            return Result.fail(ResultCodeEnum.OWNER_UPDATE_FAILED);
        }
    }

    /**
     * 删除业主信息
     */
    @DeleteMapping("/{ownerId}")
    @Operation(summary = "删除业主信息", description = "根据业主唯一标识删除业主信息")
    public Result<String> deleteOwner(@PathVariable String ownerId) {
        boolean success = ownerService.deleteOwner(ownerId);
        if (success) {
            return Result.success("业主删除成功");
        } else {
            return Result.fail(ResultCodeEnum.valueOf("业主删除失败"));
        }
    }

    /**
     * 根据业主姓名查询业主信息
     */
    @GetMapping("/by-name/{name}")
    @Operation(summary = "根据姓名查询业主", description = "根据业主姓名获取业主信息列表")
    public Result<List<Owner>> getOwnersByName(@PathVariable String name) {
        List<Owner> owners = ownerService.getOwnersByName(name);
        return Result.success(owners);
    }

    /**
     * 根据房号查询业主信息
     */
    @GetMapping("/by-room-no/{roomNo}")
    @Operation(summary = "根据房号查询业主", description = "根据房号获取业主信息")
    public Result<Owner> getOwnerByRoomNo(@PathVariable String roomNo) {
        Owner owner = ownerService.getOwnerByRoomNo(roomNo);
        if (owner != null) {
            return Result.success(owner);
        } else {
            return Result.fail(ResultCodeEnum.OWNER_NOT_FOUND);
        }
    }

    /**
     * 根据联系电话查询业主信息
     */
    @GetMapping("/by-phone/{phone}")
    @Operation(summary = "根据联系电话查询业主", description = "根据联系电话获取业主信息")
    public Result<Owner> getOwnerByPhone(@PathVariable String phone) {
        Owner owner = ownerService.getOwnerByPhone(phone);
        if (owner != null) {
            return Result.success(owner);
        } else {
            return Result.fail(ResultCodeEnum.OWNER_NOT_FOUND);
        }
    }

    private String generateUniqueOwnerId() {
        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("owner_id");
        queryWrapper.last("LIMIT 1");
        Owner lastOwner = ownerService.getBaseMapper().selectOne(queryWrapper);

        if (lastOwner != null) {
            String lastId = lastOwner.getOwnerId();
            // 确保ID格式正确
            if (lastId.startsWith("o") && lastId.length() > 1) {
                try {
                    int num = Integer.parseInt(lastId.substring(1)) + 1;
                    // 检查新ID是否已存在，如果存在则继续递增
                    String newOwnerId;
                    do {
                        newOwnerId = String.format("o%05d", num);
                        Owner existing = ownerService.getBaseMapper().selectOne(
                                new QueryWrapper<Owner>().eq("owner_id", newOwnerId)
                        );
                        if (existing == null) {
                            return newOwnerId;
                        }
                        num++;
                    } while (true);
                } catch (NumberFormatException e) {
                    // 如果解析失败，使用默认ID
                    return "o00001";
                }
            }
        }
        return "o00001";
    }
}