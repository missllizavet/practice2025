import org.example.fileservice.FileReader;
import org.example.model.Subscriber;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileReaderTest {
    private final FileReader fileReader = new FileReader();

    @Test
    public void testUnCorrectPath() {
        assertTrue(fileReader.read("nonexistent_file.txt").isEmpty());
    }

    @Test
    public void testReadValidFile() throws Exception {
        // Строго соответствуем формату, который ожидает LineParser
        String validData = "John Doe,Central,123 Main St,2,Title1;NEWSPAPER;2024-01-01;2024-12-31\n" +
                "Jane Smith,North,456 Oak St,1,Title2;MAGAZINE;2024-01-01;2024-06-30";

        Path tempFile = Files.createTempFile("valid_test", ".txt");
        Files.write(tempFile, validData.getBytes());

        List<Subscriber> result = fileReader.read(tempFile.toString());
        assertFalse("Result should not be empty", result.isEmpty());
        assertEquals("Should parse 2 subscribers", 2, result.size());

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testReadFileWithInvalidLines() throws Exception {
        // Первая строка - валидная, вторая - невалидная
        String mixedData = "Alice Brown,South,789 Pine St,1,News;NEWSPAPER;2024-01-01;2024-12-31\n" +
                "Invalid,Data,Here,123";

        Path tempFile = Files.createTempFile("mixed_test", ".txt");
        Files.write(tempFile, mixedData.getBytes());

        List<Subscriber> result = fileReader.read(tempFile.toString());
        assertFalse("Result should not be empty", result.isEmpty());
        assertEquals("Should parse only 1 valid subscriber", 1, result.size());

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testEmptyFile() throws Exception {
        Path tempFile = Files.createTempFile("empty_test", ".txt");
        Files.write(tempFile, new byte[0]);

        List<Subscriber> result = fileReader.read(tempFile.toString());
        assertTrue("Result should be empty for empty file", result.isEmpty());

        Files.deleteIfExists(tempFile);
    }

}