import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;

public class ServerW {
    public static void main(String[] args){
        try{
        Date date = new Date();
        LinkedList<Ship> ships = Commands.makeLinkedList();
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                System.out.println("Сервер Остановлен");
                try (FileWriter writer = new FileWriter("src/ships.xml", false)) {
                    for (int i = 0; i < ships.size(); i++) {
                        writer.write(ships.get(i).getName()+ ", ");
                        writer.write(ships.get(i).getSize() + ", ");
                        writer.write(ships.get(i).getPlace());
                        writer.write("\n");
                    }
                   // writer.flush();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        int port = 45000;

        //Проверяем доступность порта
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Порт: " + port + " - ошибка подключения");
            System.exit(-1);
        }

        //Создание клиента
        Socket clientSocket = null;

        while(!serverSocket.isClosed()){
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Порт: " + port + " - ошибка подключения");
                System.exit(-1);
            }

            MyThread myThread = new MyThread(serverSocket, clientSocket, ships, date);
            Thread thread = new Thread(myThread);
            thread.start();
        }
    }
    catch(Exception e){
        System.out.println("ошибка");
    }}
}
