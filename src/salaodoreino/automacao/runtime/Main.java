package salaodoreino.automacao.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javazoom.jl.decoder.JavaLayerException;

public class Main {

	public static void main(String[] args) {
		lerArquivoDoDisco();
		
	}

	private static void lerArquivoDoDisco() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				File file = fc.getSelectedFile();
				
				AudioPlayer player = null;
				Thread th = null;
				try {
					String ext = fc.getTypeDescription(file);
					if ( ext.toLowerCase().contains("mp3") ) {	
						
						
						player = new AudioPlayer(file);
//						player.play();
						th = new Thread(player);
						th.start();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				Scanner sc = new Scanner(System.in);
				while(true){
					String input = sc.nextLine();
					if(input.equals("fechar")){
						player.stop();
						th.interrupt();
						System.exit(0);
					}
					if(input.equals("pausar")){
						player.pause();
					}
					if(input.equals("play")){
						try {
							player.resume();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
	}

}
