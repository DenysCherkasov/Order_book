package com.cherkasov.repository;

import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Repository {
    private final List<Order> orderRepository = new ArrayList<>();

    public void save(Order newOrder) {
        orderRepository.stream()
                .filter(savedOrder -> savedOrder.getPrice() == newOrder.getPrice())
                .findAny()
                .ifPresentOrElse(
                        savedOrder -> savedOrder.setSize(newOrder.getSize()),
                        () -> orderRepository.add(newOrder));
    }

    final Comparator<Order> ascPriceComparator
            = Comparator.comparingInt(Order::getPrice);

    public void removeSizeInCheapestAsk(int size) {
        List<Order> sortedOrders = orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.ASK)
                .sorted(ascPriceComparator)
                .collect(Collectors.toList());
        removeSizes(size, sortedOrders);
    }

    final Comparator<Order> descPriceComparator
            = Comparator.comparingInt(Order::getPrice).reversed();

    public void removeSizeInMostExpensiveBid(int size) {
        List<Order> sortedOrders = orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.BID)
                .sorted(descPriceComparator)
                .collect(Collectors.toList());
        removeSizes(size, sortedOrders);
    }

    private void removeSizes(int size, List<Order> sortedOrders) {
        for (Order order : sortedOrders) {
            if (size <= order.getSize()) {
                order.setSize(order.getSize() - size);
                break;
            } else {
                size -= order.getSize();
                order.setSize(0);
            }
        }
    }

    public Optional<Order> getBestBidPriceSize() {
        return orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.BID && order.getSize() != 0)
                .max(ascPriceComparator);
    }

    public Optional<Order> getBestAskPriceSize() {
        return orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.ASK && order.getSize() != 0)
                .max(descPriceComparator);
    }


    public Optional<Integer> getSizeByPrice(int price) {
        return orderRepository.stream()
                .filter(order -> order.getPrice() == price)
                .map(Order::getSize)
                .findAny();
    }
}
