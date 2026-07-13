package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.DashboardResponseDTO;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import com.example.electricitybillingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final ConsumerRepository consumerRepository;
    private final ElectricityConnectionRepository electricityConnectionRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    public DashboardService(ConsumerRepository consumerRepository,
                            ElectricityConnectionRepository electricityConnectionRepository,
                            BillRepository billRepository,
                            PaymentRepository paymentRepository) {
        this.consumerRepository = consumerRepository;
        this.electricityConnectionRepository = electricityConnectionRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData() {
        long totalConsumers = consumerRepository.count();
        long totalConnections = electricityConnectionRepository.count();
        long totalBills = billRepository.count();
        long paidBills = billRepository.countByBillStatus(BillStatus.PAID);
        long unpaidBills = billRepository.countByBillStatus(BillStatus.UNPAID);

        return DashboardResponseDTO.builder()
                .totalConsumers(totalConsumers)
                .totalConnections(totalConnections)
                .totalBills(totalBills)
                .paidBills(paidBills)
                .unpaidBills(unpaidBills)
                .todayCollection(paymentRepository.sumTodayCollection())
                .monthlyRevenue(paymentRepository.sumMonthlyRevenue())
                .totalRevenue(paymentRepository.sumTotalRevenue())
                .build();
    }
}
