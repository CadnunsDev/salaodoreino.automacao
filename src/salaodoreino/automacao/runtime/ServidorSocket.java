package salaodoreino.automacao.runtime;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import utils.DateTimeUtils;
import utils.NetworkUtils;

public class ServidorSocket implements Runnable{
	private IServerListener _listener;
	private int PORT;
	private ArrayList<Cliente> listClientes;
	private ServerSocket servidor;
	private String host;
	private Thread threadPinService;
	
	public ServidorSocket(int port, IServerListener listener){
		PORT = port;
		_listener = listener;
		host = "localhost<not opened>";
		listClientes = new ArrayList<Cliente>();
		_listener.Notify("Servidor criado... pode rodar!!");
	}


	public void run(){
		_listener.Notify("Servidor iniciado,  ips = {"+NetworkUtils.getIpsFomThisMachine()+"} , porta = "+PORT+"... aguardando clientes!!");
		try {
			servidor = new ServerSocket(PORT);
			host = servidor.getInetAddress().getHostAddress()+":"+servidor.getLocalPort();
		} catch (IOException e) {
			_listener.ErrorOnStartServer(e);
		}
		if(servidor != null){
//			startPingService();
			startPollingClients();			
		}
		
	}


	private void startPingService() {
		final ServidorSocket server = this;
		threadPinService = new Thread(new Runnable() {
			
			public void run() {
				while(true){
					_listener.Notify("ping started");
					sendMessageForAllClients(new Mensagem(null, server, "ping at "+ DateTimeUtils.getDateNow(), Origem.Servidor));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});	
		threadPinService.run();
	}


	private void startPollingClients() {
		_listener.Notify("polling clients started!");
		while(true){
			try {
				Cliente cliente = new Cliente(servidor.accept(), this);
				cliente.startConn();
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
	
	public void sendMessageForAllClients(Mensagem msg){
		for (Cliente cliente : listClientes) {
			_listener.Notify("sendind {cliente : \""+cliente.getIp()+"\", message : \""+msg.getMensagem()+"\"}");
			cliente.sendResponse(msg);
		}
	}


	public void remove(Cliente cliente) {
		listClientes.remove(cliente);
		_listener.clientDisconected(cliente);
	}


	
}
