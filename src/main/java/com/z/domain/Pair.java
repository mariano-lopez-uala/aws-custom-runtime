package com.z.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Pair<T1, T2> {
    private final T1 t1;
    private final T2 t2;
}
