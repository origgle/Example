package com.hello.demo.hellodemo;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Administrator on 2016/1/1.
 */
public class UDPSender {
    private static final String TAG = UDPSender.class.getSimpleName();

    private static final String SERVERIP = "255.255.255.255";
    private static final int SERVERPORT = 8088;

    public static String send(String data) {

        DatagramSocket ds = null;

        try {
            final int MAX_LEN = 128;
            byte[] buffer = new byte[MAX_LEN];

            byte outBuf[] = data.getBytes();
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            dp.setData(outBuf);
            dp.setLength(outBuf.length);
            dp.setAddress(serverAddr);
            dp.setPort(SERVERPORT);

            Log.e(TAG, "Send data => " + data);

            ds = new DatagramSocket();
            ds.send(dp);

            dp.setData(buffer);
            dp.setLength(buffer.length);
            ds.receive(dp);
            if (dp.getLength() > 0) {
                return new String(dp.getData(), 0, dp.getLength());
            }

        } catch (Throwable e) {
            e.printStackTrace();

        } finally {
            ds.close();
        }

        return null;
    }
}
