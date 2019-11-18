package com.example.innorise.testchat.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.innorise.testchat.R;
import com.example.innorise.testchat.databinding.ActivityEnterBinding;
import com.example.innorise.testchat.ui.main.ChatActivity;
import com.example.innorise.testchat.utils.Constants;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        EnterViewModel viewModel = ViewModelProviders.of(this).get(EnterViewModel.class);

        ActivityEnterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_enter);
        binding.setViewModel(viewModel);
        viewModel.getNickName().observe(this, s -> {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Constants.NICKNAME, s);
                startActivity(intent);
                finish();
        });

        viewModel.getError_message().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(EnterActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
