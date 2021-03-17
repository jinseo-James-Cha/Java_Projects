package com.james.beatbox.version4;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BeatBox {

	JFrame theFrame;
	JPanel mainPanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare",
			"Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
			"Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
	
	int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
	
	public static void main(String[] args) {

		new BeatBox().buildGUI();
	}
	
	public void buildGUI() {
		theFrame = new JFrame("James' BeatBox");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		// 비어있는 경계선 (empty border)을 사용하여 패널 둘레와 구성요소가 들어가는 자리사이에 빈공간을 생성 
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo Up");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo Down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i = 0; i < 16; i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		// 체크상자를 만들고 모든값을 체크되지 않은 상태로 설정하고, ArrayList와 GUI패널에 추
		for(int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		
		setUpMidi();
		
		theFrame.setBounds(50,50,300,300);
		theFrame.pack();
		theFrame.setVisible(true);
		
	}
	
	public void setUpMidi() {
		
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/* 
	 * 중요한 부분
	 * 체크상자의 상태를 MIDI이벤트로 바꾼 다음 그 이벤트를 트랙에 추가한다.
	 *  */
	public void buildTrackAndStart() {
		// 각 악기의 열여섯 박자에 대한 값을 원소가 16개인 배열에 저장
		// 어떤 악기가 특정 박자에서 연주되어야하면 그 원소의 값에 건반 번호를 넣습니다.
		// 반대로, 연주되어야하지 않는다면 0을 넣는다.
		int[] trackList = null;
		
		// 기존트랙을 삭제하고 새로운 트랙을 만든다.
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		//열 16개(베이스,콩고 등) 모두에 대해 같은 작업을 처리한다.
			for(int i = 0; i < 16; i++) {
				
				trackList = new int[16];
				
				//어떤 악기인지를 나타내는 건반 번호를 설정
				//베이스 하이햇등 각 악기에 해당하는 실제 미디번호가 instruments[]에 들어있음.
				int key = instruments[i];
				
				for(int j = 0; j < 16; j++) {
					JCheckBox jc = checkboxList.get(j + (16 * i));
					if( jc.isSelected()) {
						trackList[j] = key;
					} else {
						trackList[j] = 0;
					}
				}
				
				makeTracks(trackList);
				track.add(makeEvent(176, 1, 127, 0, 16));
			}
			
			track.add(makeEvent(192, 9, 1, 0, 15));
			try {
				sequencer.setSequence(sequence);
				sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
				sequencer.start();
				sequencer.setTempoInBPM(120);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public class MyStartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrackAndStart();
		}
		
	}
	
	public class MyStopListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			sequencer.stop();
		}
		
	}
	
	public class MyUpTempoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
		}
		
	}
	
	public class MyDownTempoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 0.97));
		}
		
	}
	
	public void makeTracks(int[] list) {
		for(int i = 0; i < 16; i++) {
			int key = list[i];
			
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i+1));
			}
		}
		                           
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event= null;
		
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
