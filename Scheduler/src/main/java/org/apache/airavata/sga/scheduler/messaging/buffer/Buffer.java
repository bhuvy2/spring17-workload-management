package org.apache.airavata.sga.scheduler.messaging.buffer;

import java.util.Iterator;

/**
 * Created by arvind on 2/26/17.
 */
public interface Buffer<T> {

    public Iterator<T> iterator();

}
