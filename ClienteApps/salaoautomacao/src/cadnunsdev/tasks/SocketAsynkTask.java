package cadnunsdev.tasks;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;

public class SocketAsynkTask extends AsyncTask<String, String, Void>{

	private Socket socket;
	private Scanner scanner;
	private PrintStream serverPrintStream;
	private SocketListener listener;
	
	public SocketAsynkTask(SocketListener listener) {
		this.listener = listener;
	}
	

	@Override
	protected Void doInBackground(String... params) {
		if(serverPrintStream == null){
			try {
//	    		InetAddress inet = InetAddress.getByName(listener.getIp());
				socket = new Socket(listener.getIp(), listener.getHostPort());
				scanner = new Scanner(socket.getInputStream());
				
				serverPrintStream = new PrintStream(socket.getOutputStream());					
				
			} catch (IOException e) {
				publishProgress("ocorreu um erro ao iniciar servico em "+listener.getIp()+":"+listener.getHostPort()+
						" >>> "+ e.getMessage());
			}catch (NetworkOnMainThreadException nmtEx){
				publishProgress("Erro >>> conexao a rede na UIThread");
				nmtEx.printStackTrace();
			}
//			publishProgress("conectado");
		}
		boolean running = true;
		do{
			
			while(scanner.hasNextLine()){
				publishProgress(scanner.nextLine());
			}
			running = false;
		}while(listener.keepTaskRunnig() && running);
		listener.notify("ended connection");
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		listener.notify(values[0]);
	}


	public void sendMessage(String string) {
		serverPrintStream.println(string);	
	}
}
