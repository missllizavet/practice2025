import org.example.model.Month;
import org.example.model.Publication;
import org.example.model.PublicationType;
import org.example.model.Subscriber;
import org.example.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private Service service;
    private List<Subscriber> subscribers;

    @BeforeEach
    void setUp() {
        service = new Service();
        subscribers = getSubscribers();
    }

    @Test
    void testGetSubscriptionCountByMonthAndTitle() {
        long count = service.getSubscriptionCountByMonthAndTitle(subscribers, "Library Journal", Month.JULY);
        assertEquals(1, count);

        count = service.getSubscriptionCountByMonthAndTitle(subscribers, "Daily News", Month.JULY);
        assertEquals(4, count);
    }

    @Test
    void testGetPublicationsBySubscriberName() {
        List<Publication> expectedAlice = List.of(
                new Publication("Library Journal", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                new Publication("a", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                new Publication("b", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)));

        List<Publication> publicationsAlice = service.getPublicationsBySubscriberName(subscribers, "Alice");
        assertEquals(expectedAlice.size(), publicationsAlice.size());
        assertTrue(publicationsAlice.containsAll(expectedAlice));

        List<Publication> expectedDavid = List.of(
                new Publication("Health Journal", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                new Publication("Fashion Weekly", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)));

        List<Publication> publicationsDavid = service.getPublicationsBySubscriberName(subscribers, "David");
        assertEquals(expectedDavid.size(), publicationsDavid.size());
        assertTrue(publicationsDavid.containsAll(expectedDavid));
    }

    @Test
    void testGetAreaWithMostSubscriptions() {
        Optional<String> area = service.getAreaWithMostSubscriptions(subscribers, "Fashion Weekly", Month.JULY);
        assertTrue(area.isPresent());
        assertEquals("East", area.get());

        area = service.getAreaWithMostSubscriptions(subscribers, "Health Journal", Month.JULY);
        assertTrue(area.isPresent());
        assertEquals("South", area.get());

        Optional<String> nullArea = service.getAreaWithMostSubscriptions(subscribers, "null", Month.JULY);
        assertTrue(nullArea.isEmpty());
    }

    private List<Subscriber> getSubscribers() {
        return List.of(
                new Subscriber("Alice", "Central", "123 Main St", 2, List.of(
                        new Publication("Library Journal", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("Alice", "Central", "123 Main St", 2, List.of(
                        new Publication("a", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("b", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("Bob", "North", "456 Elm St", 1, List.of(
                        new Publication("Fashion Weekly", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("David", "South", "789 Oak St", 3, List.of(
                        new Publication("Health Journal", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("Fashion Weekly", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("Eve", "East", "101 Pine St", 2, List.of(
                        new Publication("Fashion Weekly", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("Morning Herald", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("John", "East", "101 Pine St", 1, List.of(
                        new Publication("Fashion Weekly", PublicationType.MAGAZINE, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31))
                )),
                new Subscriber("John", "West", "202 Cedar St", 2, List.of(
                        new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 12, 31)),
                        new Publication("Daily News", PublicationType.NEWSPAPER, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 12, 31))
                ))
        );
    }
}