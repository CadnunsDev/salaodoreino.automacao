package salaodoreino.automacao.runtime;

import java.io.IOException;

public interface IServerListener {
	void whenClientSendMessage(Mensagem msg);

	void ErrorOnStartServer(IOException e);

	void ErrorOnAceptClient(IOException e);

	void newConnectionAcepted(Cliente cliente);

	void Notify(String string);

	Mensagem clientSendMessage(Mensagem msg);
}
