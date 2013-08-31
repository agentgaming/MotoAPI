package com.mike724.motoapi.midi;

/**
 * Copyright (C) 2012 t7seven7t
 * Modified by dakota628 for agentgaming.net
 */

import com.mike724.motoapi.MotoAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.sound.midi.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author t7seven7t, dakota628
 */
class SequencerMidiPlayer implements Receiver, MidiPlayer {
    private final Sequencer sequencer;

    private final List<Player> tunedIn = new ArrayList<Player>();
    private final Map<Integer, Byte> channelPatches = new HashMap<Integer, Byte>();

    private boolean nowPlaying = false;
    private String midiName;

    private ArrayList<BukkitTask> tasks;

    public SequencerMidiPlayer() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.getTransmitter().setReceiver(this);
    }

    public void tuneIn(Player player) {
        tunedIn.add(player);

        player.sendMessage(ChatColor.AQUA + "Now playing: " + ChatColor.YELLOW + midiName);
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
        //TODO: implement getting this OTA, for now we will use local files
        File midi = new File(MotoAPI.getInstance().getDataFolder(), name);
        try {
            return new FileInputStream(midi);
        } catch (FileNotFoundException e) {
            return null;
        }
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

        for (Player player : tunedIn) {

            player.sendMessage(ChatColor.AQUA + "Now playing: " + ChatColor.YELLOW + midiName);

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

}