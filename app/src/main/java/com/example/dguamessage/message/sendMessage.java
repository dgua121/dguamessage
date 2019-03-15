package com.example.dguamessage.message;

import java.util.List;

public class sendMessage {

    public void sendSMS(String phoneNumber, String message, int i) {
        System.out.println("99--->" + phoneNumber);

        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager
                .getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
//        Toast.makeText(this,phoneNumber+": "+message,Toast.LENGTH_SHORT).show();

    }
}
