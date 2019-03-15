package com.example.dguamessage;

import android.content.Context;

import com.android.server.telecom.components.myandroid.rmi.RpcServer;
import com.android.server.telecom.components.out.OutDivMessage;
import com.android.server.telecom.components.out.OutDivrmitell;
import com.example.dguamessage.commn.addphone;
import com.example.dguamessage.tell.call;
import com.example.dguamessage.tell.endc;


public class tellServer implements OutDivrmitell {
    Context ct;

    public tellServer(Context a) {
        ct = a;
        System.out.println("服務器東");
        try {
            // create the RMI server
            RpcServer rpcServer = new RpcServer();
            // register a service under the name example
            // the service has to implement an interface for the magic to work
            rpcServer.registerService("tell", this);
            // start the server at port 16790
            rpcServer.start(16790);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int endcall() {
        System.out.println("end--------------------");
        new endc().rejectCall();
        return 0;
    }

    @Override
    public int startwork(OutDivMessage outDivMessage) {
        System.out.println("nihaoi--------------------");
        new addphone(ct).addContact(outDivMessage.getname(), outDivMessage.getphone());

        new call(ct).start(outDivMessage.getphone().toString());
        return 0;
    }

}

