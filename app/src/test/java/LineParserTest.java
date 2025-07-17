import org.example.fileservice.LineParser;
import org.example.model.Subscriber;
import org.junit.Test;
import static org.junit.Assert.*;

public class LineParserTest {
    private final LineParser parser = new LineParser();

    @Test
    public void testParseValidLine() {
        // Изменяем адрес, чтобы он точно соответствовал шаблону "123 Main St"
        String validLine = "John Doe,Central,123 Main St,2,Title;NEWSPAPER;2024-01-01;2024-12-31";
        Subscriber subscriber = parser.parse(validLine);

        assertEquals("John Doe", subscriber.fullName());
        assertEquals("Central", subscriber.deliveryArea());
        assertEquals("123 Main St", subscriber.address());
        assertEquals(2, subscriber.numberOfSubscriptions()); // Исправлено на 2 (как в тестовых данных)
        assertEquals(1, subscriber.publications().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidPublicationType() {
        String invalidLine = "John Doe,Central,123 Main St,2,Title;INVALID_TYPE;2024-01-01;2024-12-31";
        parser.parse(invalidLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDateFormat() {
        String invalidLine = "John Doe,Central,123 Main St,2,Title;NEWSPAPER;2024/01/01;2024-12-31";
        parser.parse(invalidLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIncompleteLine() {
        String invalidLine = "John Doe,Central,123 Main St"; // Неполная строка
        parser.parse(invalidLine);
    }

    // Добавляем новый тест для проверки нескольких публикаций
    @Test
    public void testParseMultiplePublications() {
        String validLine = "Jane Smith,North,456 Oak St,3,Mag1;MAGAZINE;2024-01-01;2024-06-30;News1;NEWSPAPER;2024-01-01;2024-12-31";
        Subscriber subscriber = parser.parse(validLine);

        assertEquals(2, subscriber.publications().size());
        assertEquals(3, subscriber.numberOfSubscriptions());
    }

    @Test
    public void testDifferentAddressFormats() {
        String line1 = "John Doe,Central,123 Main St,1,Title;NEWSPAPER;2024-01-01;2024-12-31";
        String line2 = "Jane Smith,North,456 Oak Ave,1,Title;MAGAZINE;2024-01-01;2024-06-30";

        assertNotNull(parser.parse(line1));
        assertNotNull(parser.parse(line2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyLine() {
        parser.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullLine() {
        parser.parse(null);
    }

    @Test
    public void testParseWithMultipleSpaces() {
        String line = "John  Doe,  Central,  123 Main  St,  2,  Title;  NEWSPAPER;  2024-01-01;  2024-12-31";
        Subscriber subscriber = parser.parse(line);
        assertEquals("John Doe", subscriber.fullName());
        assertEquals("Central", subscriber.deliveryArea());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidNumberOfSubscriptions() {
        String line = "John Doe,Central,123 Main St,ABC,Title;NEWSPAPER;2024-01-01;2024-12-31";
        parser.parse(line);
    }

    @Test
    public void testParseWithMinimalData() {
        String line = "A B,C,1 D St,1,E;NEWSPAPER;2024-01-01;2024-01-02";
        Subscriber subscriber = parser.parse(line);
        assertNotNull(subscriber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingPublicationParts() {
        String line = "John Doe,Central,123 Main St,2,Title;NEWSPAPER;2024-01-01";
        parser.parse(line);
    }

    @Test
    public void testParseWithMultiplePublications() {
        String line = "John Doe,Central,123 Main St,3,Pub1;NEWSPAPER;2024-01-01;2024-12-31;Pub2;MAGAZINE;2024-01-01;2024-06-30";
        Subscriber subscriber = parser.parse(line);
        assertEquals(2, subscriber.publications().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidDateRange() {
        String line = "John Doe,Central,123 Main St,1,Title;NEWSPAPER;2024-12-31;2024-01-01";
        parser.parse(line);
    }
}