package com.mike724.motoapi.push;

import com.google.gson.Gson;
import com.mike724.motoapi.MotoAPI;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class MotoPush {
    private String apiKey;
    private Socket socket;
    private BufferedReader is;
    private PrintStream os;
    private Gson gson = new Gson();

    public MotoPush() throws IOException {
        socket = new Socket("agentgaming.net", 8114);
        SSLSocket sslSocket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(
                socket,
                socket.getInetAddress().getHostAddress(),
                socket.getPort(),
                true);

        os = new PrintStream(sslSocket.getOutputStream());
        is = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

        os.println("nXWvOgfgRJKBbbzowle1");

        String line;
        while ((line = is.readLine()) != null) {
            if(line.trim() == "authed") break;
        }

        new Thread(handleMessages).start();
    }

    public void push(MotoPushData data) {
        try {
            String json = Security.encrypt(new String(gson.toJson(data).getBytes()), "9612/n1utzle//pa");
            os.println(json);
            //if this exception is called the data is not valid so ignore it
        } catch (Exception ignored) {
        }
    }

    public void cmd(String cmd, String... args) {
        String cmdStr = ":" + cmd;
        for(String s : args) {
            cmdStr += "," + s;
        }
        os.println(cmdStr);
    }

    Runnable handleMessages = new Runnable() {
        @Override
        public void run() {
            String line;
            try {
                while ((line = is.readLine()) != null) {
                    String data = new String(line);
                    data = Security.decrypt(data, "9612/n1utzle//pa");
                    MotoPushData mpd = gson.fromJson(data, MotoPushData.class);
                    if (mpd != null)
                        MotoAPI.getInstance().getServer().getPluginManager().callEvent(new MotoPushEvent(mpd));
                }
            } catch (IOException e) {
                e.printStackTrace();
                //if this exception is called the data is not valid so ignore it
            } catch (Exception e) {
            }
        }
    };
}
