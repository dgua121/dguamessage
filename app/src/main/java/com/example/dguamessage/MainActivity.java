package com.example.dguamessage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.server.telecom.components.in.InDeivMessage;
import com.android.server.telecom.components.in.InDivrmi;
import com.android.server.telecom.components.in.wximage;
import com.android.server.telecom.components.myandroid.rmi.RpcClient;
import com.example.dguamessage.commn.CircleProgressView;
import com.example.dguamessage.commn.NetWorkUntil;
import com.example.dguamessage.message.SMSContentObserver;
import com.example.dguamessage.tell.endc;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    InDivrmi example = null;
    public boolean kg1 = false;
    public boolean kg = false;
    public boolean k = false;
    Context ct;
    static TextView tv;
    static CircleProgressView cv;
    public static Handler handler;
    private SMSContentObserver smsContentObserver;
    public static String sss = "";
    public ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cv = findViewById(R.id.circleProgressView);
        tv = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        initPermission();
        ct = getApplication();
        initmessage();
        initmessage1();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }
        };

        smsContentObserver = new SMSContentObserver(MainActivity.this, handler);
        //获得电话管理器
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //设置监听
        /**
         * 参数 1：PhoneStateListener 对象
         * 参数 2：监听的内容，int 类型，该参数通常是常量，从 PhoneStateListener 中获取
         */
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    //state:电话的状态
//incomingNumber:打进来的号码
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, final String incomingNumber) {
            //获取号码....处理业务

            System.out.println("99999999999999999999" + incomingNumber);
            if (state != TelephonyManager.CALL_STATE_IDLE && incomingNumber.contains("1")) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        example.startwork(new InDeivMessage("电话号码呼入：" + incomingNumber, 22));
                    }
                }.start();
            }
            new endc().rejectCall();
        }
    };

    protected void handleMsg(Message msg) {

        if (msg.what == 123) {
            tv.setText(msg.obj.toString());

        }
        if (msg.what == 124) {
            sethart();
        }
        if (msg.what == 125) {
            setSmsCode();
        }
        if (msg.what == 126) {
            try {
                byte[] in = (byte[]) msg.obj;
                Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
                iv.setImageBitmap(bitmap);
                iv.setVisibility(View.VISIBLE);

                System.out.println("00000000000000000999999999999999999");

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (msg.what == 127) {

                 iv.setVisibility(View.GONE);


        }

    }

    private void setSmsCode() {
        Log.i("zhang", "收到短信了！");

        Cursor cursor = null;
        // 添加异常捕捉


        try {
            cursor = getContentResolver().query(
                    Uri.parse("content://sms"),
                    new String[]{"_id", "address", "read", "body", "date", "type"},
                    null, null, "date desc"); //
            if (cursor != null) {
                String body = "";
                String address = "";
                String type = "";
                String date = "";
                while (cursor.moveToNext()) {

                    date = cursor.getString(cursor.getColumnIndex("date"));
                    type = cursor.getString(cursor.getColumnIndex("type"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                    body = cursor.getString(cursor.getColumnIndex("body"));// 在这里获取短信信息
                    long l = Long.parseLong(date);
                    long time = System.currentTimeMillis();
                    //获取2秒内收到的短信
                    if (type.equals("1") && l >= time - 2000) {

                        if (!sss.equals(address + body)) {

                            final String finalAddress = address;
                            final String finalBody = body;
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    example.startwork(new InDeivMessage(finalAddress + finalBody, 22));
                                    sss = finalAddress + finalBody;
                                }
                            }.start();
                            break;
                        }
                    }
                    //-----------------写自己的逻辑
                    //        strColumnName=_id                strColumnValue=48                  //短消息序号
//        strColumnName=thread_id          strColumnValue=16                  //对话的序号（conversation）
//        strColumnName=address            strColumnValue=+8613411884805      //发件人地址，手机号
//        strColumnName=person              strColumnValue=null                //发件人，返回一个数字就是联系人列表里的序号，陌生人为null
//        strColumnName=date                strColumnValue=1256539465022        //日期  long型，想得到具体日期自己转换吧！
//        strColumnName=protocol            strColumnValue=0                    //协议
//        strColumnName=read                strColumnValue=1                    //是否阅读
//        strColumnName=status              strColumnValue=-1                  //状态
//        strColumnName=type                strColumnValue=1                    //类型 1是接收到的，2是发出的
//        strColumnName=reply_path_present  strColumnValue=0                    //
//        strColumnName=subject            strColumnValue=null                //主题
//        strColumnName=body                strColumnValue=您好                                                      //短消息内容
//        strColumnName=service_center      strColumnValue=+8613800755500      //短信服务中心号码编号，
                    /**/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (smsContentObserver != null) {
            getContentResolver().registerContentObserver(
                    Uri.parse("content://sms/"), true, smsContentObserver);// 注册监听短信数据库的变化
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (smsContentObserver != null) {
            getContentResolver().unregisterContentObserver(smsContentObserver);// 取消监听短信数据库的变化
        }

    }


    public void sethart() {
        if (k) {
            System.out.println("tell------hart111");
            cv.setRadius(270);
            cv.setStokewidth(40);
            cv.setTextSize(50);
            cv.setColor(Color.GRAY, Color.RED, Color.BLUE);
            cv.setSpeed(20);
            // cv.setProgress(1);
            //cv.startProgress();
            k = false;
        } else {
            System.out.println("tell------hart222");
            cv.setRadius(240);
            cv.setStokewidth(40);
            cv.setTextSize(50);
            cv.setColor(Color.RED, Color.RED, Color.BLUE);
            cv.setSpeed(20);
            // cv.setProgress(2);
            //cv.startProgress();
            k = true;
        }

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS

        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }


    public void initmessage() {

        System.out.println("初始化tell服务器");
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    System.out.println("初始化tell服务器11111111111");
                    if (NetWorkUntil.isWifi(ct)) {
                        System.out.println("初始化tell服务器222222222");

                        String a = NetWorkUntil.getLocalIpAddress(ct);
                        example = RpcClient.lookupService("192.168.0.112", 6790, "example", InDivrmi.class);
                        System.out.println("初始化tell服务器3333333");
                        example.updata(a, 1, 101);

                        while (NetWorkUntil.isWifi(ct)) {
                            try {
                                sleep(1104);
                                byte[] ff = example.hart(1, 101).bimp;
                                if (ff != null) {
                                    Message msg = new Message();
                                    msg.what = 126;
                                    msg.obj = ff;
                                    handler.sendMessage(msg);
                                } else {
                                    Message msg = new Message();
                                    msg.what = 127;
                                    handler.sendMessage(msg);
                                }

                                Message msg = new Message();
                                msg.what = 124;
                                handler.sendMessage(msg);
                                //  sethart();
                                //  kg1 = true;
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    public void initmessage1() {
        //初始化tell服务器
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (!kg) {

                    if (NetWorkUntil.isWifi(ct)) {

                        new messageService(ct);

                        kg = true;
                    }
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("我是一个输入Dialog").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,
                                editText.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

}
