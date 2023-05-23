package com.cherkasov.service;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.models.Order;
import com.cherkasov.models.OrderType;
import com.cherkasov.repository.Repository;

public class Service {
    private final Repository repository;
    private final OutputFileWriter outputFileWriter;

    public Service(Repository repository, OutputFileWriter outputFileWriter) {
        this.repository = repository;
        this.outputFileWriter = outputFileWriter;
    }

    public void saveOrder(String line) {
        String[] infoForOrder = line.split(",");
        Order order = new Order(createOrderTypeByInfo(infoForOrder[3]),
                Integer.parseInt(infoForOrder[1]), Integer.parseInt(infoForOrder[2]));
        repository.save(order);
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
            writeBestBidPriceAndSize();
        } else if (info.length == 2 && info[1].equals("best_ask")) {
            writeBestAskPriceAndSize();
        } else {
            writeSizeByPrice(Integer.parseInt(info[2]));
        }
    }

    private void writeBestAskPriceAndSize() {
        repository.getBestAskPriceSize()
                .ifPresent(order ->
                        outputFileWriter.writeDataToFile(String.format("%d,%d\n",
                                order.getPrice(), order.getSize())));
    }

    private void writeBestBidPriceAndSize() {
        repository.getBestBidPriceSize()
                .ifPresent(order ->
                        outputFileWriter.writeDataToFile(String.format("%d,%d\n",
                                order.getPrice(), order.getSize())));
    }

    private void writeSizeByPrice(int price) {
        repository.getSizeByPrice(price)
                .ifPresentOrElse(
                        size -> outputFileWriter.writeDataToFile(String.format("%d\n", size)),
                        () -> outputFileWriter.writeDataToFile(String.format("%d\n", 0)));
    }


}
