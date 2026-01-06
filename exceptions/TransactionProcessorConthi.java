package exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Кастомные исключения
class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

class CriticalTransactionException extends Exception {
    public CriticalTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Класс транзакции
class Transaction {
    private double amount;
    private String accountId;
    private String transactionId;
    private LocalDateTime timestamp;
    private TransactionStatus status;

    public Transaction(double amount, String accountId) {
        this.amount = amount;
        this.accountId = accountId;
        this.transactionId = "TXN-" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    public double getAmount() { return amount; }
    public String getAccountId() { return accountId; }
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Transaction[id=%s, account=%s, amount=%.2f, status=%s]",
                transactionId, accountId, amount, status);
    }
}

// Статус транзакции
enum TransactionStatus {
    PENDING, VALIDATED, PROCESSING, COMPLETED, FAILED, ROLLED_BACK
}

// Интерфейс обработчика в цепочке
interface ExceptionHandler {
    void setNext(ExceptionHandler handler);
    void handle(Transaction transaction) throws Exception;
}

// Абстрактный базовый класс для обработчиков
abstract class BaseExceptionHandler implements ExceptionHandler {
    protected ExceptionHandler nextHandler;
    protected Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void setNext(ExceptionHandler handler) {
        this.nextHandler = handler;
    }

    protected void passToNext(Transaction transaction) throws Exception {
        if (nextHandler != null) {
            nextHandler.handle(transaction);
        }
    }
}

// 1. Обработчик валидации
class ValidationHandler extends BaseExceptionHandler {
    private static final double MIN_AMOUNT = 0.01;
    private static final double MAX_AMOUNT = 1000000.00;

    @Override
    public void handle(Transaction transaction) throws Exception {
        try {
            logger.info("Validating transaction: " + transaction.getTransactionId());

            // Проверка суммы
            if (transaction.getAmount() < MIN_AMOUNT) {
                throw new ValidationException(
                        "Amount too small. Minimum: " + MIN_AMOUNT);
            }

            if (transaction.getAmount() > MAX_AMOUNT) {
                throw new ValidationException(
                        "Amount exceeds maximum limit: " + MAX_AMOUNT);
            }

            // Проверка accountId
            if (transaction.getAccountId() == null ||
                    transaction.getAccountId().trim().isEmpty()) {
                throw new ValidationException("Account ID cannot be empty");
            }

            if (!transaction.getAccountId().matches("ACC-\\d{6}")) {
                throw new ValidationException(
                        "Invalid account ID format. Expected: ACC-XXXXXX");
            }

            transaction.setStatus(TransactionStatus.VALIDATED);
            logger.info("Validation passed for: " + transaction.getTransactionId());

            // Передаем следующему обработчику
            passToNext(transaction);

        } catch (ValidationException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            logger.warning("Validation failed: " + e.getMessage());
            throw e;
        }
    }
}

// 2. Обработчик бизнес-логики
class BusinessLogicHandler extends BaseExceptionHandler {
    // Симуляция базы данных счетов
    private static class AccountDatabase {
        static double getBalance(String accountId) throws AccountNotFoundException {
            // Симуляция различных случаев
            if (accountId.equals("ACC-000000")) {
                throw new AccountNotFoundException("Account not found: " + accountId);
            }
            if (accountId.equals("ACC-111111")) {
                return 100.0; // Недостаточно средств
            }
            return 10000.0; // Достаточно средств
        }
    }

    @Override
    public void handle(Transaction transaction) throws Exception {
        try {
            logger.info("Processing business logic: " + transaction.getTransactionId());

            // Проверка существования счета
            double balance = AccountDatabase.getBalance(transaction.getAccountId());

            // Проверка достаточности средств
            if (balance < transaction.getAmount()) {
                throw new InsufficientFundsException(
                        String.format("Insufficient funds. Balance: %.2f, Required: %.2f",
                                balance, transaction.getAmount()));
            }

            transaction.setStatus(TransactionStatus.PROCESSING);
            logger.info("Business logic check passed: " + transaction.getTransactionId());

            // Передаем следующему обработчику
            passToNext(transaction);

        } catch (AccountNotFoundException | InsufficientFundsException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            logger.warning("Business logic failed: " + e.getMessage());
            throw e;
        }
    }
}

// 3. Обработчик критических ошибок с логированием
class CriticalErrorHandler extends BaseExceptionHandler {
    private List<String> criticalLog = new ArrayList<>();

    @Override
    public void handle(Transaction transaction) throws Exception {
        try {
            logger.info("Executing transaction: " + transaction.getTransactionId());

            // Симуляция возможной критической ошибки (например, сбой сети)
            if (transaction.getAccountId().equals("ACC-999999")) {
                throw new RuntimeException("Critical system error: Database connection lost");
            }

            // Успешное выполнение
            transaction.setStatus(TransactionStatus.COMPLETED);
            logger.info("Transaction completed successfully: " +
                    transaction.getTransactionId());

            passToNext(transaction);

        } catch (RuntimeException e) {
            // Логирование критической ошибки
            String criticalMessage = String.format(
                    "[CRITICAL] %s | Transaction: %s | Error: %s",
                    LocalDateTime.now(), transaction.getTransactionId(), e.getMessage()
            );

            criticalLog.add(criticalMessage);
            logger.severe(criticalMessage);

            transaction.setStatus(TransactionStatus.FAILED);
            throw new CriticalTransactionException(
                    "Critical error during transaction processing", e);
        }
    }

    public List<String> getCriticalLog() {
        return new ArrayList<>(criticalLog);
    }
}

// 4. Обработчик отката транзакций
class RollbackHandler extends BaseExceptionHandler {
    private List<Transaction> rolledBackTransactions = new ArrayList<>();

    @Override
    public void handle(Transaction transaction) throws Exception {
        // Этот обработчик вызывается только при ошибках
        // Он не должен быть в основной цепочке
    }

    public void rollback(Transaction transaction, Exception cause) {
        logger.warning(String.format(
                "Rolling back transaction %s due to: %s",
                transaction.getTransactionId(), cause.getMessage()
        ));

        transaction.setStatus(TransactionStatus.ROLLED_BACK);
        rolledBackTransactions.add(transaction);

        // Здесь может быть логика восстановления состояния
        logger.info("Rollback completed for: " + transaction.getTransactionId());
    }

    public List<Transaction> getRolledBackTransactions() {
        return new ArrayList<>(rolledBackTransactions);
    }
}

// Основной процессор транзакций
public class TransactionProcessor {
    private ExceptionHandler handlerChain;
    private RollbackHandler rollbackHandler;
    private CriticalErrorHandler criticalErrorHandler;

    public TransactionProcessor() {
        // Построение цепочки обработчиков
        ValidationHandler validationHandler = new ValidationHandler();
        BusinessLogicHandler businessLogicHandler = new BusinessLogicHandler();
        criticalErrorHandler = new CriticalErrorHandler();

        validationHandler.setNext(businessLogicHandler);
        businessLogicHandler.setNext(criticalErrorHandler);

        this.handlerChain = validationHandler;
        this.rollbackHandler = new RollbackHandler();
    }

    public void processTransaction(Transaction transaction) {
        System.out.println("\n=== Processing Transaction ===");
        System.out.println(transaction);

        try {
            // Запуск цепочки обработки
            handlerChain.handle(transaction);

            System.out.println("✓ Transaction successful: " + transaction);

        } catch (ValidationException e) {
            System.err.println("✗ Validation Error: " + e.getMessage());
            rollbackHandler.rollback(transaction, e);

        } catch (AccountNotFoundException e) {
            System.err.println("✗ Account Error: " + e.getMessage());
            rollbackHandler.rollback(transaction, e);

        } catch (InsufficientFundsException e) {
            System.err.println("✗ Insufficient Funds: " + e.getMessage());
            rollbackHandler.rollback(transaction, e);

        } catch (CriticalTransactionException e) {
            System.err.println("✗ CRITICAL ERROR: " + e.getMessage());
            System.err.println("  Caused by: " + e.getCause().getMessage());
            rollbackHandler.rollback(transaction, e);

        } catch (Exception e) {
            System.err.println("✗ Unexpected Error: " + e.getMessage());
            rollbackHandler.rollback(transaction, e);
        }
    }

    public List<String> getCriticalLog() {
        return criticalErrorHandler.getCriticalLog();
    }

    public List<Transaction> getRolledBackTransactions() {
        return rollbackHandler.getRolledBackTransactions();
    }

    // Демонстрация работы системы
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();

        // Тестовые сценарии
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Transaction Processing System Demo   ║");
        System.out.println("╚════════════════════════════════════════╝");

        // 1. Успешная транзакция
        processor.processTransaction(new Transaction(500.0, "ACC-123456"));

        // 2. Ошибка валидации - слишком малая сумма
        processor.processTransaction(new Transaction(0.001, "ACC-123456"));

        // 3. Ошибка валидации - неверный формат счета
        processor.processTransaction(new Transaction(100.0, "INVALID"));

        // 4. Счет не найден
        processor.processTransaction(new Transaction(200.0, "ACC-000000"));

        // 5. Недостаточно средств
        processor.processTransaction(new Transaction(500.0, "ACC-111111"));

        // 6. Критическая ошибка
        processor.processTransaction(new Transaction(1000.0, "ACC-999999"));

        // Вывод статистики
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           Statistics                   ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Transaction> rolledBack = processor.getRolledBackTransactions();
        System.out.println("\nRolled back transactions: " + rolledBack.size());
        for (Transaction t : rolledBack) {
            System.out.println("  - " + t);
        }

        List<String> criticalLogs = processor.getCriticalLog();
        if (!criticalLogs.isEmpty()) {
            System.out.println("\nCritical errors logged: " + criticalLogs.size());
            for (String log : criticalLogs) {
                System.out.println("  " + log);
            }
        }
    }
}