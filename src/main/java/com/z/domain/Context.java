package com.z.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class Context {
    private final Map<String, List<String>> headers;
}
