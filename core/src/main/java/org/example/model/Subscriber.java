package org.example.model;


import java.util.List;

public record Subscriber(
        String fullName,
        String deliveryArea,
        String address,
        int numberOfSubscriptions,
        List<Publication> publications
) {
}
