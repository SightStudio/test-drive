package com.gc_demo.cms.web;

public class DummyObject {

    private final byte[] data = new byte[1024]; // 1KB 크기의 객체
    private final int id;

    public DummyObject(int id) {
        this.id = id;
    }
}
