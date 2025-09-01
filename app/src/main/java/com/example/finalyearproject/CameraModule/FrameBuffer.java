package com.example.finalyearproject.CameraModule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
public class FrameBuffer {
    private final Deque<byte[]> deque = new ArrayDeque<>();
    private final int capacity;

    public FrameBuffer(int capacity) {
        this.capacity = Math.max(10, capacity);
    }

    public synchronized void add(byte[] frame) {
        if (deque.size() >= capacity) {
            deque.removeFirst();
        }
        // store a copy to avoid accidental mutation
        byte[] copy = new byte[frame.length];
        System.arraycopy(frame, 0, copy, 0, frame.length);
        deque.addLast(copy);
    }

    /** Take up to 'count' most recent frames (last-in-first-out order). */
    public synchronized List<byte[]> takeLatest(int count) {
        List<byte[]> out = new ArrayList<>(Math.min(count, deque.size()));
        int i = 0;
        for (Iterator<byte[]> it = deque.descendingIterator(); it.hasNext(); ) {
            byte[] b = it.next();
            out.add(b);
            if (++i >= count) break;
        }
        return out;
    }
}
