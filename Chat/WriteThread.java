import java.io.*;
import java.net.*;

public class WriteThread extends Thread {

	private PrintWriter writer;
	private Socket socket;
	private ChatClient client;

	public WriteThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;

		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
		} catch (IOException ex) {
			System.out.println("Error in WriteThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void run() {

		Console console = System.console();

		String userName = console.readLine("\nPlease enter username: ");
		client.setUserName(userName);
		client.setKey();
		client.decodeKey(client.getKey());
		client.encrypter();
		writer.println(userName);

		String text;

		do {
			text = console.readLine("[" + userName + "]: ");
			
				try {
					String encrypted = client.getEncrypter().encrypt(text);
					System.out.println("Key Value: " + client.getKey());
					System.out.println("Original Message: " + text);
					System.out.println("Ciphertext: " + encrypted);
					writer.println(encrypted);
				} catch(Exception e) {
					e.printStackTrace();
				}

		} while (!text.equals("exit"));

		try {
			socket.close();
		} catch (IOException ex) {

			System.out.println("Error writing to server: " + ex.getMessage());
		}
	}
}
