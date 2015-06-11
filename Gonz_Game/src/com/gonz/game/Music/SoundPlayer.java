package com.gonz.game.Music;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	
	// Data structure
	private static HashMap<String, Clip> soundsMap;
	
	// Sounds paths
	public static final String SOUND_EFFECT_PATH = "/Sounds/SFX/";
	public static final String MUSIC_PATH = "/Sounds/Music/";
	
	public void run() {
		init();
	}
	
	public static void init() {
		
		soundsMap = new HashMap<String, Clip>();
		
	}
	
	public static void loadSound(String name, String dir) {
		
		if(soundsMap.get(name) != null) {
			
			System.err.println("[SOUND PLAYER]\t\tSound "+ name +" is already loaded");
			return;
		}
		
		Clip clip;
		
		try {
			
			InputStream in = SoundPlayer.class.getResourceAsStream(dir + name);
			InputStream bin = new BufferedInputStream(in);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bin);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
				);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();			
			clip.open(dais);			
			soundsMap.put(name, clip);
			
			System.out.println("[SOUND PLAYER]\t\t"+ name + " loaded");
			
		} catch (Exception e) {
			
			System.err.println("[SOUND PLAYER]\t\tError loading "+ name);
			e.printStackTrace();
			
		}		
		
	}
	
	public static void play(String name) {
		
		Clip c = soundsMap.get(name);
		
		if(c == null) {
			System.err.println("[SOUND PLAYER]\t"+ name +" does not exist");
			return ;
		}
		
		if(c.isRunning())	
			c.stop();
		
		c.setFramePosition(0);
		
		while(!c.isRunning())
			c.start();		
	}
	
	public static void play(String name, int i) {
		
		Clip c = soundsMap.get(name);
		
		if(c == null) {
			System.err.println("[SOUND PLAYER]\t"+ name +" does not exist");
			return ;
		}
		
		if(c.isRunning()) 
			c.stop();
		
		c.setFramePosition(i);
		
		while(!c.isRunning())
			c.start();		
	}
	
	public static void loop(String s) {
		loop(s, 0, 0, soundsMap.get(s).getFrameLength() - 1);
	}

	private static void loop(String s, int frame, int start, int end) {
		
		Clip c = soundsMap.get(s);
		
		if(c == null) 
			return;
		
		if(c.isRunning()) 
			c.stop();
		
		c.setLoopPoints(start, end);
		c.setFramePosition(frame);
		c.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	
	public static void stop(String name) {
		if(soundsMap.get(name) == null)	return ;
		if(soundsMap.get(name).isRunning())
			soundsMap.get(name).stop();
	}
	
	public static void close(String name) {
		stop(name);
		soundsMap.get(name).close();
	}
	
	public static boolean isPlaying(String name) {
		if(soundsMap.get(name) == null || !soundsMap.get(name).isRunning())
			return false;
		return true;
	}
	
}
