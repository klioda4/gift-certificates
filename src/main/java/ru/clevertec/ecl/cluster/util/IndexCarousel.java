package ru.clevertec.ecl.cluster.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IndexCarousel {

    private final int minIndex;
    private final int maxIndex;
    private final AtomicInteger nextIndex;

    public IndexCarousel(int minIndex, int maxIndex, int nextIndex) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.nextIndex = new AtomicInteger(nextIndex);
    }

    public int getNext() {
        return nextIndex.getAndUpdate(current -> (current == maxIndex)
                                                 ? minIndex
                                                 : (current + 1));
    }
}
