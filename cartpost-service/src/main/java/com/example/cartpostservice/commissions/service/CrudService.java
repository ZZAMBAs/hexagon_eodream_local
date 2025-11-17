package com.example.cartpostservice.commissions.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface CrudService<T, code> {

    public void save(T requestDto);

    public Optional<T> read(code code);

    public void delete(code code);
}