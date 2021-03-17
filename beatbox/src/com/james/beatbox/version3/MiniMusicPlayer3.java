package com.james.beatbox.version3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.sound.midi.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MiniMusicPlayer3 {

	static JFrame f = new JFrame("version - 3 music Video");
	static MyDrawPanel m1;
	
	public static void main(String[] args) {

		MiniMusicPlayer3 mini = new MiniMusicPlayer3();
		mini.go();
	}
	
	public void setUpGui() {
		
		m1 = new MyDrawPanel();
		f.setContentPane(m1);
		f.setBounds(30, 30, 300, 300);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void go() {
		setUpGui();
		
		try {
			
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addControllerEventListener(m1, new int[] {127});
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			int r = 0;
			for ( int i = 0; i < 150; i += 4) {
				
				// 메세지가 언제 실행되는지를 나타내는 i
				r = (int) ((Math.random() * 20) + 70);
				//r += 8;
				track.add(makeEvent(144, 1, r, 100, i));
				track.add(makeEvent(176, 1, 127, 0, i));
				track.add(makeEvent(128,1, r, 100, i+2));
			}
			
			sequencer.setSequence(seq);
			sequencer.start();
			sequencer.setTempoInBPM(200);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
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


class MyDrawPanel extends JPanel implements ControllerEventListener{

	boolean msg = false;
	
	public void controlChange(ShortMessage event) {
		msg = true;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		if(msg) {
			
			Graphics2D g2 = (Graphics2D) g;
			
			int r = (int) (Math.random() * 250);
			int gr = (int) (Math.random() * 250);
			int b = (int) (Math.random() * 250);

			g2.setColor(new Color(r, gr, b));
			
			int ht = (int) ((Math.random() * 120) + 10);
			int width = (int) ((Math.random() * 120) + 10);
			int x = (int) ((Math.random() * 40 ) + 10);
			int y = (int) ((Math.random() * 40) + 10);
			g2.fillRect(x, y, ht ,width);
			
			msg = false;
			
		}
	}
}

