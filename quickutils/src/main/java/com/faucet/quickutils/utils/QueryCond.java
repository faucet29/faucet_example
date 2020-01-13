package com.faucet.quickutils.utils;

/**
 * Created by apple on 16/12/2.
 */

public class QueryCond {
    private long start;
    private int count;

    public QueryCond(int c) {
        this.start = -1;
        this.count = c;
    }

    public QueryCond(long start, int count) {
        this.start = start;
        this.count = count;
    }

    public long getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        start = -1;
        count = 10;
    }

    public void reset(int _count) {
        start = -1;
        count = _count;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "QueryCond{" +
                "count=" + count +
                ", start=" + start +
                '}';
    }
}
