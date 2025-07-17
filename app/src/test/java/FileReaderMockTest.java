
import org.example.fileservice.FileReader;
import org.example.fileservice.LineParser;
import org.example.model.Subscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileReaderMockTest {

    @Mock
    private LineParser lineParser;

    @InjectMocks
    private FileReader fileReader;

    @Test
    public void testReadWithMockParser() {
        // Настройка мока
        Subscriber mockSubscriber = new Subscriber("Test", "Area", "Address", 1, Collections.emptyList());
        when(lineParser.parse(anyString())).thenReturn(mockSubscriber);

        // Вызов тестируемого метода
        List<Subscriber> result = fileReader.read("test_path.txt");

        // Проверки
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockSubscriber, result.get(0));

        // Проверка вызова парсера
        verify(lineParser, times(1)).parse(anyString());
    }

    @Test
    public void testReadWithParserError() {
        // Настройка мока для выброса исключения
        when(lineParser.parse(anyString())).thenThrow(new IllegalArgumentException("Test error"));

        // Вызов тестируемого метода
        List<Subscriber> result = fileReader.read("test_path.txt");

        // Проверки
        assertTrue(result.isEmpty());
        verify(lineParser, times(1)).parse(anyString());
    }
}