package com.example.cartpostservice.cart.service;

import com.example.cartpostservice.cart.repository.CartItemsRepository;
import com.example.cartpostservice.cart.repository.CartsRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartsRepository cartsRepository; // 가상의 Repository

    @Mock
    private CartItemsRepository cartItemsRepository;

    @InjectMocks
    private CartServiceImpl cartService; // CartService의 실제 구현체

    @Test
    void testGetCartItems() {
        // given
        String xCode = UUID.randomUUID().toString();

        // when

        // then

    }

    @Test
    void testDeleteCartItems() {
        // given
        String xCode = UUID.randomUUID().toString();
        String itemCode = UUID.randomUUID().toString();

        // when

        // then

    }
}


