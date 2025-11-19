package com.tree.plms.service.impl;

import com.tree.plms.model.entity.Payment;
import com.tree.plms.mapper.PaymentMapper;
import com.tree.plms.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 存储停车缴费详情 服务实现类
 * </p>
 *
 * @author tree
 * @since 2025-11-12
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Override
    public Payment getPaymentById(String paymentId) {
        return baseMapper.selectById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean addPayment(Payment payment) {
        return save(payment);
    }

    @Override
    public boolean updatePayment(Payment payment) {
        return updateById(payment);
    }

    @Override
    public boolean deletePayment(String paymentId) {
        return removeById(paymentId);
    }

    @Override
    public List<Payment> getPaymentsBySessionId(String sessionId) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Payment> getPaymentsByPayMethod(String payMethod) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pay_method", payMethod);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Payment> getPaymentsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("pay_time", startTime, endTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("transaction_id", transactionId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public BigDecimal calculateTotalAmountByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("pay_time", startTime, endTime)
                   .eq("status", "01"); // 仅计算成功的支付
        // 这里假设PaymentMapper中有calculateTotalAmount方法，如果没有则需要在Mapper接口中添加
        return baseMapper.selectObjs(queryWrapper).stream()
                .map(obj -> (BigDecimal) obj)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 获取支付方式名称
     * @param payMethod 支付方式代码
     * @return 支付方式名称
     */
    private String getPayMethodName(String payMethod) {
        switch (payMethod) {
            case "01":
                return "微信";
            case "02":
                return "支付宝";
            case "03":
                return "现金";
            default:
                return "其他";
        }
    }
    
    /**
     * 获取支付状态名称
     * @param status 支付状态代码
     * @return 支付状态名称
     */
    private String getStatusName(String status) {
        switch (status) {
            case "01":
                return "成功";
            case "02":
                return "失败";
            case "03":
                return "退款";
            default:
                return "未知";
        }
    }
}