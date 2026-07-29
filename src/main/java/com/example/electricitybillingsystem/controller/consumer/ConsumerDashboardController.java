package com.example.electricitybillingsystem.controller.consumer;

import com.example.electricitybillingsystem.dto.*;
import com.example.electricitybillingsystem.entity.*;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.repository.*;
import com.example.electricitybillingsystem.mapper.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import com.example.electricitybillingsystem.service.ConsumerService;

@RestController
@RequestMapping("/api/consumer")
@PreAuthorize("hasRole('CONSUMER')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConsumerDashboardController {

    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final ElectricityConnectionRepository connectionRepository;
    private final ConsumerService consumerService;
    
    private final ConsumerMapper consumerMapper;
    private final BillMapper billMapper;
    private final PaymentMapper paymentMapper;
    private final MeterReadingMapper meterReadingMapper;

    public ConsumerDashboardController(UserRepository userRepository,
                                       BillRepository billRepository,
                                       PaymentRepository paymentRepository,
                                       MeterReadingRepository meterReadingRepository,
                                       ElectricityConnectionRepository connectionRepository,
                                       ConsumerService consumerService,
                                       ConsumerMapper consumerMapper,
                                       BillMapper billMapper,
                                       PaymentMapper paymentMapper,
                                       MeterReadingMapper meterReadingMapper) {
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.meterReadingRepository = meterReadingRepository;
        this.connectionRepository = connectionRepository;
        this.consumerService = consumerService;
        this.consumerMapper = consumerMapper;
        this.billMapper = billMapper;
        this.paymentMapper = paymentMapper;
        this.meterReadingMapper = meterReadingMapper;
    }

    @GetMapping("/ping")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    private Consumer getAuthenticatedConsumer(Principal principal) {
        if (principal == null) {
            throw new UsernameNotFoundException("Principal is null");
        }
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + principal.getName()));
        
        Consumer consumer = user.getConsumer();
        if (consumer == null) {
            throw new IllegalArgumentException("Logged-in user is not associated with any consumer profile.");
        }
        return consumer;
    }

    @GetMapping("/profile")
    public ResponseEntity<ConsumerDTO> getProfile(Principal principal) {
        if (principal == null) {
            throw new UsernameNotFoundException("Principal is null");
        }
        ConsumerDTO profile = consumerService.getConsumerProfileByUsername(principal.getName());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/bills")
    public ResponseEntity<List<BillDTO>> getConsumerBills(Principal principal) {
        Consumer consumer = getAuthenticatedConsumer(principal);
        List<ElectricityConnection> conns = connectionRepository.findByConsumerId(consumer.getId());
        List<String> connectionNumbers = conns.stream()
                .map(ElectricityConnection::getConnectionNumber)
                .collect(Collectors.toList());

        List<Bill> bills = billRepository.findAll().stream()
                .filter(b -> b.getMeterReading() != null && 
                             b.getMeterReading().getConnection() != null &&
                             connectionNumbers.contains(b.getMeterReading().getConnection().getConnectionNumber()))
                .collect(Collectors.toList());

        List<BillDTO> billDTOs = bills.stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(billDTOs);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getConsumerPayments(Principal principal) {
        Consumer consumer = getAuthenticatedConsumer(principal);
        List<ElectricityConnection> conns = connectionRepository.findByConsumerId(consumer.getId());
        List<String> connectionNumbers = conns.stream()
                .map(ElectricityConnection::getConnectionNumber)
                .collect(Collectors.toList());

        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(p -> p.getBill() != null &&
                             p.getBill().getMeterReading() != null &&
                             p.getBill().getMeterReading().getConnection() != null &&
                             connectionNumbers.contains(p.getBill().getMeterReading().getConnection().getConnectionNumber()))
                .collect(Collectors.toList());

        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(paymentDTOs);
    }

    @GetMapping("/meter-readings")
    public ResponseEntity<List<MeterReadingDTO>> getConsumerMeterReadings(Principal principal) {
        Consumer consumer = getAuthenticatedConsumer(principal);
        List<ElectricityConnection> conns = connectionRepository.findByConsumerId(consumer.getId());
        List<String> connectionNumbers = conns.stream()
                .map(ElectricityConnection::getConnectionNumber)
                .collect(Collectors.toList());

        List<MeterReading> readings = meterReadingRepository.findAll().stream()
                .filter(mr -> mr.getConnection() != null &&
                             connectionNumbers.contains(mr.getConnection().getConnectionNumber()))
                .collect(Collectors.toList());

        List<MeterReadingDTO> readingDTOs = readings.stream()
                .map(meterReadingMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(readingDTOs);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ConsumerDashboardDTO> getDashboard(Principal principal) {
        Consumer consumer = getAuthenticatedConsumer(principal);
        List<ElectricityConnection> conns = connectionRepository.findByConsumerId(consumer.getId());
        
        ElectricityConnection activeConn = conns.stream()
                .filter(c -> "ACTIVE".equalsIgnoreCase(c.getStatus()))
                .findFirst()
                .orElse(conns.isEmpty() ? null : conns.get(0));

        String connNumber = activeConn != null ? activeConn.getConnectionNumber() : "N/A";
        String meterNumber = activeConn != null ? activeConn.getMeterNumber() : "N/A";
        String connType = activeConn != null ? activeConn.getConnectionType() : "N/A";
        String status = activeConn != null ? activeConn.getStatus() : "INACTIVE";

        List<String> connectionNumbers = conns.stream()
                .map(ElectricityConnection::getConnectionNumber)
                .collect(Collectors.toList());

        List<Bill> allBills = billRepository.findAll().stream()
                .filter(b -> b.getMeterReading() != null && 
                             b.getMeterReading().getConnection() != null &&
                             connectionNumbers.contains(b.getMeterReading().getConnection().getConnectionNumber()))
                .collect(Collectors.toList());

        BigDecimal outstanding = allBills.stream()
                .filter(b -> b.getBillStatus() == BillStatus.UNPAID)
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String paymentStatus = "NO_BILLS";
        if (!allBills.isEmpty()) {
            boolean hasUnpaid = allBills.stream().anyMatch(b -> b.getBillStatus() == BillStatus.UNPAID);
            paymentStatus = hasUnpaid ? "UNPAID" : "PAID";
        }

        BigDecimal currentUnits = BigDecimal.ZERO;
        List<MeterReading> readings = meterReadingRepository.findAll().stream()
                .filter(mr -> mr.getConnection() != null &&
                             connectionNumbers.contains(mr.getConnection().getConnectionNumber()))
                .sorted(Comparator.comparing(MeterReading::getReadingDate).reversed())
                .collect(Collectors.toList());
                
        if (!readings.isEmpty()) {
            MeterReading latest = readings.get(0);
            currentUnits = latest.getUnitsConsumed() != null ? latest.getUnitsConsumed() : BigDecimal.ZERO;
        }

        List<BillDTO> recentBills = allBills.stream()
                .sorted(Comparator.comparing(Bill::getBillDate).reversed())
                .limit(5)
                .map(billMapper::toDTO)
                .collect(Collectors.toList());

        List<Payment> allPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getBill() != null &&
                             p.getBill().getMeterReading() != null &&
                             p.getBill().getMeterReading().getConnection() != null &&
                             connectionNumbers.contains(p.getBill().getMeterReading().getConnection().getConnectionNumber()))
                .collect(Collectors.toList());

        List<PaymentDTO> paymentHistory = allPayments.stream()
                .sorted(Comparator.comparing(Payment::getPaymentDate).reversed())
                .limit(5)
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());

        BigDecimal lastPayAmt = BigDecimal.ZERO;
        LocalDate lastPayDate = null;
        if (!allPayments.isEmpty()) {
            Payment latestPay = allPayments.stream()
                    .sorted(Comparator.comparing(Payment::getPaymentDate).reversed())
                    .findFirst()
                    .orElse(null);
            if (latestPay != null) {
                lastPayAmt = latestPay.getAmountPaid();
                lastPayDate = latestPay.getPaymentDate();
            }
        }

        Map<String, BigDecimal> monthlyUnits = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate d = now.minusMonths(i);
            String mKey = d.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + d.getYear();
            monthlyUnits.put(mKey, BigDecimal.ZERO);
        }

        for (MeterReading mr : readings) {
            if (mr.getReadingDate() == null) continue;
            LocalDate rDate = mr.getReadingDate();
            String mKey = rDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + rDate.getYear();
            if (monthlyUnits.containsKey(mKey)) {
                BigDecimal units = mr.getUnitsConsumed() != null ? mr.getUnitsConsumed() : BigDecimal.ZERO;
                monthlyUnits.put(mKey, monthlyUnits.get(mKey).add(units));
            }
        }

        List<ConsumptionMonthDTO> consumptionHistory = monthlyUnits.entrySet().stream()
                .map(e -> new ConsumptionMonthDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        double tariff = 6.50;
        if ("COMMERCIAL".equalsIgnoreCase(connType)) {
            tariff = 9.25;
        } else if ("INDUSTRIAL".equalsIgnoreCase(connType)) {
            tariff = 12.00;
        }

        return ResponseEntity.ok(ConsumerDashboardDTO.builder()
                .consumerNumber(consumer.getConsumerNumber())
                .fullName(consumer.getFirstName() + " " + consumer.getLastName())
                .connectionNumber(connNumber)
                .meterNumber(meterNumber)
                .connectionType(connType)
                .connectionStatus(status)
                .currentMonthUnitsConsumed(currentUnits)
                .currentOutstandingBill(outstanding)
                .paymentStatus(paymentStatus)
                .currentTariff(tariff)
                .lastPaymentAmount(lastPayAmt)
                .lastPaymentDate(lastPayDate)
                .recentBills(recentBills)
                .paymentHistory(paymentHistory)
                .monthlyConsumptionHistory(consumptionHistory)
                .build());
    }
}
