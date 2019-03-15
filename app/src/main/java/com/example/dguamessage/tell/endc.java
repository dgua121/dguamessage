package com.example.dguamessage.tell;

import android.content.Context;
import android.os.IBinder;


import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class endc {


    //挂断电话
    public void rejectCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
            // Log.d(TAG, "", e);
        } catch (ClassNotFoundException e) {
            //Log.d(TAG, "", e);
        } catch (Exception e) {
        }
    }
}
