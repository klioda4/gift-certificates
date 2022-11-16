package ru.clevertec.ecl.cluster.nodeInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Node {

    private final String address;
    private final boolean current;
    private final int shardIndex;

    @Builder.Default
    @Setter
    private volatile boolean isActive = true;
}
