package com.tree.plms.service;

import com.tree.plms.model.entity.Payment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 存储停车缴费详情 服务类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
public interface PaymentService extends IService<Payment> {

    /**
     * 根据支付ID查询支付记录
     * @param paymentId 支付记录标识
     * @return 支付记录
     */
    Payment getPaymentById(String paymentId);

    /**
     * 获取所有支付记录
     * @return 支付记录列表
     */
    List<Payment> getAllPayments();

    /**
     * 新增支付记录
     * @param payment 支付记录
     * @return 是否新增成功
     */
    boolean addPayment(Payment payment);

    /**
     * 更新支付记录
     * @param payment 支付记录
     * @return 是否更新成功
     */
    boolean updatePayment(Payment payment);

    /**
     * 删除支付记录
     * @param paymentId 支付记录标识
     * @return 是否删除成功
     */
    boolean deletePayment(String paymentId);

    /**
     * 根据会话ID查询支付记录
     * @param sessionId 关联会话ID
     * @return 支付记录列表
     */
    List<Payment> getPaymentsBySessionId(String sessionId);

    /**
     * 根据支付方式查询支付记录
     * @param payMethod 支付方式（01=微信，02=支付宝，03=现金）
     * @return 支付记录列表
     */
    List<Payment> getPaymentsByPayMethod(String payMethod);

    /**
     * 根据支付状态查询支付记录
     * @param status 支付状态（01=成功，02=失败，03=退款）
     * @return 支付记录列表
     */
    List<Payment> getPaymentsByStatus(String status);

    /**
     * 根据时间范围查询支付记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 支付记录列表
     */
    List<Payment> getPaymentsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据交易单号查询支付记录
     * @param transactionId 第三方支付单号
     * @return 支付记录
     */
    Payment getPaymentByTransactionId(String transactionId);

    /**
     * 计算指定时间范围内的总支付金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总支付金额
     */
    BigDecimal calculateTotalAmountByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}