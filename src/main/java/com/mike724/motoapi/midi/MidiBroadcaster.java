package com.mike724.motoapi.midi;

import com.mike724.motoapi.MotoAPI;
import org.bukkit.entity.Player;

import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;

@SuppressWarnings("UnusedDeclaration")
public class MidiBroadcaster {
    private SequencerMidiPlayer broadcastPlayer;
    private ArrayList<SequencerMidiPlayer> midiPlayers;

    public MidiBroadcaster() throws MidiUnavailableException {
        broadcastPlayer = new SequencerMidiPlayer();
        midiPlayers = new ArrayList<>();
    }

    public void broadcastMidi(String name) {
        broadcastPlayer.playSong(name);
        for (Player p : MotoAPI.getInstance().getServer().getOnlinePlayers()) {
            stopMidiForPlayer(p);
            broadcastPlayer.tuneIn(p);
        }
    }

    public void tuneIntoBroadcast(Player p) {
        stopMidiForPlayer(p);
        broadcastPlayer.tuneIn(p);
    }

    public void playMidiForPlayer(String name, Player p) {
        playMidiForPlayers(name, p);
    }

    public void playMidiForPlayers(String name, Player... players) {
        SequencerMidiPlayer smp = null;
        try {
            smp = new SequencerMidiPlayer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        if (smp == null) return;
        else midiPlayers.add(smp);

        smp.playSong(name);

        for (Player p : players) {
            stopMidiForPlayer(p);
            smp.tuneIn(p);
        }
    }

    public void stopMidiForPlayer(Player p) {
        if (broadcastPlayer.isTunedIn(p)) {
            broadcastPlayer.tuneOut(p);
        } else {
            for (SequencerMidiPlayer midiPlayer : midiPlayers) {
                if (midiPlayer.isTunedIn(p)) midiPlayer.tuneOut(p);
            }
        }
    }
}
