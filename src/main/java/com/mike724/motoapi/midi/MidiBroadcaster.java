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
    }

    public void broadcastMidi(String name) {
        for (Player p : MotoAPI.getInstance().getServer().getOnlinePlayers()) {
            stopMidiForPlayer(p);
            broadcastPlayer.tuneIn(p);
        }
        broadcastPlayer.playSong(name);
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
