package salaodoreino.automacao.runtime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorSocket implements Runnable{
	private IServerListener _listener;
	private int PORT;
	private ArrayList<Cliente> listClientes;
	private ServerSocket servidor;
	private String host;
	
	public ServidorSocket(int port, IServerListener listener){
		PORT = port;
		_listener = listener;
		host = "localhost<not opened>";
		listClientes = new ArrayList<Cliente>();
	}


	public void run(){
		try {
			servidor = new ServerSocket(PORT);
			host = servidor.getInetAddress().getHostAddress()+":"+servidor.getLocalPort();
		} catch (IOException e) {
			_listener.ErrorOnStartServer(e);
		}
		if(servidor != null){
			startPollingClients();
		}
		
	}


	private void startPollingClients() {
		while(true){
			try {
				Cliente cliente = new Cliente(servidor.accept(), this);
				listClientes.add(cliente);
				_listener.newConnectionAcepted(cliente);
				
			} catch (IOException e) {
				_listener.ErrorOnAceptClient(e);
			}
		}
	}


	public void errorWhenListeningCliente(Cliente cliente, IOException e) {
		try {
			cliente.closeConnection();
		} catch (IOException ex) {
			
		}
		listClientes.remove(cliente);
		_listener.Notify("Cliente desconectado devido a falha na leitura dos dados. Erro:\n\t"+e.getMessage());
	}


	public String getHost() {
		return host;
	}

	public Mensagem clientSendMessage(Mensagem msg) {
		
		return _listener.clientSendMessage(msg);
	}
}
