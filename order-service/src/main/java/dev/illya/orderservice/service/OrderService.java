package dev.illya.orderservice.service;

import dev.illya.orderservice.dto.InventoryResponseDto;
import dev.illya.orderservice.dto.OrderLineItemsDto;
import dev.illya.orderservice.dto.OrderRequestDto;
import dev.illya.orderservice.model.Order;
import dev.illya.orderservice.model.OrderLineItems;
import dev.illya.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDto.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call Inventory Service, and place order if product is in stock.
//        InventoryResponseDto[] inventoryResponseArray = webClient.get()
//                .uri("http://localhost:9092/api/inventory",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
//                                .build() )
//                .retrieve()
//                .bodyToMono(InventoryResponseDto[].class)
//                .block();
        InventoryResponseDto[] inventoryResponseArray = webClientBuilder
                .build()
                .get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
                                .build() )
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();

        boolean allProductsInStosk = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponseDto::isInStock);

        if (allProductsInStosk) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in the stock. PLease try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
