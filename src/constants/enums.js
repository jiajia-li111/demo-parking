// 将模型中的“域”值映射为人可读文案
export const VEHICLE_TYPE = { '01': '小型车', '02': '大型车' }
export const BOOLEAN_YN = { '01': '是', '02': '否' }
export const SPACE_STATUS = { '01': '空闲', '02': '占用', '03': '故障' }
export const GATE_TYPE = { '01': '入口', '02': '出口' }
export const CARD_STATUS = { '01': '启用', '02': '挂失', '03': '过期' }
export const EVENT_TYPE = { '01': '进场', '02': '出场' }
export const HANDLE_STAT = { '01': '放行', '02': '拦截', '03': '人工操作' }
export const APPLY_TO = { '01': '临时车', '02': '月卡超时' }
export const PAY_METHOD = { '01': '微信', '02': '支付宝', '03': '现金' }
export const PAY_STATUS = { '01': '成功', '02': '失败', '03': '退款' }
export const USER_STATUS = { '01': '启用', '02': '禁用' }


export const mapEnum = (map, v) => map?.[v] ?? v