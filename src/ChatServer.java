import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private static final int PORT = 8080;

    public static void chatServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер открыт на порту " + PORT);

            while (true) {
                // Ожидаем подключения клиента
                Socket clientSocket = serverSocket.accept();
                System.out.println("Пользователь подключен: " + clientSocket.getInetAddress());

                // Создаем поток для чтения сообщений от клиента
                Thread readThread = new Thread(new ReadThread(clientSocket));
                readThread.start();

                // Создаем поток для отправки сообщений клиенту
                Thread writeThread = new Thread(new WriteThread(clientSocket));
                writeThread.start();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ReadThread implements Runnable {

    private Socket socket;

    public ReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Читаем сообщение от клиента
                byte[] buffer = new byte[1024];
                int bytesCount = socket.getInputStream().read(buffer);
                String message = new String(buffer, 0, bytesCount);

                // Выводим сообщение в консоль
                System.out.println("Пользователь: " + message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class WriteThread implements Runnable {

    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Читаем сообщение из консоли
                byte[] buffer = new byte[1024];
                int bytesCount = System.in.read(buffer);
                String message = new String(buffer, 0, bytesCount);

                // Отправляем сообщение клиенту
                socket.getOutputStream().write(message.getBytes());
                socket.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
