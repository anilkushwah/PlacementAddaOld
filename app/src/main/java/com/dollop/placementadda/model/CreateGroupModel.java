package com.dollop.placementadda.model;

/**
 * Created by Kasim on 20-04-2018.
 */

public class CreateGroupModel {
    String playerGroup_id,name,total_member;
    String user_img;

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public CreateGroupModel(){}
    public String getPlayerGroup_id() {
        return playerGroup_id;
    }

    public void setPlayerGroup_id(String playerGroup_id) {
        this.playerGroup_id = playerGroup_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_member() {
        return total_member;
    }

    public void setTotal_member(String total_member) {
        this.total_member = total_member;
    }

    public CreateGroupModel(String playerGroup_id, String name, String total_member,String user_img) {

        this.playerGroup_id = playerGroup_id;
        this.name = name;
        this.total_member = total_member;
        this.user_img=user_img;
    }
}
