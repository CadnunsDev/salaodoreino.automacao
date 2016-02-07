package salaodoreino.automacao.runtime;

public class Mensagem {
	
	
	
	public Mensagem(Cliente cliente, ServidorSocket servidor, String mensagem,
			Origem origem) {
		this.cliente = cliente;
		this.servidor = servidor;
		this.mensagem = mensagem;
		this.origem = origem;
	}

	private Cliente cliente;
	private ServidorSocket servidor;
	private String mensagem;
	private Origem origem;
	public ServidorSocket getServidor() {
		return servidor;
	}
	public String getMensagem() {
		return mensagem;
	}
	public Origem getOrigem() {
		return origem;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
}

