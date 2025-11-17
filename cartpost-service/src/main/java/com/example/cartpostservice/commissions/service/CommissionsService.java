package com.example.cartpostservice.commissions.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CommissionsService implements CrudService {

    @Override
    public void save(Object requestDto) {

    }

    @Override
    public Optional read(Object o) {
        return Optional.empty();
    }

    @Override
    public void delete(Object o) {

    }
}