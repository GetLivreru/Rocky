package exceptions;

import java.util.logging.Logger;


public class TransactionProcessor {
    // Создаем статический логгер для класса TransactionProcessor, мы изучим его далее.
    // Это очень простая штука, которая помогает категоризировать наши сообщения в логах (например в консоли) вместо простого System.out.
    // Используем для записи логов различных уровней важности
    private static final Logger logger = Logger.getLogger(TransactionProcessor.class);

    // Метод обработки транзакции с возможностью выбрасывания двух типов исключений
    // Декларативно указываем, что метод может бросить ValidationException или InsufficientFundsException
    public void processTransaction(Transaction transaction)
            throws ValidationException, InsufficientFundsException {

        try {
            // 1. Первый этап - валидация транзакции
            // Проверяем корректность данных перед выполнением
            validateTransaction(transaction);

            // 2. Обработка платежа
            // Выполняем непосредственно транзакцию
            processPayment(transaction);

            // 3. Завершение транзакции
            // Фиксируем успешное выполнение операции
            completeTransaction(transaction);

        } catch (ValidationException e) {
            // Перехватываем и логируем ошибку валидации
            // Используем warning - это промежуточный уровень важности
            logger.warning("Ошибка валидации: " + e.getMessage());

            // Пробрасываем исключение выше по стеку
            // Позволяем вышестоящему коду принять решение об обработке
            throw e;

        } catch (InsufficientFundsException e) {
            // Логируем критическую ошибку нехватки средств
            // Используем severe - высокий уровень важности
            logger.severe("Недостаточно средств: " + e.getMessage());

            // Откатываем транзакцию при нехватке средств
            rollbackTransaction(transaction);

            // Пробрасываем исключение для внешней обработки
            throw e;

        } catch (Exception e) {
            // Перехватываем любые неожиданные исключения
            // Используем error - максимальный уровень важности
            logger.error("Неизвестная ошибка: " + e.getMessage());

            // Откатываем транзакцию при любой неизвестной ошибке
            rollbackTransaction(transaction);
        }
    }

    // Приватный метод валидации транзакции
    // Может выбросить ValidationException при некорректных данных
    private void validateTransaction(Transaction t) throws ValidationException {
        // Проверяем, что сумма транзакции положительная
        // Если сумма <= 0, генерируем исключение
        if (t.getAmount() <= 0) {
            throw new ValidationException("Некорректная сумма транзакции");
        }
    }
}

// Кастомное исключение для ошибок валидации
// Наследуется от Exception, значит это checked exception
class ValidationException extends Exception {
    // Конструктор с возможностью передать описание ошибки
    public ValidationException(String message) {
        // Вызываем конструктор родительского класса с сообщением
        super(message);
    }
}

// Кастомное исключение для ситуации нехватки средств
// Также является checked exception
class InsufficientFundsException extends Exception {
    // Конструктор с возможностью передать описание ошибки
    public InsufficientFundsException(String message) {
        // Вызываем конструктор родительского класса с сообщением
        super(message);
    }
}


class Transaction {
    private double amount;
    private String accountId;

    // Конструктор, геттеры
}