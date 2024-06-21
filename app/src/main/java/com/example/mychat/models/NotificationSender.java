package com.example.mychat.models;

import okhttp3.*;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import java.io.IOException;

public class NotificationSender {

    private static final String URL = "https://fcm.googleapis.com/v1/projects/mobile-chat-3ee15/messages:send";

    public static void sendNotification(String message, String type, String fcmToken, String username, String userId) {
        try {
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", username);
            notificationObj.put("body", type.equals("image") ? "Sent an image" : (type.equals("video") ? "Sent a video" : message));

            JSONObject dataObj = new JSONObject();
            dataObj.put("userId", userId);

            JSONObject messageObj = new JSONObject();
            messageObj.put("notification", notificationObj);
            messageObj.put("data", dataObj);
            messageObj.put("token", fcmToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", messageObj);

            callApi(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void callApi(JSONObject jsonObject) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String serviceAccount = "";
        
        
        
        InputStream inputStream = new ByteArrayInputStream(serviceAccount.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/firebase.messaging"));
        credentials.refreshIfExpired();


        String token = credentials.getAccessToken().getTokenValue();

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Request not successful: " + response.code() + " - " + response.message());
                    System.out.println("Response: " + response.body().string());
                } else {
                    System.out.println("Request successful: " + response.body().string());
                }
            }
        });
    }
}
