package org.apache.airavata.sga.scheduler.messaging.buffer;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by arvind on 2/25/17.
 */
public class RingBuffer<T> implements Buffer<T> {
    private Object[] elements;

    private int capacity;
    private int tailPos = 0;
    private int headPos = 0;
    private int available = 0;

    public RingBuffer (int cap) {
        elements = (T[]) new Object[cap];
        this.capacity = cap;
    }

    public RingBuffer (T[] elements) {
        this.elements = elements;
        this.capacity = elements.length;
    }

    public void reset() {
        this.tailPos = 0;
        this.available = 0;
    }

    public int getCapacity() { return capacity; }
    public int getAvailable() { return available; }

    public int remainingCapacity() {
        return this.capacity - this.available;
    }

    public T getHead() {
        return (T) elements[headPos];
    }

    public T getTail() {
        return (T) elements[tailPos];
    }

    public boolean willOverwrite() {
        return available > 0 && tailPos == headPos;
    }

    public boolean push(T elem) {

        elements[tailPos] = elem;
        tailPos = (tailPos + 1) % capacity;

        if(available > 0 && tailPos == headPos) {
            headPos = (headPos + 1) % capacity;
            return false;
        } else {
            available++;
            return true;
        }
    }

    public T pop() {
        if(available > 0) {
            T elem = (T) elements[headPos];
            headPos = (headPos + 1) % capacity;
            available--;
            return elem;
        } else {
            throw new RingBufferException("Cannot pop from an empty queue");
        }
    }

    public boolean release() {
        if(available > 0) {
            headPos = (headPos + 1) % capacity;
            available--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
