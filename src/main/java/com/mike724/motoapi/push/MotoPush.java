package com.mike724.motoapi.push;

import com.google.gson.Gson;
import com.mike724.motoapi.MotoAPI;

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
        os = new PrintStream(socket.getOutputStream());
        is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(handleMessages).start();
    }

    public void push(MotoPushData data) {
        try {
            String json = Security.encrypt(new String(gson.toJson(data).getBytes()), "9612/n1utzle//pa");
            os.println(json);
        } catch (Exception e) {}
    }

    Runnable handleMessages = new Runnable() {
        @Override
        public void run() {
            String line;
            try {
                while ((line = is.readLine()) != null) {
                    String data = new String(line);
                    System.out.println(data.length() + " " + "9612/n1utzle//pa".length());
                    data = Security.decrypt(data,"9612/n1utzle//pa");
                    MotoPushData mpd = gson.fromJson(data,MotoPushData.class);
                    if(mpd != null) MotoAPI.getInstance().getServer().getPluginManager().callEvent(new MotoPushEvent(mpd));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
        }
    };
}
