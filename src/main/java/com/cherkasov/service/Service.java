package com.cherkasov.service;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;
import com.cherkasov.repository.Repository;

import java.util.UUID;

public class Service {
    private final Repository repository;
    private final OutputFileWriter outputFileWriter;
    private static final int DEFAULT_PRICE = 1000000000;
    private static final int DEFAULT_SIZE = 100000000;


    public Service(Repository repository, OutputFileWriter outputFileWriter) {
        this.repository = repository;
        this.outputFileWriter = outputFileWriter;
    }

    public void saveOrder(String line) {
        String[] infoForOrder = line.split(",");
        Order order = createOrder(infoForOrder);
        repository.save(order);
    }

    private Order createOrder(String[] infoForOrder) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        int actualPrice = Integer.parseInt(infoForOrder[1]);
        order.setPrice(checkAndCreateValue(actualPrice, DEFAULT_PRICE));
        int actualSize = Integer.parseInt(infoForOrder[2]);
        order.setSize(checkAndCreateValue(actualSize, DEFAULT_SIZE));
        order.setOrderType(createOrderTypeByInfo(infoForOrder[3]));
        return order;
    }

    private int checkAndCreateValue(int actualValue, int defaultValue) {
        if (actualValue < 0) {
            return 0;
        } else if (actualValue > defaultValue) {
            return defaultValue;
        } else {
            return actualValue;
        }
    }

    private OrderType createOrderTypeByInfo(String line) {
        return switch (line) {
            case "ask" -> OrderType.ASK;
            case "bid" -> OrderType.BID;
            case "spread" -> OrderType.SPREAD;
            default -> null;
        };
    }

    public void updateOrderSize(String line) {
        String[] updateInfo = line.split(",");
        if (updateInfo[1].equals("buy")) {
            repository.removeSizeInCheapestAsk(Integer.parseInt(updateInfo[2]));
        }
        if (updateInfo[1].equals("sell")) {
            repository.removeSizeInMostExpensiveBid(Integer.parseInt(updateInfo[2]));
        }
    }

    public void writeInfoInFile(String line) {
        String[] info = line.split(",");
        if (info.length == 2 && info[1].equals("best_bid")) {
            writeBestPriceAndSizeByType(OrderType.BID);
        } else if (info.length == 2 && info[1].equals("best_ask")) {
            writeBestPriceAndSizeByType(OrderType.ASK);
        } else {
            writeSizeByPrice(Integer.parseInt(info[2]));
        }
    }

    private void writeBestPriceAndSizeByType(OrderType orderType) {
        repository.getBestPriceSizeByType(orderType)
                .ifPresent(order ->
                        outputFileWriter.writeDataToFile(String.format("%d,%d\n",
                                order.getPrice(), order.getSize())));
    }

    private void writeSizeByPrice(int price) {
        if (price >= 0 && price <= DEFAULT_PRICE)
            repository.getSizeByPrice(price)
                    .ifPresent(order -> outputFileWriter.writeDataToFile(String.format("%d\n", order.getSize())));
    }

}
