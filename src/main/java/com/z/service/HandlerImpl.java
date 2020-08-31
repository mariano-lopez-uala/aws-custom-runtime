package com.z.service;

import com.z.domain.Context;
import com.z.dto.HelloRequest;
import com.z.dto.HelloResponse;

public class HandlerImpl implements Handler<HelloRequest, HelloResponse> {
    @Override
    public HelloResponse handleRequest(HelloRequest input, Context context) {
        return new HelloResponse("Hello there "+input.getName());
    }

    @Override
    public Class<HelloRequest> getInClass() {
        return HelloRequest.class;
    }
}
