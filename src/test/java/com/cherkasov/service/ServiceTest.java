package com.cherkasov.service;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;
import com.cherkasov.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ServiceTest {
    @Mock
    private Repository repository;
    @Mock
    private OutputFileWriter outputFileWriter;
    @InjectMocks
    private Service target;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrder_ShouldSaveOrderInRepository() {
        String line = "order1,100,50,ask";

        target.saveOrder(line);

        verify(repository, times(1)).save(any(Order.class));
        Assertions.assertDoesNotThrow(() -> target.saveOrder(line));
    }


    @Test
    void updateOrderSize_WithBuy_ShouldRemoveSizeInCheapestAsk() {
        String line = "update,buy,10";

        target.updateOrderSize(line);

        verify(repository, times(1)).removeSizeInCheapestAsk(eq(10));
        verify(repository, never()).removeSizeInMostExpensiveBid(anyInt());
        Assertions.assertDoesNotThrow(() -> target.updateOrderSize(line));
    }

    @Test
    void updateOrderSize_WithSell_ShouldRemoveSizeInMostExpensiveBid() {
        String line = "update,sell,10";

        target.updateOrderSize(line);

        verify(repository, times(1)).removeSizeInMostExpensiveBid(eq(10));
        verify(repository, never()).removeSizeInCheapestAsk(anyInt());
        Assertions.assertDoesNotThrow(() -> target.updateOrderSize(line));
    }

    @Test
    void writeInfoInFile_WithBestBid_ShouldWriteBestPriceAndSizeByTypeBid() {
        String line = "q,best_bid";
        String lineToWriteInFile = String.format("%d,%d\n", 100, 50);
        Order order = new Order();
        order.setPrice(100);
        order.setSize(50);
        when(repository.getBestPriceSizeByType(OrderType.BID)).thenReturn(Optional.of(order));

        target.writeInfoInFile(line);

        verify(outputFileWriter, times(1)).writeDataToFile(lineToWriteInFile);
        Assertions.assertDoesNotThrow(() -> target.writeInfoInFile(line));
    }

    @Test
    void writeInfoInFile_WithBestAsk_ShouldWriteBestPriceAndSizeByTypeAsk() {
        String line = "q,best_ask";
        String lineToWriteInFile = String.format("%d,%d\n", 200, 100);
        Order order = new Order();
        order.setPrice(200);
        order.setSize(100);
        when(repository.getBestPriceSizeByType(OrderType.ASK)).thenReturn(Optional.of(order));

        target.writeInfoInFile(line);

        verify(outputFileWriter, times(1)).writeDataToFile(lineToWriteInFile);
        Assertions.assertDoesNotThrow(() -> target.writeInfoInFile(line));
    }

    @Test
    void writeInfoInFile_WithPrice_ShouldWriteSizeByPrice() {
        String line = "q,size,150";
        String lineToWriteInFile = String.format("%d\n", 75);
        Order order = new Order();
        order.setSize(75);
        when(repository.getSizeByPrice(150)).thenReturn(Optional.of(order));

        target.writeInfoInFile(line);

        verify(outputFileWriter, times(1)).writeDataToFile(lineToWriteInFile);
        Assertions.assertDoesNotThrow(() -> target.writeInfoInFile(line));

    }

}