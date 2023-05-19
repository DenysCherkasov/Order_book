package com.cherkasov.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private String id;
    private OrderType orderType;
    private int price;
    private int size;

}
