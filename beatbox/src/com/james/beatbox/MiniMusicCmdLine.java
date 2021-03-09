package com.james.beatbox;

import java.util.Scanner;

import javax.sound.midi.*;

public class MiniMusicCmdLine {

	public static void main(String[] args) {

		MiniMusicCmdLine mini = new MiniMusicCmdLine();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("악기 지정하는 숫자를 입력하세요 : ");
		int instrument = sc.nextInt();
		System.out.print("음을 지정하는 숫자를 입력하세요 : ");
		int note = sc.nextInt();
		
		mini.play(instrument, note);
		
	}
	
	public void play(int instrument, int note) {
		
		try {
			Sequencer player = MidiSystem.getSequencer();
			player.open();

			Sequence seq = new Sequence(Sequence.PPQ, 4);
			
			Track track = seq.createTrack();
			
			MidiEvent event = null;
			
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 100);
			MidiEvent changeInstrument = new MidiEvent(first,1);
			track.add(changeInstrument);
			
			ShortMessage a = new ShortMessage();
			a.setMessage(144, 1, note, 100);
			MidiEvent noteOn = new MidiEvent(a, 1);
			track.add(noteOn);
			
			ShortMessage b = new ShortMessage();
			a.setMessage(128, 1, note, 100);
			MidiEvent noteOff = new MidiEvent(b, 16);
			track.add(noteOff);
			
			player.setSequence(seq);
			player.start();
						
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
