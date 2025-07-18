import org.example.fileservice.FileReader;
import org.example.fileservice.LineParser;
import org.example.model.Subscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
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
    public void testReadWithMockParser_SingleLine() {
        Subscriber mockSubscriber = new Subscriber("Test", "Area", "Address", 1, Collections.emptyList());
        when(lineParser.parse(anyString())).thenReturn(mockSubscriber);

        List<Subscriber> result = fileReader.read("test_path.txt");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockSubscriber, result.get(0));
        verify(lineParser, times(1)).parse(anyString());
    }

    @Test
    public void testReadWithMockParser_MultipleLines() {
        Subscriber sub1 = new Subscriber("Test1", "Area1", "Address1", 1, Collections.emptyList());
        Subscriber sub2 = new Subscriber("Test2", "Area2", "Address2", 2, Collections.emptyList());
        when(lineParser.parse(anyString())).thenReturn(sub1).thenReturn(sub2);

        List<Subscriber> result = fileReader.read("test_path.txt");

        assertEquals(2, result.size());
        assertTrue(result.contains(sub1));
        assertTrue(result.contains(sub2));
        verify(lineParser, times(2)).parse(anyString());
    }

    @Test
    public void testReadWithParserError() {
        when(lineParser.parse(anyString())).thenThrow(new IllegalArgumentException("Test error"));

        List<Subscriber> result = fileReader.read("test_path.txt");

        assertTrue(result.isEmpty());
        verify(lineParser, times(1)).parse(anyString());
    }

    @Test
    public void testReadWithEmptyFile() {
        when(lineParser.parse(anyString())).thenReturn(null);

        List<Subscriber> result = fileReader.read("empty_file.txt");

        assertTrue(result.isEmpty());
        verify(lineParser, times(1)).parse(anyString());
    }

    @Test
    public void testReadWithMixedContent() {
        Subscriber validSub = new Subscriber("Valid", "Area", "Address", 1, Collections.emptyList());
        when(lineParser.parse(anyString()))
                .thenReturn(validSub)
                .thenThrow(new IllegalArgumentException("Test error"))
                .thenReturn(null);

        List<Subscriber> result = fileReader.read("mixed_content.txt");

        assertEquals(1, result.size());
        assertEquals(validSub, result.get(0));
        verify(lineParser, times(3)).parse(anyString());
    }
}