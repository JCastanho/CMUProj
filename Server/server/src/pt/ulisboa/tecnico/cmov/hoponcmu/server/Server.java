package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.command.Command;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetCorrectAnswersResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class Server {

	private static final int PORT = 9090;

	public static void main(String[] args) throws Exception {
		
//		EncryptionUtils eu = new EncryptionUtils();
//
//		eu.generateKeys("client");
//		eu.generateKeys("server");
//		
//		System.out.println("Keys generated!");
		
//		EncryptionUtils eu = new EncryptionUtils("server");
//		
//		String text = "hello world";
//		byte[] array = text.getBytes("UTF-8");
//		byte[] encrypt = eu.encrypt(array);
//		byte[] decrypt = eu.decrypt(encrypt);
//		
//		System.out.println(new String(decrypt, "UTF-8"));
//		
//		byte[] signature = eu.generateSignature(array);
//		System.out.println("Valid Signature: " + eu.verifySignature(array, signature));
		
		CommandHandlerImpl chi = new CommandHandlerImpl();
		ServerSocket socket = new ServerSocket(PORT);
		Socket client = null;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Server now closed.");
				try { socket.close(); }
				catch (Exception e) { }
			}
		});

		System.out.println("Server is accepting connections at " + PORT);

		while (true) {
			try {
				client = socket.accept();

				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				Command cmd = (Command) ois.readObject();
				Response rsp = cmd.handle(chi);

				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				oos.writeObject(rsp);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (client != null) {
					try { client.close(); }
					catch (Exception e) {}
				}
			}
		}
	}
}
