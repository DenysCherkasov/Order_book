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

    public void save(Order order) {
        orderRepository.add(order);
    }

    final Comparator<Order> ascPriceComparator
            = Comparator.comparingInt(Order::getPrice);

    public void removeSizeInCheapestAsk(int size) {
        orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.ASK)
                .sorted(ascPriceComparator)
                .limit(1)
                .peek(order -> {
                    int newSize = order.getSize() - size;
                    if (newSize >= 0) {
                        order.setSize(order.getSize() - size);
                    } else {
                        order.setSize(0);
                    }
                })
                .collect(Collectors.toList());
    }

    final Comparator<Order> descPriceComparator
            = Comparator.comparingInt(Order::getPrice).reversed();

    public void removeSizeInMostExpensiveBid(int size) {
        orderRepository.stream()
                .filter(order -> order.getOrderType() == OrderType.BID)
                .sorted(descPriceComparator)
                .limit(1)
                .peek(order -> {
                    int newSize = order.getSize() - size;
                    order.setSize(Math.max(newSize, 0));
                })
                .collect(Collectors.toList());
    }

    public Order getBestPriceSizeByType(OrderType orderType) {
        List<Order> orderList = orderRepository.stream()
                .filter(order -> order.getOrderType() == orderType)
                .sorted(descPriceComparator)
                .limit(1)
                .collect(Collectors.toList());
        return orderList.get(0);
    }

    public Optional<Order> getSizeByPrice(int price) {
        return orderRepository.stream()
                .filter(order -> order.getPrice() == price)
                .findAny();
    }
}
