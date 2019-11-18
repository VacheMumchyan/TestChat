package com.example.innorise.testchat.ui.screen;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.innorise.testchat.utils.Constants;


public class EnterViewModel extends ViewModel {

    public MutableLiveData<String> edittext = new MutableLiveData<>();
    private MutableLiveData<String> nickName = new MutableLiveData<>();
    private MutableLiveData<String> error_message = new MutableLiveData<>();

    public MutableLiveData<String> getError_message() {
        return error_message;
    }

    public MutableLiveData<String> getNickName() {
        return nickName;
    }

    public void  onEnterClick(){
        if (edittext.getValue() != null && !edittext.getValue().isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra(Constants.NICKNAME, edittext.getValue());
            nickName.setValue(edittext.getValue());
        }else {
          error_message.setValue(Constants.ERROR_MESSAGE_NICKNAME);
        }
    }

}
