/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.wave.fundamental.utility.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is intended as a (mostly) GC-free alternative to {@link ConcurrentLinkedQueue}
 * when the requirement is to create an unbounded queue with no requirement to shrink
 * the queue. The aim is to provide the bare minimum of required functionality as quickly
 * as possible with minimum garbage.
 *
 * @author Vince Yuan
 * @date 2021/12/2
 * @param <T> The type of object managed by this queue
 */
public class SynchronizedQueue<T> {

    public static final int DEFAULT_SIZE = 128;

    private Object[] queue;
    private int size;
    private int insert = 0;
    private int remove = 0;

    public SynchronizedQueue() {
        this(DEFAULT_SIZE);
    }

    public SynchronizedQueue(int initialSize) {
        queue = new Object[initialSize];
        size = initialSize;
    }

    /**
     * This method is used to add an element to the tail of this queue
     *
     * @param t
     * @return
     */
    public synchronized boolean offer(T t) {
        queue[insert++] = t;

        // Wrap
        if (insert == size) {
            insert = 0;
        }

        if (insert == remove) {
            expand();
        }
        return true;
    }

    /**
     * This method is used to get the element from the head of this queue
     *
     * @return
     */
    public synchronized T poll() {
        if (insert == remove) {
            // empty
            return null;
        }

        @SuppressWarnings("unchecked")
        T result = (T) queue[remove];
        queue[remove] = null;
        remove++;

        // Wrap
        if (remove == size) {
            remove = 0;
        }

        return result;
    }

    /**
     * This method is used to get the number of elements in this queue
     *
     * @return
     */
    public synchronized int size() {
        int result = insert - remove;
        if (result < 0) {
            result += size;
        }
        return result;
    }

    /**
     * This method is sued to clear all elements in this queue
     */
    public synchronized void clear() {
        queue = new Object[size];
        insert = 0;
        remove = 0;
    }

    /**
     * This method is used to expand the capacity of this queue
     */
    private void expand() {
        int newSize = size * 2;
        Object[] newQueue = new Object[newSize];

        System.arraycopy(queue, insert, newQueue, 0, size - insert);
        System.arraycopy(queue, 0, newQueue, size - insert, insert);

        insert = size;
        remove = 0;
        queue = newQueue;
        size = newSize;
    }
}
