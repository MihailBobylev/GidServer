import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static volatile Server instane = null;
    /* Порт, на который сервер принимает соеденения */
    private final int SERVER_PORT = 5000;
    /* Сокет, который обрабатывает соединения на сервере */
    private ServerSocket serverSoket = null;
    private Server() {}
    public static Server getServer() {
        if (instane == null) {
            synchronized (Server.class) {
                if (instane == null) {
                    instane = new Server();
                }
            }
        }
        return instane;
    }
    @Override
    public void run() {
        try {
            /* Создаем серверный сокет, которые принимает соединения */
            serverSoket = new ServerSocket(SERVER_PORT);
            LOGGER.log(Level.WARNING, "Start server on port:"+SERVER_PORT);
            System.out.println("Start server on port:"+SERVER_PORT);
            /*  старт приема соединений на сервер */
            while(true) {
                ConnectionWorker worker = null;
                try {
                    /* ждем нового соединения  */
                    worker = new ConnectionWorker(serverSoket.accept());
                    System.out.println("Get client connection");
                    /* создается новый поток, в котором обрабатывается соединение */
                    Thread t = new Thread(worker);
                    t.start();
                } catch (Exception e) {
                    System.out.println("Connection error: "+e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.out.println("Cant start server on port "+SERVER_PORT+":"+e.getMessage());
        }
        finally
        {             /* Закрываем соединение */
            if (serverSoket != null) {
                try {
                    serverSoket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

