package com.mike724.motoapi.midi;

/**
 * Copyright (C) 2012 t7seven7t
 */

import org.bukkit.entity.Player;

/**
 * @author t7seven7t
 */
interface MidiPlayer {

    public void tuneIn(Player player);

    public void tuneOut(Player player);

    public void stopPlaying();

    public boolean isNowPlaying();

    public void playNextSong();

    public void playSong(String midiName);

}