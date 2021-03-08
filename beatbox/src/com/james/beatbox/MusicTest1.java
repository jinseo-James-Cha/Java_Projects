package com.james.beatbox;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class MusicTest1 {

	public void play() {
		
		try {
		Sequencer sequencer = MidiSystem.getSequencer();
		
		System.out.println("first commit with Sequencer class and try~catch statement");
		
		}catch(MidiUnavailableException ex) {
			ex.getStackTrace();
		}
	}
	
	public static void main(String[] args) {

		
		MusicTest1 music = new MusicTest1();
		
		music.play();
	}

}
