package com.cherkasov.repository;

import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RepositoryTest {

    private Repository target;
    private List<Order> orderRepository;

    @BeforeEach
    void setUp() {
        target = new Repository();
        orderRepository = new ArrayList<>();
    }

    @Test
    void save_ShouldAddOrderToOrderRepository() {
        Order order = mock(Order.class);
        target.save(order);
        orderRepository.add(order);

        assertEquals(1, orderRepository.size());
        assertEquals(order, orderRepository.get(0));
    }

    @Test
    void removeSizeInCheapestAsk_ShouldUpdateSizeOfCheapestAskOrder() {
        Order askOrder = mock(Order.class);
        when(askOrder.getOrderType()).thenReturn(OrderType.ASK);
        when(askOrder.getSize()).thenReturn(10);
        orderRepository.add(askOrder);

        target.removeSizeInCheapestAsk(5);
        orderRepository.forEach(order -> order.setSize(10 - 5));

        verify(askOrder).setSize(5);
        Assertions.assertDoesNotThrow(() -> target.removeSizeInCheapestAsk(5));
    }

    @Test
    void removeSizeInCheapestAsk_ShouldNotUpdateSizeIfNewSizeIsNegative() {
        Order askOrder = mock(Order.class);
        when(askOrder.getOrderType()).thenReturn(OrderType.ASK);
        when(askOrder.getSize()).thenReturn(5);
        orderRepository.add(askOrder);

        target.removeSizeInCheapestAsk(10);
        orderRepository.forEach(order -> order.setSize(0));

        verify(askOrder).setSize(0);
    }

    @Test
    void removeSizeInMostExpensiveBid_ShouldUpdateSizeOfMostExpensiveBidOrder() {
        Order bidOrder = mock(Order.class);
        when(bidOrder.getOrderType()).thenReturn(OrderType.BID);
        when(bidOrder.getSize()).thenReturn(10);
        orderRepository.add(bidOrder);

        target.removeSizeInMostExpensiveBid(5);
        orderRepository.forEach(order -> order.setSize(10 - 5));

        verify(bidOrder).setSize(5);
        Assertions.assertDoesNotThrow(() -> target.removeSizeInMostExpensiveBid(5));
    }

    @Test
    void removeSizeInMostExpensiveBid_ShouldNotUpdateSizeIfNewSizeIsNegative() {
        Order bidOrder = mock(Order.class);
        when(bidOrder.getOrderType()).thenReturn(OrderType.BID);
        when(bidOrder.getSize()).thenReturn(5);
        orderRepository.add(bidOrder);

        target.removeSizeInMostExpensiveBid(10);
        orderRepository.forEach(order -> order.setSize(0));

        verify(bidOrder).setSize(0);
    }

    @Test
    void getBestPriceSizeByType_ShouldReturnOptionalEmptyIfNoOrdersMatchType() {
        Optional<Order> result = target.getBestPriceSizeByType(OrderType.ASK);

        assertEquals(Optional.empty(), result);
        Assertions.assertDoesNotThrow(() -> target.getBestPriceSizeByType(OrderType.ASK));
    }

    @Test
    void getSizeByPrice_ShouldReturnOptionalEmptyIfNoOrderWithMatchingPrice() {
        Optional<Order> result = target.getSizeByPrice(10);

        assertEquals(Optional.empty(), result);
        Assertions.assertDoesNotThrow(() -> target.getSizeByPrice(10));
    }

}
