import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerW {}/*
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Date date = new Date();
            CopyOnWriteArrayList<Ship> ships = Commands.makeLinkedList();


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Сервер Остановлен");
                try (FileWriter writer = new FileWriter("ships.xml", false)) {
                    for (Ship ship : ships) {
                        writer.write(ship.getName() + ", ");
                        writer.write(ship.getSize() + ", ");
                        writer.write(ship.getPlace()+", ");
                        writer.write(ship.getCreator());
                        writer.write("\n");
                    }
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Ошибка ввода-вывода");
                }
            }));

            int port = 63485;

            //Проверяем доступность порта
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                //System.out.println(serverSocket.getLocalPort());
                System.out.println("Ждем подключения клиента...");
            } catch (IOException e) {
                System.out.println("Порт: " + port + " - ошибка подключения");
                System.exit(-1);
            }

            //Создание клиента
            Socket clientSocket = null;

            while (!serverSocket.isClosed()) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("Порт: " + port + " - ошибка подключения");
                    System.exit(-1);
                }

                PoThread myThread = new PoThread(serverSocket, clientSocket, ships, date);
                Thread thread = new Thread(myThread);
                thread.start();
                }

        } catch (Exception e) {
            System.out.println("ошибка");
        }
    }
}
*/