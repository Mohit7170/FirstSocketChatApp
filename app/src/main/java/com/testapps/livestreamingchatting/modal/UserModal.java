package com.testapps.livestreamingchatting.modal;

import androidx.lifecycle.ViewModel;

public class UserModal extends ViewModel {

    private String message;
    private String imgUrl;
    private String name;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
