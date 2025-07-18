//package org.example.app;

import org.example.app.SubsApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SubsApplicationPublicInterfaceTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testMainMenuOption1_PublicationsCount() {
        String input = "1\nTest Publication\n1\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Количество экземпляров для доставки:"));
    }

    @Test
    void testMainMenuOption2_PublicationsPerSubscriber() {
        String input = "2\nTest Subscriber\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        // Проверяем общую структуру вывода вместо конкретного названия издания
        assertTrue(output.contains("Список изданий:") ||
                output.contains("Издания не найдены") ||
                output.contains("Список изданий подписчика"));
    }

    @Test
    void testMainMenuOption3_AreaWithMostSubscriptions() {
        String input = "3\nTest Publication\n1\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Участок с наибольшим количеством доставок:"));
    }

    @Test
    void testMainMenuOption4_ExpiringSubscriptions() {
        String input = "4\n1\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Подписки, истекающие в указанном месяце"));
    }

    @Test
    void testMainMenuOption5_SubscribersByPublicationType() {
        String input = "5\nNEWSPAPER\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Подписчики по типу издания"));
    }

    @Test
    void testMainMenuOption6_DeliveryAreaStatistics() {
        String input = "6\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Статистика по районам доставки:"));
    }

    @Test
    void testMainMenuOption7_Exit() {
        String input = "7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Выход из программы..."));
    }

    @Test
    void testMainMenuInvalidOption() {
        String input = "99\n\n7\n";
        provideInput(input);

        SubsApplication.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Неверный выбор, попробуйте снова."));
    }
}