package org.narcissus.narcissuscoreservice.services.order;

import java.util.Date;
import java.util.List;

import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.model.order.Orders;
import org.narcissus.narcissuscoreservice.repository.OrderRepository;
import org.narcissus.narcissuscoreservice.utils.NullFieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ErrorHandler errorHandler;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(ErrorHandler errorHandler, OrderRepository orderRepository) {
        this.errorHandler = errorHandler;
        this.orderRepository = orderRepository;
    }

    public ResponsePayload createOrder (Orders request) {
        try {
            request.setDate(new Date());

            return save(request, "createOrder OrderService");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "createOrder OrderService");
        }

    }

    public ResponsePayload updateOrder(Orders request) {
        try {
            Orders order = findOrders(request.getOrdersId());
            if (order != null) {
                if (request.getMoney() > 0) order.setMoney(request.getMoney());
                if (request.getAddress() != null) order.setAddress(request.getAddress());
                if (request.getStatus() != null) order.setStatus(request.getStatus());
                if (request.getShipped() != null) order.setShipped(request.getShipped());
                if (request.getCancellationReason() != null) order.setCancellationReason(request.getCancellationReason());
                if (request.getCanceledAt() != null) order.setCanceledAt(request.getCanceledAt());
                if (request.getTransactionDateTime() != null) order.setTransactionDateTime(request.getTransactionDateTime());
                return save(order, "updateOrder OrderService");
            } else return errorHandler.error("Cannot find order with ID: "+request.getOrdersId(), "updateOrder OrderService");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "updateOrder OrderService");
        }
    }

    private Orders findOrders(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    private ResponsePayload save(Orders orders, String function ) {
        List<String> nullList = NullFieldChecker.check(orders,
         "transactionDateTime",
          "cancellationReason",
          "canceledAt",
          "shipped",
          "ordersId");
        if (nullList.isEmpty()) {
            orderRepository.save(orders);
            return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.OK).build();
        } else return errorHandler.error(nullList.toString()+"is null", "createProduct ProductService");
    }
}
