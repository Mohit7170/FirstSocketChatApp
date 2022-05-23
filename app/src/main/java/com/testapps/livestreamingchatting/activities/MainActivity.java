package com.testapps.livestreamingchatting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.testapps.livestreamingchatting.R;
import com.testapps.livestreamingchatting.utils.Helper;
import com.testapps.livestreamingchatting.utils.Params;

public class MainActivity extends AppCompatActivity implements Params {

    EditText userName;
    EditText roomId;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.name);
        roomId = findViewById(R.id.room_id);
        button = findViewById(R.id.join_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = MainActivity.this.userName.getText().toString().trim();
                String roomId = MainActivity.this.roomId.getText().toString().trim();

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(roomId)) {
                    startActivity(new Intent(MainActivity.this, VideoActivity.class)
                            .putExtra(INTENT_USER_NAME, userName)
                            .putExtra(INTENT_ROOM_ID, roomId));
                    finish();
                } else {
                    Helper.showSnackBar(MainActivity.this, "Please Enter Name and Room id to join");
                }
            }
        });
    }
}