package aop.prototypes.singleTon.services;

import aop.prototypes.singleTon.domain.Order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
