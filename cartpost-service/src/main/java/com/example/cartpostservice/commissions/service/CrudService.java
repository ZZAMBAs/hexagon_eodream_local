package com.example.cartpostservice.commissions.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface CrudService<Req, Res, code> {

    String create(Req requestDto);

    Res read(code code);

    void update(Req requestDto, code code);

    void delete(code userCode, code dataCode);
}