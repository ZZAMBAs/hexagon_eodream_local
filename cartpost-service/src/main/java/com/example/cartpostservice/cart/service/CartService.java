package com.example.cartpostservice.cart.service;

import com.example.cartpostservice.cart.service.dto.response.CartItemDeleteResult;
import com.example.cartpostservice.cart.service.dto.response.CartItemGetResult;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    public CartItemGetResult getCartItems(String xCode);

    public CartItemDeleteResult deleteCartItems(String xCode, String itemCode);
}
