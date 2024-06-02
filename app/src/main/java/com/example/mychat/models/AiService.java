package com.example.mychat.models;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AiService {

    private static Map<Long, List<Content>> chatHistories = new HashMap<>();

    public static CompletableFuture<String> sendMessageAi(Context context, String message, Bitmap image, long chatRoomId) {
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", "AIzaSyCNaCOVQFhuPFCGOuM9lLevLJ-ZRm_kSm0");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        List<Content> chatHistory = chatHistories.computeIfAbsent(chatRoomId, k -> new ArrayList<>());

        Content.Builder userMessageBuilder = new Content.Builder();
        userMessageBuilder.setRole("user");
        userMessageBuilder.addText(message);
        if (image != null) {
            userMessageBuilder.addImage(image);
        }
        Content userMessage = userMessageBuilder.build();

        chatHistory.add(userMessage);

        Content[] historyArray = chatHistory.toArray(new Content[0]);

        ListenableFuture<GenerateContentResponse> response = model.generateContent(historyArray);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        FutureCallback<GenerateContentResponse> callback = new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String responseText = result.getText();

                Content.Builder aiMessageBuilder = new Content.Builder();
                aiMessageBuilder.setRole("model");
                aiMessageBuilder.addText(responseText);
                Content aiMessage = aiMessageBuilder.build();

                chatHistory.add(aiMessage);

                completableFuture.complete(responseText);
            }

            @Override
            public void onFailure(Throwable t) {
                completableFuture.completeExceptionally(t);
                t.printStackTrace();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, callback, context.getMainExecutor());
        } else {
            Futures.addCallback(response, callback, MoreExecutors.directExecutor());
        }

        return completableFuture;
    }
}
