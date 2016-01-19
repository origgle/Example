package com.hello.demo.hellodemo;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Administrator on 2016/1/1.
 */
public class UDPRecevier {
    private static final String TAG = UDPRecevier.class.getSimpleName();

    public interface Callback {
        void onDataReceived(byte[] data, long length);
    }

    private UDPThread mUDPThread = null;
    private Callback mCallback = null;

    public UDPRecevier(Callback callback) {
        mCallback = callback;
    }

    public void start() {
        synchronized (this) {
            if(mUDPThread == null) {
                mUDPThread = new UDPThread();
                mUDPThread.start();
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if(mUDPThread != null) {
                mUDPThread.interrupt();
                mUDPThread = null;
            }
        }
    }

    private class UDPThread extends Thread {
        DatagramSocket ds = null;

        @Override
        public void interrupt() {
            super.interrupt();

            if(ds != null) {
                ds.close();
            }
        }

        @Override
        public void run() {
            Log.e(TAG, "UDPThread run");

            try {
                ds = new DatagramSocket(8088);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if(ds != null) {
                try {
                    byte[] buf = new byte[1024];
                    while(!Thread.interrupted()) {
                        DatagramPacket pack = new DatagramPacket(buf, 0, buf.length);
                        ds.receive(pack);
                        Log.e(TAG, "UDPThread receive length => " + pack.getLength());
                        if (pack.getLength() > 0) {
                            if (mCallback != null) {
                                mCallback.onDataReceived(pack.getData(), pack.getLength());
                            }
                        }
                  }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    ds.close();
                }
            }

            Log.e(TAG, "UDPThread exit");
        }
    }
}
