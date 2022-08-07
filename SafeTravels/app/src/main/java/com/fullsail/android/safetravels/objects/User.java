package com.fullsail.android.safetravels.objects;

import android.net.Uri;

public class User {

    String username;
    String uid;
    String uri;

    public User(String username, String uid, String uri) {
        this.username = username;
        this.uid = uid;
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getUri() {
        return uri;
    }
}
