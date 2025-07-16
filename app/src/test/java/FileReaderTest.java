import org.example.fileservice.FileReader;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileReaderTest {

    private final FileReader fileReader = new FileReader();

    @Test
    public void testUnCorrectPath() {

        assertTrue(fileReader.read("abc").isEmpty());
    }
}