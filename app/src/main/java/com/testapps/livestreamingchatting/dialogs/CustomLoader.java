package com.testapps.livestreamingchatting.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.testapps.livestreamingchatting.R;

public class CustomLoader extends Dialog {

    public CustomLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_loader);
        setCancelable(false);
    }
}
