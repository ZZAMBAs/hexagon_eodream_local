package com.example.cartpostservice.cart.controller;


import com.example.cartpostservice.cart.controller.dto.response.CartItemsGetResponse;
import com.example.cartpostservice.common.dto.EmptyResponse;
import com.example.cartpostservice.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController implements CartApi {


    @Override
    @GetMapping("/items")
    public ResponseEntity<ResponseDto<CartItemsGetResponse>> getCartItems(@RequestHeader(name = "X-CODE") String xCode) {
        return null;
    }

    @Override
    @DeleteMapping("/items/{item-code}")
    public ResponseEntity<ResponseDto<EmptyResponse>> deleteCartItem(@RequestHeader(name = "X-CODE") String xCode,
            @PathVariable String itemCode) {
        
        return null;
    }
}

