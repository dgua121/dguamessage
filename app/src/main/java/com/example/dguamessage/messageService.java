package com.example.dguamessage;

import android.content.Context;
import android.os.Message;

import com.android.server.telecom.components.myandroid.rmi.RpcServer;
import com.android.server.telecom.components.out.OutDivMessage;
import com.android.server.telecom.components.out.OutDivrmimessage;
import com.example.dguamessage.commn.addphone;
import com.example.dguamessage.message.sendMessage;


public class messageService implements OutDivrmimessage {
    Context ct;
    int i = 0;

    public messageService(Context a) {
        ct = a;
        System.out.println("服務器東");
        try {
            // create the RMI server
            RpcServer rpcServer = new RpcServer();
            // register a service under the name example
            // the service has to implement an interface for the magic to work
            rpcServer.registerService("message", this);
            // start the server at port 6789
            rpcServer.start(6791);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int startwork(final OutDivMessage outDivMessage) {
        i++;
        MainActivity.cv.setProgress(i);

        Message msg = new Message();
        msg.what = 123;
        msg.obj =outDivMessage.getname()+"/n"+outDivMessage.getphone()+"/n"+outDivMessage.getname().substring(0, 1) + outDivMessage.getmessage() ;
        MainActivity.handler.sendMessage(msg);
           System.out.print("ddddddddddddddddddddddd");
           new addphone(ct).addContact(outDivMessage.getname(), outDivMessage.getphone());


           new sendMessage().sendSMS(outDivMessage.getphone(), outDivMessage.getname().substring(0, 1) + outDivMessage.getmessage(), 1);



        return 0;
    }
}
