package com.example.mychat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychat.models.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public  static void passUserModelAsIntent(Intent intent, User user){
        intent.putExtra("userName",user.getUsername());
        intent.putExtra("phone",user.getPhoneNumber());
        intent.putExtra("userId",user.getUserId());
        intent.putExtra("fcmToken",user.getFcmToken());
        intent.putExtra("email",user.getEmail());
        intent.putExtra("password",user.getPassword());
    }

    public static User getUserModelFromIntent(Intent intent){
        User user = new User();
        user.setUsername(intent.getStringExtra("userName"));
        user.setPhoneNumber(intent.getStringExtra("phone"));
        user.setUserId(intent.getStringExtra("userId"));
        user.setFcmToken(intent.getStringExtra("fcmToken"));
        user.setEmail(intent.getStringExtra("email"));
        user.setPassword(intent.getStringExtra("password"));
        return user;
    }



    public static String formatPhoneNumber(String phoneNumber, String countryCode) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, countryCode);
            String formattedPhoneNumber = phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

            formattedPhoneNumber = formattedPhoneNumber.replaceAll("\\s", "");

            return formattedPhoneNumber;
        } catch (NumberParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


    public static boolean isNumeric(String phoneNumber) {
        String regex = "\\d+";
        return phoneNumber.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    public static List<String> splitStringToList(String input) {
        String[] parts = input.split("_");
        return Arrays.asList(parts);
    }

    public static List<String> getUserIdsFromMap(Map<User, Boolean> addedUsersMap) {
        List<String> userIds = new ArrayList<>();
        for (User user : addedUsersMap.keySet()) {
            userIds.add(user.getUserId());
        }
        return userIds;
    }
}
