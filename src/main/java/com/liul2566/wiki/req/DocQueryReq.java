package com.liul2566.wiki.req;

public class DocQueryReq extends PageReq {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{} " + super.toString();
    }
}