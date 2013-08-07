package com.mike724.motoapi.push;

import com.google.gson.Gson;
import com.mike724.motoapi.MotoAPI;
import net.minecraft.v1_6_R2.org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.net.Socket;

public class MotoPush {
    private String apiKey;
    private Socket socket;
    private BufferedReader is;
    private PrintStream os;
    private Gson gson = new Gson();

    public MotoPush() throws IOException {
        socket = new Socket("http://agentgaming.net/", 8114);
        os = new PrintStream(socket.getOutputStream());
        is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(handleMessages).start();
    }

    public void push(MotoPushData data) {
        String json = new String(Base64.encode(gson.toJson(data).getBytes()));
        os.println(json);
    }

    Runnable handleMessages = new Runnable() {
        @Override
        public void run() {
            String line;
            try {
                while ((line = is.readLine()) != null) {
                    String data = new String(Base64.decode(line));
                    MotoPushData mpd = gson.fromJson(data,MotoPushData.class);
                    if(mpd != null) MotoAPI.getInstance().getServer().getPluginManager().callEvent(new MotoPushEvent(mpd));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
