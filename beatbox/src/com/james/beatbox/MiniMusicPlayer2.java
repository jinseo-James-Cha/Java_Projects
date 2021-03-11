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
			
			for( int i = 60; i < 120; i += 5) {
				
				//noteOn
				track.add(makeEvent(144,1,i,120,i));
				
				// 176은 이벤트 유형이 ControllerEvent라는 것을 지정하기 위한 숫
				// 127은 인자로 전달하여 별도의 ControllerEvent를 추가
				// 기능적인 면은 없지만 연주될때마다 Event를 받아 올 수 있다.
				// 144번의 이벤트 유형(NoteOn)의 EVent를 받아올수 없기 때문에
				// 간접적이나마 144이벤트가 언제 일어났는지 알 수 있다.
				track.add(makeEvent(176, 1, 127, 0, i));
				
				// noteOff
				track.add(makeEvent(128,1,i,120,i+2));
			}
			
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(300);
			sequencer.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * Event handler메소드이다.
	 * ControllerEventListener 인터페이스의 추상 메소드이며, 반드시 override하게 되어있다.
	 * Event가 발생할때마다 자동으로 메소드가 실행된다.
	 * */
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
