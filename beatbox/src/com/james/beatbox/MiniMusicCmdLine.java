package com.james.beatbox;

import java.util.Scanner;

import javax.sound.midi.*;

public class MiniMusicCmdLine {

	public static void main(String[] args) {

		MiniMusicCmdLine mini = new MiniMusicCmdLine();
		Scanner sc = new Scanner(System.in);
		
		
		//악기의 숫자와 음은 0~127이하를 입력 할 수 있다.
		System.out.print("악기 지정하는 숫자를 입력하세요 : ");
		int instrument = sc.nextInt();
		System.out.print("음을 지정하는 숫자를 입력하세요 : ");
		int note = sc.nextInt();
		
		mini.play(instrument, note);
		sc.close();
		
	}
	
	public void play(int instrument, int note) {
		
		try {
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			//MidiEvent event = null;
			
			ShortMessage first = new ShortMessage();
			// 192 : 악기 변경 메세지, 1: 채널, 연주자, instrument : 변경할 악기
			// 192번이라는 악기변경 메세지를 가지고 악기 세팅을 하였다.
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first,1);
			track.add(changeInstrument);
			
			// 메세지 a는 음을 누르는 역할을 하며, 1번 채널(연주자)의 내용이고
			// 음 높이는 note로써 사용자에게 입력받은 값이고,
			// 그 누르는 힘은 100으로 한다.
			ShortMessage a = new ShortMessage();
			a.setMessage(144, 1, note, 100);
			// a message를 1번째 비트에 실행한다.
			MidiEvent noteOn = new MidiEvent(a, 1);
			track.add(noteOn);
			
			// 메세지 b는 음을 떼는 역할
			ShortMessage b = new ShortMessage();
			b.setMessage(128, 1, note, 100);
			// b message를 16번째 비트에 실행한다.
			MidiEvent noteOff = new MidiEvent(b, 16);
			track.add(noteOff);
			
			/*
			 * 2번째 채널(연주자)에 입력받은 키높이의 - 40을 하여
			 * 화음을 넣을 수 있는지 테스트 하였다.
			 * */
			
			ShortMessage c = new ShortMessage();
			c.setMessage(144, 2, note - 40, 100);
			// a message를 1번째 비트에 실행한다.
			MidiEvent noteOn2 = new MidiEvent(c, 1);
			track.add(noteOn2);
						
			// 메세지 b는 음을 떼는 역할
			ShortMessage d = new ShortMessage();
			d.setMessage(128, 2, note - 40, 100);
			// b message를 16번째 비트에 실행한다.
			MidiEvent noteOff2 = new MidiEvent(d, 16);
			track.add(noteOff2);
			
			player.setSequence(seq);
			player.start();
						
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
