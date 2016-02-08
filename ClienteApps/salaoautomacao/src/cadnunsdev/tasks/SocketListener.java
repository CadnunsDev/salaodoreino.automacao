package cadnunsdev.tasks;

public interface SocketListener {

	String getIp();

	int getHostPort();

	void notify(String string);

	boolean keepTaskRunnig();

}
