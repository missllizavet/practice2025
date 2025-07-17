

import org.example.app.SubsApplication;
import org.example.fileservice.FileReader;
import org.example.model.Month;
import org.example.model.Subscriber;
import org.example.service.Service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubsApplicationMockTest {

    @Mock
    private FileReader fileReader;

    @Mock
    private Service service;

    @InjectMocks
    private SubsApplication subsApplication;

    private List<Subscriber> testSubscribers;

    @Before
    public void setUp() {
        testSubscribers = Collections.emptyList();
        when(fileReader.read(anyString())).thenReturn(testSubscribers);
    }

    @Test
    public void testMainWithEmptyData() {
        // Подготовка
        when(fileReader.read(anyString())).thenReturn(Collections.emptyList());

        // Вызов
        SubsApplication.main(new String[]{});

        // Проверка что метод read был вызван
        verify(fileReader, times(1)).read(anyString());
    }

    @Test
    public void testPublicationsCountThroughMainMenu() {
        // Подготовка
        when(fileReader.read(anyString())).thenReturn(testSubscribers);
        when(service.getSubscriptionCountByMonthAndTitle(
                anyList(), anyString(), any(Month.class)))
                .thenReturn(5L);

        // Эмулируем пользовательский ввод:
        // 1 - выбор пункта меню "Найти количество экземпляров"
        // Test Publication - название издания
        // 1 - выбор месяца (январь)
        // 7 - выход
        String input = "1\nTest Publication\n1\n7\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Вызов
        SubsApplication.main(new String[]{});

        // Проверка
        verify(service, times(1)).getSubscriptionCountByMonthAndTitle(
                anyList(), eq("Test Publication"), any(Month.class));
    }
}