package salaodoreino.automacao.runtime;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable{	
	
	private ServidorSocket servidor;
	private String ip;
	private Socket connection;
	private Thread thread;
	private PrintStream clienteOutput;

	public Cliente(Socket connection, ServidorSocket servidorSocket) throws IOException {
		servidor = servidorSocket;
		ip = connection.getInetAddress().getHostAddress();
		this.connection = connection;
		clienteOutput = new PrintStream(connection.getOutputStream());
		thread =  new Thread(this);
		sendResponse(new Mensagem(this, servidor, "conected", Origem.Servidor));
	}

	public void run() {
		Scanner s = null;
		try {
			s = new Scanner(connection.getInputStream());
			while(s.hasNextLine()){
				Mensagem msg = new Mensagem(this, servidor, s.nextLine(), Origem.Cliente);
				Mensagem resposta = servidor.clientSendMessage(msg);
				sendResponse(resposta);
			}
			s.close();
			removeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			servidor.errorWhenListeningCliente(this, e);
		}
	}

	private void removeConnection() {
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		servidor.remove(this);
	}

	public void sendResponse(Mensagem resposta) {
		clienteOutput.println("servidor = "+resposta.getServidor().getHost()+", mensagem->"+resposta.getMensagem());		
	}

	public void closeConnection() throws IOException {		
		if (thread.isAlive()) {
			thread.interrupt();
		}
		if (!connection.isClosed()) {
			connection.close();
		}		
	}

	public String getIp() {		
		return ip;
	}

	public void startConn(){
		thread.start();
	}
	
}
