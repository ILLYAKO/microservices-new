package dev.illya.inventoryservice.service;

import dev.illya.inventoryservice.dto.InventoryResponseDto;
import dev.illya.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

//    @Transactional(readOnly = true)
//    public boolean isInStock(String skuCode){
//        return  inventoryRepository.findBySkuCode(skuCode).isPresent();
//    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> isInStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory ->
                        InventoryResponseDto.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                )
                .toList();
    }
}
