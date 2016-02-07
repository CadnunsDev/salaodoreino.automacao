package salaodoreino.automacao.runtime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class AudioPlayer implements Runnable {
	private URL _filePath;
	private Player audioPlayer;
	private Clip clip;
	private Player player;
	private int positionCasePaused;
	private File file;
	
	public AudioPlayer(File file) throws Exception {
		this.file = file;
		buildPlayer() ;		
	}
	
	void buildPlayer() throws Exception{
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		player = new Player(bis);
	}
//	public AudioPlayer(URL filePath)  throws Exception{
////		_filePath = filePath;
////		audioPlayer = Manager.createPlayer(_filePath);
////			
//		AudioInputStream audioInputStream =
//		        AudioSystem.getAudioInputStream(
//		            this.getClass().getResource(_filePath.getPath()));
//		    clip = AudioSystem.getClip();
//		    clip.open(audioInputStream);
//		    
//	}

	public void play() throws JavaLayerException {
		// TODO Auto-generated method stub
//		audioPlayer.start();
//		clip.start();
		player.play();
	}

	public boolean isPlaying() {
		
		return !player.isComplete();
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}

	public void stop() {
		player.close();		
	}

	public void pause() {
		positionCasePaused =  player.getPosition();
		player.close();
	}
	
	public void resume() throws Exception{
		if(isPlaying())
			stop();
		buildPlayer();
		player.play();
	}

}
