package dev.illya.orderservice.service;

import dev.illya.orderservice.dto.OrderLineItemsDto;
import dev.illya.orderservice.dto.OrderRequestDto;
import dev.illya.orderservice.model.Order;
import dev.illya.orderservice.model.OrderLineItems;
import dev.illya.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDto.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        // Call Inventory Service, and place order if product is in stock.
        Boolean result = webClient.get()
                .uri("http://localhost:9092/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        if (result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in the stock. PLease try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuality());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
