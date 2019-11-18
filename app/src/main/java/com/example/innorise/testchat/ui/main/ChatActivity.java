package com.example.innorise.testchat.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.innorise.testchat.R;
import com.example.innorise.testchat.data.repository.ChatRepository;
import com.example.innorise.testchat.databinding.ActivityChatBinding;
import com.example.innorise.testchat.ui.main.adapter.ChatAdapter;
import com.example.innorise.testchat.ui.main.factory.ChatViewModelFactory;
import com.example.innorise.testchat.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private String nickname;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChatBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        if (getIntent() != null) {
            nickname = getIntent().getExtras().getString(Constants.NICKNAME);
        }

        chatAdapter = new ChatAdapter();
        chatRecyclerView = findViewById(R.id.messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
        EditText message_input = findViewById(R.id.message_input);

        chatViewModel = ViewModelProviders.of(this, new ChatViewModelFactory(
                nickname, new ChatRepository(),
                getApplicationContext().getContentResolver())
        ).get(ChatViewModel.class);
        binding.setViewModel(chatViewModel);

        chatViewModel.init();

        chatViewModel.getErrorMessage().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

        chatViewModel.getReceivedMessage().observe(this, message -> {
            chatAdapter.setMessage(message);
            scrollToBottom();
            message_input.setText("");

        });

        chatViewModel.getCheckPermission().observe(this, aBoolean -> {
            checkPermission();
        });

        chatViewModel.getOpenGalleryStatus().observe(this, aBoolean -> {
            openGallery();
        });

        chatViewModel.getEmptyMessageError().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

    }

    private void scrollToBottom() {
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.socket_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,

                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        chatViewModel.onPermissionsChecked(report);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_attach:
                chatViewModel.galleryAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatViewModel.onActivityResult(requestCode, resultCode, data);
    }
}


