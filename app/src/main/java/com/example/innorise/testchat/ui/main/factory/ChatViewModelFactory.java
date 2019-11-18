package com.example.innorise.testchat.ui.main.factory;

import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.innorise.testchat.data.repository.ChatRepository;
import com.example.innorise.testchat.ui.main.ChatViewModel;


public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private String nickNAme;
    private ChatRepository repository;
    private ContentResolver contentResolver;


    public ChatViewModelFactory(String nickNAme, ChatRepository repository, ContentResolver contentResolver) {
        this.nickNAme = nickNAme;
        this.repository = repository;
        this.contentResolver = contentResolver;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ChatViewModel(nickNAme, repository,contentResolver);
    }

}