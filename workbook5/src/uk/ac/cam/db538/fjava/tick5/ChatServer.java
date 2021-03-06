package uk.ac.cam.db538.fjava.tick5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import uk.ac.cam.cl.fjava.messages.Message;

public class ChatServer {
	public static void main(String args[]) {
		// get parameter
		int port = 0;
		try {
			if (args.length != 2)
				throw new IllegalArgumentException();
			port = Integer.parseInt(args[0]);
		} catch (Throwable ex) {
			System.err.println("Usage: java ChatServer <port> <database name>");
			return;
		}

		Database database = null;
		try {
			database = new Database(args[1]);
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.err.println("Couldn't connect to the database");
			return;
		}

		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Cannot use port number " + port);
			return;
		}

		MultiQueue<Message> handlers = new MultiQueue<Message>();

		while (true) {
			try {
				Socket client = socket.accept();
				new ClientHandler(client, handlers, database);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
