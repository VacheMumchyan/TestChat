package com.example.innorise.testchat.data.repository;

import com.example.innorise.testchat.data.entity.Message;
import com.example.innorise.testchat.utils.BitmapParser;
import com.example.innorise.testchat.utils.Constants;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ChatRepository {

    private Socket socket;

    public void init() throws URISyntaxException {
        socket = IO.socket(Constants.SERVER_ADDRESS);
    }

    public Observable<JSONObject> connect() {
        PublishSubject<JSONObject> subject = PublishSubject.create();

        if (socket != null) {
            socket.connect();
            socket.on(Constants.SERVER_EVENT, args -> {
                subject.onNext((JSONObject) args[0]);
            });
        } else {
            subject.onError(new Throwable("Can not connected"));
        }
        return subject;
    }

    public void sendMessage(Message message){
        JSONObject senderJson = new JSONObject();
        try{
            senderJson.put(Constants.NAME, message.getNickname());
            senderJson.put(Constants.TEXT,message.getMessage());
            senderJson.put(Constants.IMAGE, BitmapParser.bitmapToBase64(message.getBitmap()));
        }catch(JSONException e){

        }
        socket.emit(Constants.SERVER_EVENT, senderJson);
    }

    public void disconnect() {
        socket.disconnect();
    }
}
