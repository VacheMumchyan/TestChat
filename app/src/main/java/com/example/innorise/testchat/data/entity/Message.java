package com.example.innorise.testchat.data.entity;

import android.graphics.Bitmap;

/**
 * Created by sreejeshpillai on 10/05/15.
 */
public class Message {


    private String nickname;
    private String message ;
    private Bitmap bitmap;

    public Message(String nickname, String message, Bitmap bitmap) {
        this.nickname = nickname;
        this.message = message;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nickname='" + nickname + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}