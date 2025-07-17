import org.example.model.*;
import org.example.service.Service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceMockTest {

    @InjectMocks
    private Service service;

    private Subscriber mockSubscriber;
    private Publication mockPublication;

    @Before
    public void setUp() {
        mockPublication = new Publication(
                "Test Title",
                PublicationType.NEWSPAPER,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        mockSubscriber = new Subscriber(
                "Test Name",
                "Test Area",
                "Test Address",
                1,
                List.of(mockPublication));
    }

    @Test
    public void testGetPublicationsBySubscriberNameWithMock() {
        // Подготовка
        List<Subscriber> subscribers = List.of(mockSubscriber);

        // Вызов
        List<Publication> result = service.getPublicationsBySubscriberName(subscribers, "Test Name");

        // Проверка
        assertEquals(1, result.size());
        assertEquals(mockPublication, result.get(0));
    }

    @Test
    public void testGetAreaWithMostSubscriptionsWithMock() {
        // Подготовка
        List<Subscriber> subscribers = List.of(mockSubscriber);

        // Вызов
        Optional<String> result = service.getAreaWithMostSubscriptions(
                subscribers, "Test Title", Month.JANUARY);

        // Проверка
        assertTrue(result.isPresent());
        assertEquals("Test Area", result.get());
    }
}
