package com.example.electricitybillingsystem.config;

import com.example.electricitybillingsystem.entity.*;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.entity.enums.PaymentMode;
import com.example.electricitybillingsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ConsumerRepository consumerRepository;
    private final ElectricityConnectionRepository connectionRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ConsumerRepository consumerRepository,
                           ElectricityConnectionRepository connectionRepository,
                           MeterReadingRepository meterReadingRepository,
                           BillRepository billRepository,
                           PaymentRepository paymentRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.consumerRepository = consumerRepository;
        this.connectionRepository = connectionRepository;
        this.meterReadingRepository = meterReadingRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (consumerRepository.count() < 50 || userRepository.count() == 0) {
            paymentRepository.deleteAllInBatch();
            billRepository.deleteAllInBatch();
            meterReadingRepository.deleteAllInBatch();
            connectionRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
            consumerRepository.deleteAllInBatch();

            String[] firstNames = {
                "John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Oliver", "Isabella", 
                "David", "Mia", "Joseph", "Charlotte", "Daniel", "Amelia", "Robert", "Harper", "Thomas", "Evelyn",
                "Charles", "Abigail", "Matthew", "Emily", "George", "Elizabeth", "Edward", "Sofia", "Richard", "Avery",
                "Joseph", "Ella", "Daniel", "Madison", "Paul", "Scarlett", "Mark", "Victoria", "Donald", "Grace",
                "Steven", "Chloe", "Andrew", "Camila", "Kenneth", "Penelope", "Joshua", "Layla", "Kevin", "Riley"
            };
            String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
                "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
                "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
                "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts"
            };

            Random random = new Random();

            for (int i = 0; i < 50; i++) {
                // 1. Create Consumer
                Consumer consumer = new Consumer();
                consumer.setConsumerNumber(String.format("CON%04d", 1001 + i));
                consumer.setFirstName(firstNames[i]);
                consumer.setLastName(lastNames[i]);
                consumer.setEmail(String.format("%s.%s@example.com", firstNames[i].toLowerCase(), lastNames[i].toLowerCase()));
                consumer.setPhone(String.format("98765432%02d", 10 + (i % 90)));
                Consumer savedConsumer = consumerRepository.save(consumer);
                
                // Create User login credentials for the consumer
                User user = User.builder()
                        .username(savedConsumer.getConsumerNumber().toLowerCase())
                        .password(passwordEncoder.encode("password"))
                        .role("ROLE_CONSUMER")
                        .consumer(savedConsumer)
                        .build();
                userRepository.save(user);
                
                // 2. Create Electricity Connection
                ElectricityConnection conn = new ElectricityConnection();
                conn.setConnectionNumber(String.format("CON%04d", 1001 + i));
                conn.setMeterNumber(String.format("MET%05d", 20000 + i));
                conn.setConnectionType(i % 3 == 0 ? "COMMERCIAL" : (i % 5 == 0 ? "INDUSTRIAL" : "DOMESTIC"));
                conn.setStatus("ACTIVE");
                conn.setSanctionedLoad(i % 4 == 0 ? 15.0 : (i % 7 == 0 ? 45.0 : 5.0));
                conn.setPhaseType(i % 3 == 0 ? "THREE_PHASE" : "SINGLE_PHASE");
                conn.setConsumer(savedConsumer);
                ElectricityConnection savedConn = connectionRepository.save(conn);
                
                // 3. Create historical readings, bills, and payments for Jan - Jun 2026
                int lastReadingValue = 100;
                for (int monthIdx = 1; monthIdx <= 6; monthIdx++) {
                    LocalDate readingDate = LocalDate.of(2026, monthIdx, 15);
                    int consumed = 150 + random.nextInt(300); // 150 - 450 kWh
                    int currentReadingValue = lastReadingValue + consumed;

                    // Save Reading
                    MeterReading mr = new MeterReading();
                    mr.setConnection(savedConn);
                    mr.setReadingDate(readingDate);
                    mr.setPreviousReading(BigDecimal.valueOf(lastReadingValue));
                    mr.setCurrentReading(BigDecimal.valueOf(currentReadingValue));
                    mr.setUnitsConsumed(BigDecimal.valueOf(consumed));
                    mr.setRemarks("Regular monthly reading");
                    mr.setCreatedAt(readingDate.atStartOfDay());
                    mr.setUpdatedAt(readingDate.atStartOfDay());
                    MeterReading savedMr = meterReadingRepository.save(mr);
                    
                    lastReadingValue = currentReadingValue;

                    // Calculate rates
                    BigDecimal units = BigDecimal.valueOf(consumed);
                    BigDecimal fixed = BigDecimal.valueOf(120.00);
                    BigDecimal energy = units.multiply(BigDecimal.valueOf(6.50));
                    BigDecimal duty = energy.multiply(BigDecimal.valueOf(0.05)); // 5% tax
                    BigDecimal total = fixed.add(energy).add(duty);

                    // Save Bill
                    Bill bill = new Bill();
                    bill.setBillNumber(String.format("BIL%04d%02d", 1001 + i, monthIdx));
                    bill.setBillingMonth(readingDate.getMonth().toString() + " 2026");
                    bill.setBillDate(readingDate.plusDays(2));
                    bill.setDueDate(readingDate.plusDays(17));
                    bill.setUnitsConsumed(units);
                    bill.setEnergyCharge(energy);
                    bill.setFixedCharge(fixed);
                    bill.setElectricityDuty(duty);
                    bill.setTotalAmount(total);
                    
                    // The last bill (June) might be unpaid for some consumers, earlier bills are paid
                    BillStatus status = (monthIdx == 6 && i % 3 == 0) ? BillStatus.UNPAID : BillStatus.PAID;
                    bill.setBillStatus(status);
                    bill.setMeterReading(savedMr);
                    Bill savedBill = billRepository.save(bill);

                    // If bill is paid, create a Payment record
                    if (status == BillStatus.PAID) {
                        Payment payment = new Payment();
                        payment.setTransactionId(String.format("TXN%06d%02d", 900000 + i, monthIdx));
                        payment.setAmountPaid(total);
                        payment.setPaymentDate(readingDate.plusDays(5));
                        
                        // Select a random payment mode
                        PaymentMode mode = PaymentMode.values()[i % PaymentMode.values().length];
                        payment.setPaymentMode(mode);
                        
                        payment.setBill(savedBill);
                    }
                }
            }
            // Seed Admin User
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role("ROLE_ADMIN")
                    .build();
            userRepository.save(admin);

            System.out.println("Successfully seeded 50 consumers, connections, readings, bills, payments, and user credentials!");
        }
    }
}
