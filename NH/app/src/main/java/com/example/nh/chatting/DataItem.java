package com.example.nh.chatting;

import java.io.Serializable;

public class DataItem implements Serializable {

    private Integer id;
    private String content;
    private String name;
    private int viewType;

    public DataItem(String content, String name ,int viewType) {
        this.content = content;
        this.viewType = viewType;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public int getViewType() {
        return viewType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
