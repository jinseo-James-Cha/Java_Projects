package com.james.beatbox;

import javax.sound.midi.*;


public class MusicTest1 {

	public void play() {
		
		try {
		
			// Sequencer class : 실제로 음악을 재생하게 하는 역할
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			
			// Sequence class : Sequencer에서 연주될 악보를 의미
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			
			// Track class : 모든 음악적 데이터를 담는 실제 정보가 들어있는 클래스
			Track track = seq.createTrack();
			
			
			// Message를 통하여 "무엇을" 할지 지시사항을 넣는다.
			ShortMessage sm = new ShortMessage();
			// 144 = Note On(음을 누른다), 1 = 채널, 44 = 44번째 음, 100(기본값) = 누르는 속도
			sm.setMessage(144, 1, 44, 100);

			// MediEvent 인스턴스를 할일 = sm, 할 시기 = 1로 생성한다.
			// 1의 의미는 첫번째 박자(Beat)
			MidiEvent noteOn = new MidiEvent(sm,1);
			track.add(noteOn);
			
			ShortMessage sm2 = new ShortMessage();
			
			// 128 = Note Off(누른 음을 뗀다), 1 = 채널, 44 = 44번째 음, 100(기본값) = 누르는 속도
			sm2.setMessage(128, 1, 44, 100);
			MidiEvent noteOff = new MidiEvent(sm2, 16);
			track.add(noteOff);
			
			player.setSequence(seq);
			
			player.start();
		
		} catch(MidiUnavailableException e) {
			e.getStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		
		MusicTest1 music = new MusicTest1();
		
		music.play();
	}

}
