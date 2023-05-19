package com.cherkasov.service;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;
import com.cherkasov.repository.Repository;

import java.util.UUID;

public class Service {
    private final Repository repository;
    private final OutputFileWriter outputFileWriter;

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
        order.setPrice(Integer.parseInt(infoForOrder[1]));
        order.setSize(Integer.parseInt(infoForOrder[2]));
        order.setOrderType(createOrderTypeByInfo(infoForOrder[3]));
        return order;
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
        Order order = repository.getBestPriceSizeByType(orderType);
        outputFileWriter.writeDataToFile(String.format("%d,%d%n", order.getPrice(), order.getSize()));
    }

    private void writeSizeByPrice(int price) {
        repository.getSizeByPrice(price)
                .ifPresent(order -> outputFileWriter.writeDataToFile(String.format("%d%n", order.getSize())));
    }

}
