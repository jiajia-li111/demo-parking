-- 停车场管理系统数据库脚本
-- 适用数据库：MySQL 8.0+
-- 生成时间：2025-11-19
-- 作者：优化版（基于原需求修正）

CREATE DATABASE IF NOT EXISTS `PLMS`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE `PLMS`;

-- 先删除已有表（避免重复创建冲突）
DROP TABLE IF EXISTS `t_payment`;
DROP TABLE IF EXISTS `t_access_event`;
DROP TABLE IF EXISTS `t_parking_session`;
DROP TABLE IF EXISTS `t_monthly_card`;
DROP TABLE IF EXISTS `t_vehicle`;
DROP TABLE IF EXISTS `t_sys_user`;
DROP TABLE IF EXISTS `t_role`;
DROP TABLE IF EXISTS `t_fee_rule`;
DROP TABLE IF EXISTS `t_parking_space`;
DROP TABLE IF EXISTS `t_gate`;
DROP TABLE IF EXISTS `t_floor`;
DROP TABLE IF EXISTS `t_owner`;

-- 删除已有视图
DROP VIEW IF EXISTS `v_parking_space_usage`;
DROP VIEW IF EXISTS `v_today_parking_stats`;
DROP VIEW IF EXISTS `v_card_expiry_alert`;


-- 1. 楼层表（t_floor）
CREATE TABLE `t_floor` (
                           `floor_id` VARCHAR(2) NOT NULL COMMENT '楼层唯一标识（如"b1"）',
                           `floor_name` VARCHAR(20) NOT NULL COMMENT '楼层名称（如"地下一层"）',
                           `total_spaces` INT NOT NULL COMMENT '总车位数（每层固定200个车位）',
                           PRIMARY KEY (`floor_id`),
                           UNIQUE KEY `uk_floor_name` (`floor_name`), -- 确保楼层名称唯一
                           CONSTRAINT `ck_floor_total` CHECK (`total_spaces` = 200) -- 强制每层200个车位
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储停车场楼层信息';


-- 2. 车位表（t_parking_space）
CREATE TABLE `t_parking_space` (
                                   `space_id` VARCHAR(8) NOT NULL COMMENT '车位唯一标识（如"b1_001"）',
                                   `floor_id` VARCHAR(2) NOT NULL COMMENT '所属楼层ID',
                                   `space_no` VARCHAR(3) NOT NULL COMMENT '车位编号（楼层内编号，如"001"）',
                                   `status` VARCHAR(2) NOT NULL COMMENT '状态（01=空闲，02=占用，03=故障）',
                                   `is_fixed` VARCHAR(2) NOT NULL COMMENT '是否固定车位（01=是，02=否）',
                                   `owner_id` VARCHAR(6) DEFAULT NULL COMMENT '关联业主ID（仅固定车位有值）',
                                   PRIMARY KEY (`space_id`),
                                   UNIQUE KEY `uk_floor_space_no` (`floor_id`, `space_no`), -- 楼层内车位编号唯一
                                   KEY `idx_space_floor` (`floor_id`),
                                   KEY `idx_space_owner` (`owner_id`),
                                   CONSTRAINT `ck_space_status` CHECK (`status` IN ('01', '02', '03')),
                                   CONSTRAINT `ck_space_is_fixed` CHECK (`is_fixed` IN ('01', '02')),
                                   CONSTRAINT `fk_space_floor` FOREIGN KEY (`floor_id`) REFERENCES `t_floor` (`floor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储单个车位信息';


-- 3. 通道表（t_gate）
CREATE TABLE `t_gate` (
                          `gate_id` VARCHAR(4) NOT NULL COMMENT '通道唯一标识（如"in1"）',
                          `gate_type` VARCHAR(2) NOT NULL COMMENT '通道类型（01=入口，02=出口）',
                          `entrance_id` VARCHAR(2) NOT NULL COMMENT '所属出入口（01=1号出入口，02=2号出入口）',
                          `status` VARCHAR(2) NOT NULL COMMENT '状态（01=正常，02=故障）',
                          PRIMARY KEY (`gate_id`),
                          UNIQUE KEY `uk_gate_type_entrance` (`gate_type`, `entrance_id`), -- 同一出入口的类型唯一（一进一出）
                          CONSTRAINT `ck_gate_type` CHECK (`gate_type` IN ('01', '02')),
                          CONSTRAINT `ck_gate_status` CHECK (`status` IN ('01', '02'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储出入口通道信息';


-- 4. 业主表（t_owner）
CREATE TABLE `t_owner` (
                           `owner_id` VARCHAR(6) NOT NULL COMMENT '业主唯一标识（如"o202401"）',
                           `name` VARCHAR(20) NOT NULL COMMENT '业主姓名',
                           `room_no` VARCHAR(20) NOT NULL COMMENT '房号（如"3栋2单元501"）',
                           `phone` VARCHAR(15) NOT NULL COMMENT '联系电话（唯一）',
                           PRIMARY KEY (`owner_id`),
                           UNIQUE KEY `uk_owner_phone` (`phone`),
                           UNIQUE KEY `uk_owner_room` (`room_no`) -- 房号唯一
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储小区业主信息';


-- 5. 车辆表（t_vehicle）
CREATE TABLE `t_vehicle` (
                             `vehicle_id` VARCHAR(8) NOT NULL COMMENT '车辆唯一标识（如"v2024001"）',
                             `license_plate` VARCHAR(15) NOT NULL COMMENT '车牌号（唯一，如"粤A12345"）',
                             `vehicle_type` VARCHAR(2) NOT NULL COMMENT '车型（01=小型车，02=大型车）',
                             `is_owner_car` VARCHAR(2) NOT NULL COMMENT '是否业主车（01=是，02=否）',
                             `owner_id` VARCHAR(6) DEFAULT NULL COMMENT '关联业主ID（仅业主车有值）',
                             PRIMARY KEY (`vehicle_id`),
                             UNIQUE KEY `uk_vehicle_license` (`license_plate`),
                             KEY `idx_vehicle_owner` (`owner_id`),
                             CONSTRAINT `ck_vehicle_type` CHECK (`vehicle_type` IN ('01', '02')),
                             CONSTRAINT `ck_vehicle_is_owner` CHECK (`is_owner_car` IN ('01', '02')),
    -- 非业主车不允许关联业主ID
                             CONSTRAINT `ck_owner_car_relation` CHECK (
                                 (`is_owner_car` = '01' AND `owner_id` IS NOT NULL) OR
                                 (`is_owner_car` = '02' AND `owner_id` IS NULL)
                                 ),
                             CONSTRAINT `fk_vehicle_owner` FOREIGN KEY (`owner_id`) REFERENCES `t_owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储车辆基本信息';


-- 6. 角色表（t_role）
CREATE TABLE `t_role` (
                          `role_id` VARCHAR(4) NOT NULL COMMENT '角色标识（如"r001"）',
                          `role_name` VARCHAR(20) NOT NULL COMMENT '角色名称（如"超级管理员"）',
                          `permissions` VARCHAR(200) NOT NULL COMMENT '权限列表（逗号分隔，如"发卡,计费"）',
                          PRIMARY KEY (`role_id`),
                          UNIQUE KEY `uk_role_name` (`role_name`) -- 角色名称唯一
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储角色及权限信息';


-- 7. 系统用户表（t_sys_user）
CREATE TABLE `t_sys_user` (
                              `user_id` VARCHAR(6) NOT NULL COMMENT '用户唯一标识（如"u202401"）',
                              `username` VARCHAR(20) NOT NULL COMMENT '登录用户名（唯一）',
                              `password` VARCHAR(64) NOT NULL COMMENT '加密密码（BCrypt加密）',
                              `role_id` VARCHAR(4) NOT NULL COMMENT '关联角色ID',
                              `department` VARCHAR(20) NOT NULL COMMENT '所属部门（"管理中心"、"测试中心"、"用户中心"）',
                              `status` VARCHAR(2) NOT NULL COMMENT '状态（01=启用，02=禁用）',
                              PRIMARY KEY (`user_id`),
                              UNIQUE KEY `uk_sys_user_username` (`username`),
                              KEY `idx_user_role` (`role_id`),
                              CONSTRAINT `ck_user_department` CHECK (`department` IN ('管理中心', '测试中心', '用户中心')), -- 强制部门为管理中心
                              CONSTRAINT `ck_user_status` CHECK (`status` IN ('01', '02')),
                              CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储操作人员信息';


-- 8. 月卡表（t_monthly_card）
CREATE TABLE `t_monthly_card` (
                                  `card_id` VARCHAR(8) NOT NULL COMMENT '月卡唯一标识（如"c2024001"）',
                                  `vehicle_id` VARCHAR(8) NOT NULL COMMENT '绑定车辆ID',
                                  `issuer_id` VARCHAR(6) NOT NULL COMMENT '发卡人ID',
                                  `start_date` DATETIME NOT NULL COMMENT '生效时间（如"2024-01-01 00:00"）',
                                  `end_date` DATETIME NOT NULL COMMENT '到期时间（如"2024-12-31 23:59"）',
                                  `status` VARCHAR(2) NOT NULL COMMENT '状态（01=启用，02=挂失，03=过期）',
                                  PRIMARY KEY (`card_id`),
                                  UNIQUE KEY `uk_card_vehicle` (`vehicle_id`), -- 一车一卡
                                  KEY `idx_card_issuer` (`issuer_id`),
    -- 生效时间需早于到期时间
                                  CONSTRAINT `ck_card_date` CHECK (`start_date` < `end_date`),
                                  CONSTRAINT `ck_card_status` CHECK (`status` IN ('01', '02', '03')),
                                  CONSTRAINT `fk_card_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `t_vehicle` (`vehicle_id`),
                                  CONSTRAINT `fk_card_issuer` FOREIGN KEY (`issuer_id`) REFERENCES `t_sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储业主车月卡信息';


-- 9. 收费规则表（t_fee_rule）
CREATE TABLE `t_fee_rule` (
                              `rule_id` VARCHAR(6) NOT NULL COMMENT '规则标识（如"r00001"）',
                              `rule_name` VARCHAR(50) NOT NULL COMMENT '规则名称（如"临时车白天费率"）',
                              `apply_to` VARCHAR(2) NOT NULL COMMENT '适用对象（01=临时车，02=月卡超时）',
                              `vehicle_type` VARCHAR(2) NOT NULL COMMENT '适用车型（01=小型车，02=大型车）',
                              `first_hour_fee` DECIMAL(5,2) NOT NULL COMMENT '首小时费用（单位：元）',
                              `next_hour_fee` DECIMAL(5,2) NOT NULL COMMENT '后续小时费用（单位：元）',
                              `daily_cap` DECIMAL(5,2) NOT NULL COMMENT '每日封顶费用（单位：元）',
                              `night_start` VARCHAR(5) NOT NULL COMMENT '夜间时段开始（如"20:00"）',
                              `night_end` VARCHAR(5) NOT NULL COMMENT '夜间时段结束（如"08:00"）',
                              `night_rate` DECIMAL(5,2) NOT NULL COMMENT '夜间费率（单位：元/小时）',
                              `effective_date` DATE NOT NULL COMMENT '生效日期（如"2024-01-01"）',
                              PRIMARY KEY (`rule_id`),
                              UNIQUE KEY `uk_rule_apply_vehicle` (`apply_to`, `vehicle_type`), -- 同一对象+车型规则唯一
                              CONSTRAINT `ck_rule_apply_to` CHECK (`apply_to` IN ('01', '02')),
                              CONSTRAINT `ck_rule_vehicle_type` CHECK (`vehicle_type` IN ('01', '02')),
    -- 费用需为正数
                              CONSTRAINT `ck_rule_fee_positive` CHECK (
                                  `first_hour_fee` >= 0 AND
                                  `next_hour_fee` >= 0 AND
                                  `daily_cap` >= 0 AND
                                  `night_rate` >= 0
                                  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储计费标准';


-- 10. 停车会话表（t_parking_session）
CREATE TABLE `t_parking_session` (
                                     `session_id` VARCHAR(15) NOT NULL COMMENT '会话标识（如"s20241105001"）',
                                     `vehicle_id` VARCHAR(8) NOT NULL COMMENT '关联车辆ID',
                                     `card_id` VARCHAR(8) DEFAULT NULL COMMENT '关联月卡ID（可选）',
                                     `entry_time` DATETIME NOT NULL COMMENT '进场时间（精确到秒）',
                                     `exit_time` DATETIME DEFAULT NULL COMMENT '出场时间（未离场为null）',
                                     `entry_gate_id` VARCHAR(4) NOT NULL COMMENT '进场通道ID',
                                     `exit_gate_id` VARCHAR(4) DEFAULT NULL COMMENT '出场通道ID（可选）',
                                     `space_id` VARCHAR(8) DEFAULT NULL COMMENT '占用车位ID（可选）',
                                     `rule_id` VARCHAR(6) NOT NULL COMMENT '适用规则ID',
                                     PRIMARY KEY (`session_id`),
                                     KEY `idx_session_vehicle` (`vehicle_id`),
                                     KEY `idx_session_card` (`card_id`),
                                     KEY `idx_session_time` (`entry_time`, `exit_time`), -- 按时间查询优化
                                     KEY `idx_session_space` (`space_id`),
                                     KEY `idx_session_rule` (`rule_id`),
    -- 未离场时，出场时间和通道必须为null；已离场时必须有值
                                     CONSTRAINT `ck_session_exit` CHECK (
                                         (`exit_time` IS NULL AND `exit_gate_id` IS NULL) OR
                                         (`exit_time` IS NOT NULL AND `exit_gate_id` IS NOT NULL)
                                         ),
    -- 出场时间需晚于进场时间
                                     CONSTRAINT `ck_session_time` CHECK (`exit_time` > `entry_time` OR `exit_time` IS NULL),
                                     CONSTRAINT `fk_session_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `t_vehicle` (`vehicle_id`),
                                     CONSTRAINT `fk_session_card` FOREIGN KEY (`card_id`) REFERENCES `t_monthly_card` (`card_id`),
                                     CONSTRAINT `fk_session_entry_gate` FOREIGN KEY (`entry_gate_id`) REFERENCES `t_gate` (`gate_id`),
                                     CONSTRAINT `fk_session_exit_gate` FOREIGN KEY (`exit_gate_id`) REFERENCES `t_gate` (`gate_id`),
                                     CONSTRAINT `fk_session_space` FOREIGN KEY (`space_id`) REFERENCES `t_parking_space` (`space_id`),
                                     CONSTRAINT `fk_session_rule` FOREIGN KEY (`rule_id`) REFERENCES `t_fee_rule` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录完整停车周期数据';


-- 11. 过闸事件表（t_access_event）
CREATE TABLE `t_access_event` (
                                  `event_id` VARCHAR(15) NOT NULL COMMENT '事件标识（如"e20241105001"）',
                                  `gate_id` VARCHAR(4) NOT NULL COMMENT '通道ID',
                                  `event_time` DATETIME NOT NULL COMMENT '事件时间（精确到秒）',
                                  `vehicle_id` VARCHAR(8) DEFAULT NULL COMMENT '关联车辆ID（未识别为null）',
                                  `recognition_result` VARCHAR(2) NOT NULL COMMENT '识别结果（01=成功，02=无牌，03=黑名单）',
                                  `event_type` VARCHAR(2) NOT NULL COMMENT '事件类型（01=进场，02=出场）',
                                  `session_id` VARCHAR(15) DEFAULT NULL COMMENT '关联会话ID（出场时必填）',
                                  `handle_status` VARCHAR(2) NOT NULL COMMENT '处理状态（01=放行，02=拦截，03=人工操作）',
                                  `operator_id` VARCHAR(6) DEFAULT NULL COMMENT '操作人ID（人工操作时必填）',
                                  PRIMARY KEY (`event_id`),
                                  KEY `idx_event_gate_time` (`gate_id`, `event_time`), -- 按通道+时间查询优化
                                  KEY `idx_event_vehicle` (`vehicle_id`),
                                  KEY `idx_event_session` (`session_id`),
    -- 人工操作时必须有操作员ID
                                  CONSTRAINT `ck_event_operator` CHECK (
                                      (`handle_status` = '03' AND `operator_id` IS NOT NULL) OR
                                      (`handle_status` != '03' AND `operator_id` IS NULL)
                                      ),
    -- 出场事件必须关联会话ID
                                  CONSTRAINT `ck_event_session` CHECK (
                                      (`event_type` = '02' AND `session_id` IS NOT NULL) OR
                                      (`event_type` = '01')
                                      ),
                                  CONSTRAINT `ck_event_recognition` CHECK (`recognition_result` IN ('01', '02', '03')),
                                  CONSTRAINT `ck_event_type` CHECK (`event_type` IN ('01', '02')),
                                  CONSTRAINT `ck_event_handle_status` CHECK (`handle_status` IN ('01', '02', '03')),
                                  CONSTRAINT `fk_event_gate` FOREIGN KEY (`gate_id`) REFERENCES `t_gate` (`gate_id`),
                                  CONSTRAINT `fk_event_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `t_vehicle` (`vehicle_id`),
                                  CONSTRAINT `fk_event_session` FOREIGN KEY (`session_id`) REFERENCES `t_parking_session` (`session_id`),
                                  CONSTRAINT `fk_event_operator` FOREIGN KEY (`operator_id`) REFERENCES `t_sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录车辆过闸行为';


-- 12. 支付记录表（t_payment）
CREATE TABLE `t_payment` (
                             `payment_id` VARCHAR(15) NOT NULL COMMENT '支付记录标识（如"p20241105001"）',
                             `session_id` VARCHAR(15) NOT NULL COMMENT '关联会话ID',
                             `amount` DECIMAL(6,2) NOT NULL COMMENT '支付金额（单位：元）',
                             `pay_method` VARCHAR(2) NOT NULL COMMENT '支付方式（01=微信，02=支付宝，03=现金）',
                             `pay_time` DATETIME NOT NULL COMMENT '支付时间（精确到秒）',
                             `status` VARCHAR(2) NOT NULL COMMENT '支付状态（01=成功，02=失败，03=退款）',
                             `transaction_id` VARCHAR(50) DEFAULT NULL COMMENT '第三方支付单号（如微信支付流水号）',
                             PRIMARY KEY (`payment_id`),
                             UNIQUE KEY `uk_payment_session` (`session_id`), -- 一个停车会话对应一笔支付
                             CONSTRAINT `ck_payment_amount` CHECK (`amount` >= 0), -- 金额非负
                             CONSTRAINT `ck_payment_method` CHECK (`pay_method` IN ('01', '02', '03')),
                             CONSTRAINT `ck_payment_status` CHECK (`status` IN ('01', '02', '03')),
                             CONSTRAINT `fk_payment_session` FOREIGN KEY (`session_id`) REFERENCES `t_parking_session` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储停车缴费详情';


-- 初始化数据（修正原脚本冲突，确保一致性）
-- 1. 角色表
INSERT INTO `t_role` (`role_id`, `role_name`, `permissions`) VALUES
                                                                 ('r001', '超级管理员', '发卡,计费,查询,统计,系统管理'),
                                                                 ('r002', '操作员', '发卡,计费,查询'),
                                                                 ('r003', '财务', '查询,统计');

-- 2. 系统用户表（密码：123456，BCrypt加密）
INSERT INTO `t_sys_user` (`user_id`, `username`, `password`, `role_id`, `department`, `status`) VALUES
                                                                                                    ('u00001', 'admin', '$2a$10$CamVlH2CDusn47lyR1/IOeWsKR6GsKhGypjYrznZveaOtKFPe9c8K', 'r001', '管理中心', '01'),
                                                                                                    ('u00002', 'operator', '$2a$10$CamVlH2CDusn47lyR1/IOeWsKR6GsKhGypjYrznZveaOtKFPe9c8K', 'r002', '管理中心', '01'),
                                                                                                    ('u00003', 'finance', '$2a$10$CamVlH2CDusn47lyR1/IOeWsKR6GsKhGypjYrznZveaOtKFPe9c8K', 'r003', '管理中心', '01');

-- 3. 楼层表（两层地下车库）
INSERT INTO `t_floor` (`floor_id`, `floor_name`, `total_spaces`) VALUES
                                                                     ('b1', '地下一层', 200),
                                                                     ('b2', '地下二层', 200);

-- 4. 通道表（两进两出）
INSERT INTO `t_gate` (`gate_id`, `gate_type`, `entrance_id`, `status`) VALUES
                                                                           ('in1', '01', '01', '01'),  -- 1号入口
                                                                           ('out1', '02', '01', '01'), -- 1号出口
                                                                           ('in2', '01', '02', '01'),  -- 2号入口
                                                                           ('out2', '02', '02', '01'); -- 2号出口

-- 5. 收费规则表
INSERT INTO `t_fee_rule` (`rule_id`, `rule_name`, `apply_to`, `vehicle_type`, `first_hour_fee`, `next_hour_fee`, `daily_cap`, `night_start`, `night_end`, `night_rate`, `effective_date`) VALUES
                                                                                                                                                                                              ('r00001', '临时车小型车费率', '01', '01', 5.00, 3.00, 30.00, '20:00', '08:00', 2.00, '2024-01-01'),
                                                                                                                                                                                              ('r00002', '临时车大型车费率', '01', '02', 8.00, 5.00, 50.00, '20:00', '08:00', 3.00, '2024-01-01'),
                                                                                                                                                                                              ('r00003', '月卡超时费率', '02', '01', 2.00, 1.00, 15.00, '20:00', '08:00', 1.00, '2024-01-01');

-- 6. 业主表
INSERT INTO `t_owner` (`owner_id`, `name`, `room_no`, `phone`) VALUES
                                                                   ('o00001', '张三', '1栋1单元101', '13800138001'),
                                                                   ('o00002', '李四', '2栋3单元502', '13800138002'),
                                                                   ('o00003', '王五', '3栋2单元301', '13800138003');

-- 7. 车辆表（业主车+临时车）
INSERT INTO `t_vehicle` (`vehicle_id`, `license_plate`, `vehicle_type`, `is_owner_car`, `owner_id`) VALUES
                                                                                                        ('v0000001', '粤A12345', '01', '01', 'o00001'), -- 业主车
                                                                                                        ('v0000002', '粤B67890', '01', '01', 'o00002'), -- 业主车
                                                                                                        ('v0000003', '粤C13579', '02', '01', 'o00003'), -- 业主车
                                                                                                        ('v0000004', '粤D24680', '01', '02', NULL);     -- 临时车

-- 8. 月卡表（业主车绑定月卡）
INSERT INTO `t_monthly_card` (`card_id`, `vehicle_id`, `issuer_id`, `start_date`, `end_date`, `status`) VALUES
                                                                                                            ('c0000001', 'v0000001', 'u00001', '2024-01-01 00:00:00', '2024-12-31 23:59:59', '01'),
                                                                                                            ('c0000002', 'v0000002', 'u00001', '2024-01-01 00:00:00', '2024-12-31 23:59:59', '01'),
                                                                                                            ('c0000003', 'v0000003', 'u00001', '2024-01-01 00:00:00', '2024-06-30 23:59:59', '03'); -- 已过期

-- 9. 车位表（初始化400个车位，含3个固定车位）
DELIMITER //
CREATE PROCEDURE init_parking_spaces()
BEGIN
    DECLARE i INT DEFAULT 1;
    -- 地下一层车位（b1_001 至 b1_200）
    WHILE i <= 200 DO
            INSERT INTO `t_parking_space` (`space_id`, `floor_id`, `space_no`, `status`, `is_fixed`, `owner_id`)
            VALUES (CONCAT('b1_', LPAD(i, 3, '0')), 'b1', LPAD(i, 3, '0'), '01', '02', NULL);
            SET i = i + 1;
        END WHILE;

    -- 地下二层车位（b2_001 至 b2_200）
    SET i = 1;
    WHILE i <= 200 DO
            INSERT INTO `t_parking_space` (`space_id`, `floor_id`, `space_no`, `status`, `is_fixed`, `owner_id`)
            VALUES (CONCAT('b2_', LPAD(i, 3, '0')), 'b2', LPAD(i, 3, '0'), '01', '02', NULL);
            SET i = i + 1;
        END WHILE;
END //
DELIMITER ;
CALL init_parking_spaces();
DROP PROCEDURE IF EXISTS init_parking_spaces;

-- 10. 停车会话表示例数据
INSERT INTO `t_parking_session` (`session_id`, `vehicle_id`, `card_id`, `entry_time`, `exit_time`, `entry_gate_id`, `exit_gate_id`, `space_id`, `rule_id`) VALUES
                                                                                                                                                               ('s20241105001', 'v0000001', 'c0000001', '2024-11-05 08:30:00', '2024-11-05 18:45:00', 'in1', 'out1', 'b1_001', 'r00003'),
                                                                                                                                                               ('s20241105002', 'v0000004', NULL, '2024-11-05 09:15:00', '2024-11-05 12:30:00', 'in2', 'out2', 'b2_050', 'r00001'),
                                                                                                                                                               ('s20241105003', 'v0000002', 'c0000002', '2024-11-05 10:00:00', NULL, 'in1', NULL, 'b1_002', 'r00003');

-- 11. 过闸事件表示例数据
INSERT INTO `t_access_event` (`event_id`, `gate_id`, `event_time`, `vehicle_id`, `recognition_result`, `event_type`, `session_id`, `handle_status`, `operator_id`) VALUES
                                                                                                                                                                       ('e20241105001', 'in1', '2024-11-05 08:30:00', 'v0000001', '01', '01', NULL, '01', NULL),
                                                                                                                                                                       ('e20241105002', 'out1', '2024-11-05 18:45:00', 'v0000001', '01', '02', 's20241105001', '01', NULL),
                                                                                                                                                                       ('e20241105003', 'in2', '2024-11-05 09:15:00', 'v0000004', '01', '01', NULL, '01', NULL),
                                                                                                                                                                       ('e20241105004', 'out2', '2024-11-05 12:30:00', 'v0000004', '01', '02', 's20241105002', '01', NULL),
                                                                                                                                                                       ('e20241105005', 'in1', '2024-11-05 10:00:00', 'v0000002', '01', '01', NULL, '01', NULL);

-- 12. 支付记录表（临时车+月卡超时）
INSERT INTO `t_payment` (`payment_id`, `session_id`, `amount`, `pay_method`, `pay_time`, `status`, `transaction_id`) VALUES
                                                                                                                         ('p20241105001', 's20241105001', 12.00, '01', '2024-11-05 18:45:00', '01', 'wx202411051845000001'),
                                                                                                                         ('p20241105002', 's20241105002', 11.00, '02', '2024-11-05 12:30:00', '01', 'alipay202411051230000001');