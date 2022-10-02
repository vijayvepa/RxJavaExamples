package rxjava.examples.httpservers;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadHttp {
    public static final byte[] RESPONSE = (
            "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: 2\r\n" +
                    "\r\n" +
                    "OK"
    ).getBytes();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080, 100)) {

            while (!Thread.currentThread().isInterrupted()) {
                final Socket client = serverSocket.accept();
                handleSocket(client);
            }
        }
    }

    private static void handleSocket(Socket client) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                readFullRequest(client);
                client.getOutputStream().write(RESPONSE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(client);
        }
    }

    private static void readFullRequest(Socket client) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String line = reader.readLine();

        while (line != null && !line.isEmpty()) {
            line = reader.readLine();
        }
    }
}
