package com.z;


import com.z.service.AWSCustomRuntime;
import com.z.service.HandlerImpl;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new AWSCustomRuntime<>(new HandlerImpl());
    }
}
