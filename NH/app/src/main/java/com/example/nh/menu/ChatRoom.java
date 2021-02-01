package com.example.nh.menu;

import com.example.nh.User;
import com.example.nh.chatting.DataItem;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ChatRoom implements Serializable {


    private Integer id;
    private String roomId;
    private List<String> userList;
    private List<DataItem> dataItemList;


    public String getRoomId() {

        return roomId;

    }

    public void setRoomId(String roomId) {

        this.roomId = roomId;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<DataItem> getDataItemList() {
        return dataItemList;
    }

    public void setDataItemList(List<DataItem> dataItemList) {
        this.dataItemList = dataItemList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
