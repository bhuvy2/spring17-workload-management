package org.apache.airavata.sga.scheduler.messaging.buffer;

import org.apache.airavata.sga.scheduler.util.Constants;

import java.time.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by arvind on 2/25/17.
 */
public class TrackingBuffer<T> implements Buffer<T> {
    private ArrayDeque<T> buffer;
    private double addTime;
    private static RingBuffer<Long> recentTimes;

    public TrackingBuffer (int ringBufferCap) {
        buffer = new ArrayDeque<T>();
        recentTimes = new RingBuffer<Long>(ringBufferCap);
        addTime = 0;
    }

    public TrackingBuffer (int initialCapacity, int ringBufferCap) {
        buffer = new ArrayDeque<T>(initialCapacity);
        recentTimes = new RingBuffer<Long>(ringBufferCap);
        addTime = 0;
    }

    public void push(T elem) {
        buffer.push(elem);
        long time = ZonedDateTime.now().toInstant().toEpochMilli();
        if(recentTimes.willOverwrite())
            addTime -= recentTimes.getTail();
        recentTimes.push(time);
    }

    public double getAverage() {
        return addTime / recentTimes.getAvailable();
    }

    public static int getRingBufferCapacity() {
        try {
            return Integer.parseInt(Constants.properties.
                    getProperty(Constants.SCHEDULER_MESSAGE_BUFFER_TRACKING_SIZE));
        } catch (NumberFormatException nfe) {
            return 10;
        }
    }

    public Iterator<T> iterator() { return buffer.iterator(); }

    public int size() { return buffer.size(); }

    public void clear() { buffer.clear(); }

}
