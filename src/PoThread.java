import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class PoThread implements Runnable {

    ServerSocket serverSocket;
    Socket clientSocket;
    Date date;
    CopyOnWriteArrayList<Ship> ships;
    String creator;

    public PoThread(ServerSocket serverSocket, Socket clientSocket, CopyOnWriteArrayList<Ship> ships, Date date) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
        this.ships = ships;
        this.date = date;
    }

    @Override
    public void run() {
        InputStream inStream = null;
        try {
            inStream = clientSocket.getInputStream();
        } catch (IOException e) {
            System.out.println("Невозможно получить поток ввода");
            System.exit(-1);
        }
        //Поток вывода клиенту, отправляем ему сообщения
        //Читаем поток и отправляем ответ
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inStream));
        String readClientStream = null;

        try {
            while (!serverSocket.isClosed()) {
                OutputStream outClientStream = null;
                outClientStream = clientSocket.getOutputStream();
                DataOutputStream outDataClientStream = new DataOutputStream(outClientStream);


                while (clientSocket != null) {
                    readClientStream = reader.readLine();
                    readClientStream = readClientStream.replaceAll("\\s+", "");
                    Date dateOfChanging = new Date();

                    if (readClientStream.equals("show")) {
                        Commands.show(ships, clientSocket);

                    } else if (readClientStream.equals("info")) {
                        Commands.info(ships, date, dateOfChanging, clientSocket);

                    } else if (readClientStream.equals("stop")) {
                        Commands.sendMessageToClient("Работа с сервером завершена.", clientSocket);
                        clientSocket.close();
                        clientSocket = null;

                    } else if (readClientStream.equals("help")) {
                        Commands.help(clientSocket);

                    } else if ((readClientStream.contains("remove"))&&(readClientStream.contains("{"))&&(readClientStream.contains("}"))) {
                       try {
                            ParseLine.parseShortLine(readClientStream, ships, clientSocket, date);
                        } catch (NullPointerException e) {
                           System.out.println(e);
                           Commands.sendMessageToClient("Данный элемент недоступен. Введите команду получше", clientSocket);
                        }
                       catch (Exception e) {
                           System.out.println(e);
                           Commands.sendMessageToClient("Ошибка доступа. Введите команду получше", clientSocket);
                       }

                    } else if ((readClientStream.contains("{")) && (readClientStream.contains("\"size\"") &&
                            (readClientStream.contains("\"name\"")) && (readClientStream.contains("}") &&
                            (readClientStream.contains("\"place\"")) && (readClientStream.contains("\"creator\""))))) {

                        try {
                            ParseLine.parseLine(readClientStream, ships, clientSocket, date);
                        } catch (Exception e) {
                            Commands.sendMessageToClient("Ошибка. Введите команду получше", clientSocket);
                        }

                    } else {
                        Commands.sendMessageToClient("Введите команду получше", clientSocket);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("Один из клиентов был отключен");
        }
    }
}
