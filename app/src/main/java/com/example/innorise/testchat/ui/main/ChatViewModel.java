package com.example.innorise.testchat.ui.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.innorise.testchat.data.entity.Message;
import com.example.innorise.testchat.data.repository.ChatRepository;
import com.example.innorise.testchat.data.repository.parser.JsonObjectToMessageParser;
import com.example.innorise.testchat.utils.BitmapParser;
import com.example.innorise.testchat.utils.Constants;
import com.karumi.dexter.MultiplePermissionsReport;

import java.net.URISyntaxException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class ChatViewModel extends ViewModel {

    private final ContentResolver contentResolver;
    private CompositeDisposable disposable;

    private ChatRepository chatRepository;
    private String nickName;

    private MutableLiveData<Message> receivedMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkPermission = new MutableLiveData<>();
    private MutableLiveData<Boolean> openGalleryStatus = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<String> emptyMessageError = new MutableLiveData<>();

    public MutableLiveData<String> getEmptyMessageError() {
        return emptyMessageError;
    }

    public MutableLiveData<String> senderMessage = new MutableLiveData<>();
    private MutableLiveData<Bitmap> senderImage = new MutableLiveData<>();

    public MutableLiveData<Boolean> getCheckPermission() {
        return checkPermission;
    }

    public MutableLiveData<Boolean> getOpenGalleryStatus() {
        return openGalleryStatus;
    }

    public MutableLiveData<Message> getReceivedMessage() {
        return receivedMessage;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public ChatViewModel(String nickName, ChatRepository chatRepository, ContentResolver contentResolver) {
        this.nickName = nickName;
        this.chatRepository = chatRepository;
        this.contentResolver = contentResolver;
        disposable = new CompositeDisposable();
    }

    public void init() {
        try {
            chatRepository.init();
            disposable.add(chatRepository.connect()
                    .map(new JsonObjectToMessageParser())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(message -> {
                        receivedMessage.setValue(message);

                    }, err -> {
                        errorMessage.setValue(Constants.ERROR_RECIEVED_MESSAGE);
                    }, () -> {

                    }));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void send() {
        if (senderMessage.getValue() != null &&!senderMessage.getValue().equals("")) {
            sendMessage(new Message(nickName, senderMessage.getValue(), senderImage.getValue()));
            senderMessage.setValue("");
        }else {
            emptyMessageError.setValue(Constants.EMPTY_MESSAGE);
        }
    }

    private void sendMessage(Message message) {
        chatRepository.sendMessage(message);
        receivedMessage.setValue(message);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = contentResolver.query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            sendMessage(new Message(nickName, null, BitmapParser.urlToBitmap(picturePath)));
        }
    }

    public void galleryAction() {
        checkPermission.setValue(true);
    }

    public void onPermissionsChecked(MultiplePermissionsReport report) {
        // check if all permissions are granted
        if (report.areAllPermissionsGranted()) {
            openGalleryStatus.setValue(true);
        } else if (report.isAnyPermissionPermanentlyDenied()) {
            errorMessage.setValue(Constants.PERMISSION_ERROR);
        } else {
            errorMessage.setValue(Constants.CHECK_PERMISSION);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        chatRepository.disconnect();
    }
}
