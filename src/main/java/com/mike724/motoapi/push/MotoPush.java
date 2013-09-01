package com.mike724.motoapi.push;

import com.amazonaws.util.json.JSONObject;
import com.google.gson.Gson;
import com.mike724.motoapi.MotoAPI;
import com.mike724.motoapi.storage.HTTPUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class MotoPush {
    private String apiKey;
    private SSLSocket sslSocket;
    private BufferedReader is;
    private BufferedWriter os;
    private Gson gson = new Gson();
    private Boolean isConnected = true;

    public MotoPush() throws IOException {
        connect();

        new Thread(handleMessages).start();

        new Thread(ping).start();
    }

    private void connect() throws IOException {
        Socket socket = new Socket("agentgaming.net", 8114);
        sslSocket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(
                socket,
                socket.getInetAddress().getHostAddress(),
                socket.getPort(),
                true);
        os = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
        is = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

        os.write("nXWvOgfgRJKBbbzowle1," + Bukkit.getServer().getPort() + "\n");
        os.flush();
    }

    public Boolean isConnected() {
        return isConnected;
    }

    public void push(MotoPushData data) {
        try {
            String json = Security.encrypt(Base64.encodeBase64String(gson.toJson(data).getBytes()), "9612/n1utzle//pa");
            os.write(json + "\n");
            os.flush();
            //if this exception is called the data is not valid so ignore it
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void cmd(String cmd, String... args) {
        String cmdStr = ":" + cmd;
        for (String s : args) cmdStr += "," + s;
        try {
            os.write(cmdStr + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIdentity(ServerType t, ServerState s) {
        cmd("setidentity", t.name(), s.name());
    }

    public JSONObject apiMethod(String method, String... args) {
        String url = "https://agentgaming.net:8115/" + method;
        for (String s : args) url += "/" + s;
        try {
            String out = HTTPUtils.basicAuth(url, new UsernamePasswordCredentials("jxBkqvpe0seZhgfavRqB", "RXaCcuuQcIUFZuVZik9K"));
            return new JSONObject(out);
        } catch (Exception e) {
            return null;
        }
    }

    //Ping the server every 5 seconds to make sure we are still connected
    private Runnable ping = new Runnable() {
        @Override
        public void run() {
            boolean reconnect = true;

            while (reconnect) {
                try {
                    os.write("::ping\n");
                    os.flush();
                    isConnected = true;
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    reconnect = false;
                    connectionFailed();
                } catch (IOException e) {
                    try {
                        connect();
                        isConnected = true;

                        //Call event sync
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MotoAPI.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                MotoAPI.getInstance().getServer().getPluginManager().callEvent(new MotoPushReconnect());
                            }
                        });

                        Bukkit.getLogger().info("Successfully reconnected to MotoPush.");
                    } catch (IOException e1) {
                        connectionFailed();
                    }
                }
            }
        }
    };

    //Handle MotoPush messages
    private Runnable handleMessages = new Runnable() {
        @Override
        public void run() {
            String line;

            try {
                while ((line = is.readLine()) != null) {
                    String data = null;
                    try {
                        data = Security.decrypt(line, "9612/n1utzle//pa");
                    } catch (Exception e) {
                        continue;
                    }
                    data = new String(Base64.decodeBase64(data));
                    final MotoPushData mpd = gson.fromJson(data, MotoPushData.class);
                    if (mpd != null) {
                        //Call event sync
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MotoAPI.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                MotoAPI.getInstance().getServer().getPluginManager().callEvent(new MotoPushEvent(mpd));
                            }
                        });

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    };

    //The connection has failed. That is not good. I'm not really sure if this is the best way to handle this.
    private void connectionFailed() {
        isConnected = false;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MotoAPI.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player p : MotoAPI.getInstance().getServer().getOnlinePlayers()) {
                    p.kickPlayer("Unable to connect to persistence server!");
                }
            }
        });
    }
}
