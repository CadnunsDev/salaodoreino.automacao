package salaodoreino.automacao.runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.sun.xml.internal.ws.util.StringUtils;

import utils.DateTimeUtils;
import utils.FileUtils;
import javazoom.jl.decoder.JavaLayerException;

public class Main implements IServerListener{	
	public static final String CANTICOS_FOLDER = "E:\\musicas\\canticos";

	public static final String CANTICOS_FILEPREFIX = "iasn_T_";
	public static void main(String[] args) {
		//lerArquivoDoDisco();
		Main mainObject = new Main();
		int port = 25346;
		ServidorSocket servidor = new ServidorSocket(port, mainObject);
		Scanner sc = new Scanner(System.in);
		Thread threadServidor = new Thread(servidor);
		threadServidor.run();
		try {
			threadServidor.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(1000);
			print("apos um segundo...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static void criaCliente(String host, int port) throws UnknownHostException, IOException {
		Socket cliente = new Socket(host, port);// 127.0.0.1 é o endereço local da maquina
	     System.out.println("O cliente se conectou ao servidor!");
	     
	     Scanner teclado = new Scanner(System.in);
	     PrintStream saida = new PrintStream(cliente.getOutputStream());
	     
	     while (teclado.hasNextLine()) {
	       saida.println(teclado.nextLine());
	     }
	     
	     saida.close();
	     teclado.close();
	     cliente.close();		
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

	private Thread audioThread;

	private AudioPlayer audioPlayer;

	public void whenClientSendMessage(Mensagem msg) {
		print(msg.getCliente().getIp()+" diz: "+msg.getMensagem());		
	}

	private static void print(String string) {
		System.out.println(string);
	}

	public void ErrorOnStartServer(IOException e) {
		print(e.getMessage());
	}

	public void ErrorOnAceptClient(IOException e) {
		print(e.getMessage());		
	}

	public void newConnectionAcepted(Cliente cliente) {
		print(DateTimeUtils.getDateNow() +" >> Nova conexão do ip "+cliente.getIp());		
	}

	public void Notify(String string) {
		print(DateTimeUtils.getDateNow() +" >> Notificacao do servidor : "+string);
	}

	public Mensagem clientSendMessage(Mensagem msg) {
		String[] args = msg.getMensagem().split(" ");
		String retMsg = "opcao não encontrada";
		if (args.length > 0) {
			if (args.length >= 2) {
				if (args[0].equals("cantico")) {
					retMsg = "executando cantico " + args[1]
							+ ", tema 'jeová é o refugio'";
				}
			}
			if (args[0].equals("listar")) {
				String[] arquivos = FileUtils
						.fileNamesOnFolder("E:\\musicas\\canticos");
				StringBuilder bulder = new StringBuilder(
						"todos os canticos \n#listaCanticos");
				for (String string : arquivos) {
					bulder.append("\n" + string);
				}
				bulder.append("\n" + "#listaCanticos");
				retMsg = bulder.toString();
			}
			if(args[0].equals("tocar_cantico")){				
				if(args.length >= 2){
					int numCantico = 0;					
					try {
						numCantico = Integer.parseInt(args[1]);
						if(numCantico > 0 && executeCantico(numCantico)){
							retMsg = "executing cantico "+numCantico;
						}else{
							retMsg = "não foi possivel executar o cantico";
						}
					} catch (NumberFormatException e) {
						retMsg = "Número do Cantico[parametro 2] invalido";
					}
				}else{
					retMsg = "Segundo parametro não informado";
				}				
			}
		}
		return new Mensagem(msg.getCliente(), msg.getServidor(), retMsg, Origem.Servidor);
	}

	private boolean executeCantico(int numCantico) {
		
		String[] arquivos = FileUtils
				.fileNamesOnFolder(CANTICOS_FOLDER);
		String fileNameExpected = CANTICOS_FILEPREFIX+String.format("%03d", numCantico)+".mp3";
		boolean foundFile  = false;
		for (String arquivo : arquivos) {			
			if(arquivo.equals(fileNameExpected)){
				try {
					if(audioThread != null && audioThread.isAlive()){
						audioPlayer.stop();
						audioThread.interrupt();
					}
					audioPlayer = new AudioPlayer(new File(CANTICOS_FOLDER+"\\"+arquivo));
					audioThread = new Thread(audioPlayer);
					audioThread.start();
					foundFile = true;
					print("executing "+  fileNameExpected);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return foundFile;
	}

	public void clientDisconected(Cliente cliente) {
		print(DateTimeUtils.getDateNow() +" >> client "+cliente.getIp()+" desconectou");		
	}

}
