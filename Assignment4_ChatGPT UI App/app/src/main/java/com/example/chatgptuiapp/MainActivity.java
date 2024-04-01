package com.example.chatgptuiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText promptEditText;
    TextView response;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        promptEditText = findViewById(R.id.prompt);
        Button send = findViewById(R.id.send);
        Button cancel = findViewById(R.id.cancel);
        response = findViewById(R.id.response);
        progressBar = findViewById(R.id.progressBar);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prompt = promptEditText.getText().toString().trim();
                if (!prompt.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    callOpenAIAPi(prompt, new OpenAIResponseCallback() {
                        @Override
                        public void onResponse(String resp) {
                            response.setText(resp);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(String error) {
                            response.setText(error);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a prompt", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancel.setOnClickListener(view -> {
            promptEditText.setText("");
            response.setText("");
        });
    }

    public void callOpenAIAPi(String prompt, OpenAIResponseCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            // Replace YOUR_API_KEY with your actual OpenAI API key
            String apiKey = "secret";

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            String json = "{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"system\",\"content\": \"You are a helpful assistant.\"},{\"role\": \"user\",\"content\": \"" + prompt + "\"}]}";

            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = response.body().string();

                    JSONObject jsonObject = new JSONObject(responseString);
                    String content = jsonObject.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    handler.post(() -> {
                        callback.onResponse(content);
                    });
                } else {
                    handler.post(() -> {
                        callback.onError("Error calling API: " + response);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    callback.onError("Facing issue... Please try again");
                });
            }
        });
    }


}

