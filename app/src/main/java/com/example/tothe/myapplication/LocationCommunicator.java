package com.example.tothe.myapplication;

import io.socket.client.*;
import io.socket.emitter.Emitter;

/**
 * Created by tothe on 3/13/16.
 */
public class LocationCommunicator {

    private Socket socket;

    public LocationCommunicator(String event) {
        this.socket = null;

        try {
            final Socket socket;
            socket = IO.socket("http://routetracker-tudorb.rhcloud.com:8000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    socket.emit("foo", "hi");
                    socket.disconnect();
                }

            }).on(event, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });


            socket.connect();

            this.socket = socket;
        } catch (Exception e) {
            System.out.print(e);

        }


    }

    void NotifyServer(String event, String message) {
        if (socket != null) {
            socket.emit(event, message);
        }
    }
}
