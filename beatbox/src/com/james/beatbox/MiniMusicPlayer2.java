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
	
		
	}
	
	@Override
	public void controlChange(ShortMessage event) {
		// TODO Auto-generated method stub
		
	}
	
	

}
