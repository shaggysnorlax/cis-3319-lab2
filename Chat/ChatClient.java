import java.net.*;
import java.io.*;
import java.nio.file.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class ChatClient {
	private String hostname;
	private int port;
	private String userName;
	private String keyValue;
	private String file = "/Users/jay/Downloads/Chat/key.txt";
	private byte[] decodedKey;
	private SecretKey originalKey;
	DesEncrypter encrypter;
	String encrypted;

	public ChatClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void execute() {
		try {
			Socket socket = new Socket(hostname, port);

			System.out.println("Connected to the chat server");

			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();

		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}

	}

	void setUserName(String userName) {
		this.userName = userName;
	}

	String getUserName() {
		return this.userName;
	}

	String getKey() {
		return this.keyValue;
	}

	void setKey() {
		try {
			this.keyValue = new String(Files.readAllBytes(Paths.get(file)));
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	void decodeKey(String key) {
		decodedKey = Base64.getDecoder().decode(key);
		originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
	}

	SecretKey getSecretKey() {
		return this.originalKey;
	}

	DesEncrypter getEncrypter() {
		return this.encrypter;
	}

	void encrypter() {
		try {
			encrypter = new DesEncrypter(this.originalKey);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Syntax Error: Please enter a port number");
			System.exit(0);
		}

		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}
}
