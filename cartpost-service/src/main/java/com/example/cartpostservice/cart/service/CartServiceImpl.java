package com.example.cartpostservice.cart.service;

import com.example.cartpostservice.cart.service.dto.response.CartItemDeleteResult;
import com.example.cartpostservice.cart.service.dto.response.CartItemGetResult;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements  CartService {

    @Override
    public CartItemGetResult getCartItems(String xCode) {
        return null;
    }

    @Override
    public CartItemDeleteResult deleteCartItems(String xCode, String itemCode) {
        return null;
    }
}
