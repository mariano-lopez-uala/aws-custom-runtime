package com.z.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.z.domain.Context;
import com.z.domain.Pair;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AWSCustomRuntime<IN, OUT> {
    private final Handler<IN, OUT> handler;

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String RUNTIME_INVOCATION = "/2018-06-01/runtime/invocation";
    private static final String REQUEST_ID_HEADER = "lambda-runtime-aws-request-id";
    private static final String HOST_ENV_KEY = "AWS_LAMBDA_RUNTIME_API";

    public AWSCustomRuntime(Handler<IN, OUT> handler) throws IOException {
        this.handler = handler;
        process();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void process() throws IOException {
        while (true) {
            Pair<IN, Context> input = this.fetchContext();
            System.out.println("Input: "+input);
            String requestId = this.getRequestId(input.getT2().getHeaders());

            OUT response = handler.handleRequest(input.getT1(), input.getT2());
            System.out.println("Response: "+response);
            this.sendResponse(requestId, response);
        }
    }


    private Pair<IN, Context> fetchContext() throws IOException {
        try (Response response = client.newCall(this.buildRequest()).execute()) {
            this.printResponse(response);
            return this.buildPair(response);
        }
    }

    private Request buildRequest() {
        return new Request.Builder().url(this.getBaseUrl()+"/next").build();
    }

    private String getBaseUrl() {
        String AWS_LAMBDA_RUNTIME_API = System.getenv(HOST_ENV_KEY);
        return "http://"+AWS_LAMBDA_RUNTIME_API+ RUNTIME_INVOCATION;
    }

    private Pair<IN, Context> buildPair(Response response) throws IOException {
        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        return new Pair<>(
                objectMapper.readValue(inputStream, handler.getInClass()),
                new Context(response.headers().toMultimap()));
    }

    private String getRequestId(Map<String, List<String>> headers) {
        return headers.get(REQUEST_ID_HEADER).get(0);
    }

    private void sendResponse(String requestId, OUT response) throws IOException {
        RequestBody body = RequestBody.create(objectMapper.writeValueAsBytes(response), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(getBaseUrl()+"/"+requestId+"/response")
                .post(body)
                .build();
        try (Response httpResponse = client.newCall(request).execute()) {
            printResponse(httpResponse);
            String jsonResponse = Objects.requireNonNull(httpResponse.body()).string();
            System.out.println("Response: "+jsonResponse+" status: "+httpResponse.code());
        }
    }

    private void printResponse(Response response) {
        System.out.printf("[%d] %s%n", response.code(), response.request().url().toString());
    }
}
