import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;

public class MyThread implements Runnable {

    ServerSocket serverSocket;
    Socket clientSocket;
    Date date;
    LinkedList<Ship> ships;

    public MyThread(ServerSocket serverSocket, Socket clientSocket, LinkedList<Ship> ships, Date date) {
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
                    //
                    //Commands.sendMessageToClient(readClientStream,clientSocket);

                    if (readClientStream.equals("show")) {
                        Commands.show(ships, clientSocket);

                    } else if (readClientStream.equals("info")) {
                        Commands.info(ships, date, dateOfChanging, clientSocket);

                    } else if (readClientStream.equals("stop")) {
                        Commands.sendMessageToClient("Работа с сервером завершена.", clientSocket);
                        clientSocket.close();
                        clientSocket = null;


                    } else if ((readClientStream.contains("{")) && (readClientStream.contains("\"size\"") &&
                            (readClientStream.contains("\"name\"")) && (readClientStream.contains("}") &&
                            (readClientStream.contains("\"place\""))))) {

                        String a = readClientStream;
                        String[] ab = a.split(":\"", 5);
                        String name = ab[1].substring(0, ab[1].indexOf("\""));
                        int size = Integer.parseInt(ab[2].substring(0, ab[2].indexOf("\"")));
                        String place = ab[3].substring(0, ab[1].indexOf("\"") - 1);
                        if (a.startsWith("add{")) {
                            Commands.add(ships, name, size, place, clientSocket);
                        }
                        else if (a.startsWith("add_if_max")){
                            Commands.add_if_max(ships, name, size, place, clientSocket);
                        }
                        else if (a.startsWith("add_if_min")){
                            Commands.add_if_min(ships, name, size, place, clientSocket);
                        }
                        else if (a.startsWith("remove{")){
                            Commands.remove(ships, name, size, place, clientSocket);
                        }
                        else if (a.startsWith("remove_lower")){
                            Commands.remove_lower(ships, name, size, place,date, clientSocket);
                        }


                    } else {
                        Commands.sendMessageToClient("Введите команду получше", clientSocket);
                    }


                }

            }
        } catch (Exception e) {
            System.out.println("До свидания!");
        }
        finally {
            System.exit(0);
        }
    }
}
