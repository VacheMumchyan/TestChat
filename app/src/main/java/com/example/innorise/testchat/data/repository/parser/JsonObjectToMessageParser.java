package com.example.innorise.testchat.data.repository.parser;

import android.graphics.Bitmap;


import com.example.innorise.testchat.data.entity.Message;
import com.example.innorise.testchat.utils.BitmapParser;
import com.example.innorise.testchat.utils.Constants;

import org.json.JSONObject;
import io.reactivex.functions.Function;


public class JsonObjectToMessageParser implements Function<JSONObject, Message> {
    @Override
    public Message apply(JSONObject data) {
        String nickName = null;
        String message = null;
        Bitmap bitmap = null;

        try {
            nickName = data.getString(Constants.NAME);
        }catch (Exception e){

        }
        try {
            message = data.getString(Constants.TEXT);
        }catch (Exception e){

        }
        try {
            bitmap = BitmapParser.base64ToBitmap(data.getString(Constants.IMAGE));
        }catch (Exception e){

        }
        return new Message(nickName, message, bitmap);
    }

}
