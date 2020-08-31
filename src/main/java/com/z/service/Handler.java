package com.z.service;


import com.z.domain.Context;

public interface Handler<IN, OUT> {
    OUT handleRequest(IN input, Context context);
    Class<IN> getInClass();
}
