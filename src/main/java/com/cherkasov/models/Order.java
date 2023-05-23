package com.cherkasov.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private OrderType orderType;
    private int price;
    private int size;

}
