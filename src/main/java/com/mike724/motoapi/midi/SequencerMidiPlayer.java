package com.mike724.motoapi.midi;

/**
 * Copyright (C) 2012 t7seven7t
 * Modified by dakota628 for agentgaming.net
 */

import com.mike724.motoapi.MotoAPI;
import com.mike724.motoapi.storage.HTTPUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.message.BasicNameValuePair;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.sound.midi.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author t7seven7t, dakota628
 */
class SequencerMidiPlayer implements Receiver, MidiPlayer {
    private final Sequencer sequencer;

    private final ArrayList<Player> tunedIn = new ArrayList<Player>();
    private final HashMap<Integer, Byte> channelPatches = new HashMap<Integer, Byte>();

    private boolean nowPlaying = false;
    private String midiName;

    private ArrayList<BukkitTask> tasks = new ArrayList<>();

    public SequencerMidiPlayer() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.getTransmitter().setReceiver(this);
    }

    public void tuneIn(Player player) {
        tunedIn.add(player);
    }

    public void tuneOut(Player player) {
        tunedIn.remove(player);
    }

    public boolean isNowPlaying() {
        return nowPlaying;
    }

    @Override
    public void playNextSong() {
        //we no longer need this method :)
    }

    public boolean isTunedIn(Player p) {
        return tunedIn.contains(p);
    }

    public void stopPlaying() {
        sequencer.stop();

        for (BukkitTask t : tasks) {
            t.cancel();
        }
    }

    public InputStream getMidiStream(String name) {
        byte[] midiBytes = null;
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("n", name));
            String out = doPost("https://agentgaming.net/api/get_midi.php", params);

            if (out.trim() == "0") {
                System.exit(0);
            }

            midiBytes = Base64.decode(out);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (midiBytes == null) return null;

        return new ByteArrayInputStream(midiBytes);
    }

    public void playSong(String midiName) {
        this.midiName = midiName;

        InputStream midiStream = getMidiStream(midiName);
        if (midiStream == null) return;

        try {
            Sequence midi = MidiSystem.getSequence(midiStream);
            sequencer.setSequence(midi);
            sequencer.start();
            nowPlaying = true;
        } catch (InvalidMidiDataException ex) {
            System.err.println("Invalid midi file: " + midiName);
        } catch (IOException e) {
            System.err.println("Can't read file: " + midiName);
        }

        BukkitTask task = new BukkitRunnable() {

            @Override
            public void run() {
                if (!nowPlaying)
                    this.cancel();

                if (!sequencer.isRunning() || sequencer.getMicrosecondPosition() > sequencer.getMicrosecondLength()) {
                    stopPlaying();
                }

            }

        }.runTaskTimer(MotoAPI.getInstance(), 20L, 20L);

        tasks.add(task);
    }

    @Override
    protected void finalize() {
        sequencer.close();
    }

    @Override
    public void close() {
        // We don't really need this in this case, thanks anyway oracle <3
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (!(message instanceof ShortMessage))
            return; // Not interested in meta events

        ShortMessage event = (ShortMessage) message;
        if (event.getCommand() == ShortMessage.NOTE_ON) {

            int midiNote = event.getData1();
            float volume = event.getData2() / 127;

            if (volume == 0)
                volume = 1;

            int note = Integer.valueOf((midiNote - 6) % 24);

            int channel = event.getChannel();
            byte patch = 1;
            if (channelPatches.containsKey(channel))
                patch = channelPatches.get(channel);

            for (Player player : tunedIn) {
                //Play the sound to each player tuned in
                player.playSound(player.getLocation(), Instrument.getInstrument(patch, channel), volume, NotePitch.getPitch(note));
            }

        } else if (event.getCommand() == ShortMessage.PROGRAM_CHANGE) {
            channelPatches.put(event.getChannel(), (byte) event.getData1());
        } else if (event.getCommand() == ShortMessage.STOP) {
            stopPlaying();
            playNextSong();
        }
    }

    //Do post method from motoloader
    private static String key = "nXWvOgfgRJKBbbzowle1";
    private static String username = "jxBkqvpe0seZhgfavRqB";
    private static String password = "RXaCcuuQcIUFZuVZik9K";

    public static String doPost(String url, List<NameValuePair> params) throws Exception {
        List<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("key", key));
        data.addAll(params);

        Credentials creds = new UsernamePasswordCredentials(username, password);

        return HTTPUtils.basicAuthPost(url, data, creds);
    }
}