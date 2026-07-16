package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.DashboardResponseDTO;
import com.example.electricitybillingsystem.dto.PredictionDTO;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import com.example.electricitybillingsystem.repository.PaymentRepository;
import com.example.electricitybillingsystem.repository.MeterReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ConsumerRepository consumerRepository;
    private final ElectricityConnectionRepository electricityConnectionRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final MeterReadingRepository meterReadingRepository;

    public DashboardService(ConsumerRepository consumerRepository,
                            ElectricityConnectionRepository electricityConnectionRepository,
                            BillRepository billRepository,
                            PaymentRepository paymentRepository,
                            MeterReadingRepository meterReadingRepository) {
        this.consumerRepository = consumerRepository;
        this.electricityConnectionRepository = electricityConnectionRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.meterReadingRepository = meterReadingRepository;
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

    @Transactional(readOnly = true)
    public List<PredictionDTO> getConsumptionPredictions(String connectionNumber) {
        List<MeterReading> readings = meterReadingRepository.findAll();
        
        if (connectionNumber != null && !connectionNumber.trim().isEmpty()) {
            readings = readings.stream()
                    .filter(r -> r.getConnection() != null && connectionNumber.equalsIgnoreCase(r.getConnection().getConnectionNumber()))
                    .collect(Collectors.toList());
        }

        // Group readings by Month (normalized to 1st of month)
        Map<LocalDate, Double> monthlyConsumptions = new TreeMap<>();
        for (MeterReading mr : readings) {
            if (mr.getReadingDate() == null) continue;
            LocalDate firstOfMonth = mr.getReadingDate().withDayOfMonth(1);
            double consumed = mr.getUnitsConsumed() != null ? mr.getUnitsConsumed().doubleValue() : 0.0;
            monthlyConsumptions.put(firstOfMonth, monthlyConsumptions.getOrDefault(firstOfMonth, 0.0) + consumed);
        }

        List<Map.Entry<LocalDate, Double>> sortedEntries = new ArrayList<>(monthlyConsumptions.entrySet());
        
        double slope = 0;
        double intercept = 250.0; // default average fallback
        int n = sortedEntries.size();
        
        if (n >= 2) {
            double sumX = 0;
            double sumY = 0;
            double sumXY = 0;
            double sumX2 = 0;
            
            for (int i = 0; i < n; i++) {
                double x = i + 1;
                double y = sortedEntries.get(i).getValue();
                
                sumX += x;
                sumY += y;
                sumXY += x * y;
                sumX2 += x * x;
            }
            
            double denominator = (n * sumX2) - (sumX * sumX);
            if (denominator != 0) {
                slope = ((n * sumXY) - (sumX * sumY)) / denominator;
                intercept = (sumY - (slope * sumX)) / n;
            }
        } else if (n == 1) {
            intercept = sortedEntries.get(0).getValue();
        }

        List<PredictionDTO> predictions = new ArrayList<>();
        LocalDate lastDate = n > 0 ? sortedEntries.get(n - 1).getKey() : LocalDate.now().withDayOfMonth(1);
        
        for (int i = 1; i <= 3; i++) {
            LocalDate targetDate = lastDate.plusMonths(i);
            String monthName = targetDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            
            double xVal = n + i;
            double predictedVal = (slope * xVal) + intercept;
            if (predictedVal < 50.0) {
                predictedVal = 50.0;
            }
            
            predictions.add(PredictionDTO.builder()
                    .month(monthName)
                    .predictedKwh(Math.round(predictedVal * 100.0) / 100.0)
                    .lowerBoundKwh(Math.round(predictedVal * 0.88 * 100.0) / 100.0)
                    .upperBoundKwh(Math.round(predictedVal * 1.12 * 100.0) / 100.0)
                    .build());
        }
        
        return predictions;
    }
}
