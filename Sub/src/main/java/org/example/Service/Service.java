package org.example.Service;

import org.example.Model.Month;
import org.example.Model.Publication;
import org.example.Model.PublicationType;
import org.example.Model.Subscriber;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Service {

    public List<Publication> getPublicationsBySubscriberName(List<Subscriber> subs, String fullName) {
        return subs.stream()
                .filter(sub -> sub.fullName().equals(fullName))
                .flatMap(sub -> sub.publications().stream())
                .collect(Collectors.toList());
    }

    public Optional<String> getAreaWithMostSubscriptions(List<Subscriber> subs, String title, Month month) {
        return subs.stream()
                .collect(Collectors.groupingBy(
                        Subscriber::deliveryArea,
                        Collectors.summingInt(sub -> (int) sub.publications().stream()
                                .filter(pub -> pub.title().equals(title) && pub.isInMonth(month))
                                .count()))
                )
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    public long getSubscriptionCountByMonthAndTitle(List<Subscriber> subs, String title, Month month) {
        return subs.stream()
                .flatMap(sub -> sub.publications().stream())
                .filter(pub -> pub.title().equals(title) && pub.isInMonth(month))
                .count();
    }

    // Новый метод: получение подписок, истекающих в указанном месяце
    public List<Publication> getExpiringSubscriptions(List<Subscriber> subs, Month month) {
        return subs.stream()
                .flatMap(sub -> sub.publications().stream())
                .filter(pub -> pub.endDate().getMonthValue() == month.getNumber())
                .collect(Collectors.toList());
    }

    // Новый метод: получение подписчиков по типу издания
    public List<Subscriber> getSubscribersByPublicationType(List<Subscriber> subs, PublicationType type) {
        return subs.stream()
                .filter(sub -> sub.publications().stream()
                        .anyMatch(pub -> pub.type() == type))
                .collect(Collectors.toList());
    }

    // Новый метод: статистика по районам доставки
    public Map<String, Long> getDeliveryAreaStatistics(List<Subscriber> subs) {
        return subs.stream()
                .collect(Collectors.groupingBy(
                        Subscriber::deliveryArea,
                        Collectors.counting()
                ));
    }
}