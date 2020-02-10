import java.io.*;
import java.net.*;

public class ReadThread extends Thread {

	private BufferedReader reader;
	private Socket socket;
	private ChatClient client;
	private boolean first = true;

	public ReadThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;

		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				String response = reader.readLine();
				if (!first) {
					String result[] = response.split(":");
					System.out.print("\n" + result[0] + ": ");
					System.out.println("Key Value: " + client.getKey());
					System.out.println("Ciphertext: " + result[1].trim());
					System.out.println("Original Message: " + client.getEncrypter().decrypt(result[1].trim()));
				} else {
					System.out.println("\n" + response);
					first = false;
				}

				if (client.getUserName() != null) {
					System.out.print("[" + client.getUserName() + "]: ");
				}
			} catch (Exception ex) {
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}
}
