import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class WishesServer {
    /*public static void main(String args[]) throws Exception
    {
        try{
            int serverPort = 5000;
            ServerSocket serverSocket = new ServerSocket(serverPort, 0,
                    InetAddress.getByName("192.168.1.103"));
            //serverSocket.setSoTimeout(20000);
            System.out.println("server is ready");
            while (true) {
                // Подключение к порту. По сути, начало работы сервера.
                Socket server = serverSocket.accept();
                // Получение данных от клиента.
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String line = fromClient.readLine();
                //String[] arr;
                //arr = line.split(",");
                //String line = fromClient.readLine();
                System.out.println(line);
                // Ответ клиенту.
                PrintWriter toClient = new PrintWriter(server.getOutputStream(), true);
                toClient.println("Thank you for connecting to " + server.getLocalSocketAddress() + " Goodbye!");
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            try {
                if (e instanceof SocketTimeoutException) {
                    throw new SocketTimeoutException();
                } else {
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException ste) {
                System.out.println("Turn off the server by timeout");
            }
        }
        *//*ServerSocket sersock = new ServerSocket(5000);
        System.out.println("server is ready");  //  message to know the server is running
        Socket sock = sersock.accept();

        InputStream istream = sock.getInputStream();
        DataInputStream dstream = new DataInputStream(istream);
        String message2 = dstream.readLine();
        System.out.println(message2);
        dstream .close(); istream.close(); sock.close(); sersock.close();*//*
    }*/
}
