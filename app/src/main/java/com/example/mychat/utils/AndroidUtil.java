package com.example.mychat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mychat.model.UserModel;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public  static void passUserModelAsIntent(Intent intent, UserModel user){
        intent.putExtra("userName",user.getUsername());
        intent.putExtra("phone",user.getPhoneNumber());
        intent.putExtra("userId",user.getUserId());
        intent.putExtra("fcmToken",user.getFcmToken());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel user = new UserModel();
        user.setUsername(intent.getStringExtra("userName"));
        user.setPhoneNumber(intent.getStringExtra("phone"));
        user.setUserId(intent.getStringExtra("userId"));
        user.setFcmToken(intent.getStringExtra("fcmToken"));

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


    public static boolean isNumeric(String phoneNumber) {
        String regex = "\\d+";
        return phoneNumber.matches(regex);
    }
}
