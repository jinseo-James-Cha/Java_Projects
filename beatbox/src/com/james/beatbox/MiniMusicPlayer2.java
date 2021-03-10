package com.james.beatbox;

import javax.sound.midi.*;

public class MiniMusicPlayer2 implements ControllerEventListener{
	// ControllerEventListener 구현하여
	// ControllerEvent를 감시하고 그에 따른 음악플레이어 구현하기
	
	public static void main(String[] args) {

		MiniMusicPlayer2 mini = new MiniMusicPlayer2();
		mini.go();
	}

	public void go() {
	
		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			// 이벤트 시퀀서에 등록
			// ControllerEvent 목록을 나타내는 int[]을 두번째 인자로 요구
			int[] eventsIWant = {127};
			sequencer.addControllerEventListener(this, eventsIWant);
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			for( int i = 40; i < 100; i += 4) {
				
				track.add(makeEvent(144,1,i,100,i));
				
				// 176은 이벤트 유형이 ControllerEvent라는 것을 지정하기 위한 숫
				// 127은 인자로 전달하여 별도의 ControllerEvent를 추가
				// 기능적인 면은 없지만 연주될때마다 Event를 받아 올 수 있다.
				
				track.add(makeEvent(176, 1, 127, 0, i));
				track.add(makeEvent(128,1,i,100,i+2));
			}
			
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void controlChange(ShortMessage event) {
		System.out.println("la");
	}
	
	
	/*
	 * 음이 증가하여 여러 음표를 Track에 담고 실행하기 위해 method로 구현 
	 * */
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return event;
	}
	

}
