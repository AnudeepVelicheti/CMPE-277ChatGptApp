package com.example.chatgptuiapp;

public interface OpenAIResponseCallback {
    void onResponse(String response);
    void onError(String error);
}
