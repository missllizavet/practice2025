import org.example.fileservice.LineParser;
import org.example.model.PublicationType;
import org.example.model.Subscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LineParserExtendedTest {

    private final LineParser parser = new LineParser();

    @Test
    public void testParseWithMultiplePublicationsDifferentTypes() {
        String line = "John Smith,North,123 Main St,2,News;NEWSPAPER;2024-01-01;2024-12-31;Mag;MAGAZINE;2024-01-01;2024-06-30";
        Subscriber subscriber = parser.parse(line);
        assertEquals(2, subscriber.publications().size());
        assertEquals(PublicationType.NEWSPAPER, subscriber.publications().get(0).type());
        assertEquals(PublicationType.MAGAZINE, subscriber.publications().get(1).type());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidNumberOfSubscriptionsMismatch() {
        String line = "John Smith,North,123 Main St,3,News;NEWSPAPER;2024-01-01;2024-12-31";
        parser.parse(line);
    }

    @Test
    public void testParseWithSpecialCharactersInTitle() {
        String line = "John Smith,North,123 Main St,1,News & Review;NEWSPAPER;2024-01-01;2024-12-31";
        Subscriber subscriber = parser.parse(line);
        assertEquals("News & Review", subscriber.publications().get(0).title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyLine() {
        parser.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidPublicationType() {
        String line = "John Smith,North,123 Main St,1,News;INVALID_TYPE;2024-01-01;2024-12-31";
        parser.parse(line);
    }

    @Test
    public void testParseWithSinglePublication() {
        String line = "John Smith,North,123 Main St,1,News;NEWSPAPER;2024-01-01;2024-12-31";
        Subscriber subscriber = parser.parse(line);
        assertEquals(1, subscriber.publications().size());
        assertEquals("News", subscriber.publications().get(0).title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidDateRange() {
        String line = "John Smith,North,123 Main St,1,News;NEWSPAPER;2024-12-31;2024-01-01";
        parser.parse(line);
    }
}