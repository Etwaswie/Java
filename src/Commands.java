import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import java.util.concurrent.CopyOnWriteArrayList;

public class Commands {
    public static CopyOnWriteArrayList<Ship> makeLinkedList() {


        CopyOnWriteArrayList<Ship> ships = new CopyOnWriteArrayList<>();
        String inLine;
        int index = 0;
        String name = null;
        int size = 0;
        String place = null;
        String creator = null;
        try (
                BufferedReader reader = new BufferedReader(new FileReader("ships.xml"))) {
            while ((inLine = reader.readLine()) != null) {
                Scanner scanner = new Scanner(inLine);
                scanner.useDelimiter(", ");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0) {
                        name = data;
                    } else if (index == 1) {
                        size = Integer.parseInt(data);
                    } else if (index == 2) {
                        place = data;
                    }else if (index==3){
                        creator = data;
                    } else {
                        System.out.println("некоррекные данные");
                    }
                    index++;
                }
                index = 0;
                //Date dateOfCReating = new Date();
                ships.add(new Ship(name, size, place, creator));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Невозможно получить доступ к файлу");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода");
        }
        return ships;
    }

    public static void info(CopyOnWriteArrayList<Ship> ships,  Socket clientSocket) {
        String allInfo = "Тип коллекции: " + ships.getClass() + "\n" + "Кол-во элементов: " + ships.size() +
                "\n" ;
        Commands.sendMessageToClient(allInfo, clientSocket);
    }

    public static void sendMessageToClient(String message, Socket clientSocket) {
        OutputStream outStream = null;
        try {
            outStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Невозможно отправить ответ клиенту");
            System.exit(-1);
        }
        PrintWriter writer = null;
        writer = new PrintWriter(outStream, true);
        //чтобы отправлять String клиенту
        DataOutputStream outStringStream = new DataOutputStream(outStream);
        try {
            outStringStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void show(CopyOnWriteArrayList<Ship> ships, Socket clientSocket) {
        StringBuilder message = new StringBuilder();
        ships.sort(Ship::compareTo);
        ships.forEach(ship -> message.append(ship.toString()));
        Commands.sendMessageToClient(message.toString(), clientSocket);
    }

    public static void add(CopyOnWriteArrayList<Ship> ships, String name, int size, String place,  String creator) {
        Ship s = new Ship(name, size, place, creator);
        ships.add(s);

    }

    public static void help(Socket clientSocket){
        Commands.sendMessageToClient("Примеры команд:"+"\n"+"show , info , add{}, add_if_max{}, add_in_min{}, remove{}, remove_lower{}, stop"+"\n"+ "Пример:" +"\n"+"add{\"name\":\"корабль\",\"size\":\"5000\",\"place\":\"гавань\"}" ,clientSocket);

    }

    public static void add_if_max(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username) {
        Ship ship = new Ship(name, size, place,username);
        if (ship.getSize() > Collections.max(ships, new ShipComparator()).size) {
            ships.add(ship);

        }
    }

    public static void add_if_min(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username) {
        Ship ship = new Ship(name, size, place,username);
        if (ship.getSize() < Collections.min(ships, new ShipComparator()).size) {
            ships.add(ship);

    }//add_if_max{"name":"1","size":"0","place":"z"}

    /*public static void remove(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, Socket clientSocket, String username) {
        CopyOnWriteArrayList<Ship> toRemove = new CopyOnWriteArrayList<>();
        for (Ship s : ships) {
            if ((s.getSize() == (size)) && (s.getName().equals(name)) && (s.getPlace().equals(place))&&(s.creator.equals(username))) {
                toRemove.add(s);
            }
            else if (!s.creator.equals(username)){
                Commands.sendMessageToClient("Нет доступа к элементу",clientSocket);
            }
        }
        if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Нет такого элемента.", clientSocket);
        } else Commands.sendMessageToClient("Элемент удален.", clientSocket);
        ships.removeAll(toRemove);
    }

    public static void remove_lower(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, Date date, Socket clientSocket, String username) {
        CopyOnWriteArrayList<Ship> toRemove = new CopyOnWriteArrayList<>();
        Ship sh = new Ship(name, size, place, username);
        for (Ship s : ships) {
            if (s.getSize() > 0) {
                if (s.getSize() < sh.getSize()) {
                    toRemove.add(s);
                }
            }
        }

        if (sh.getSize() == 0) {

            Commands.sendMessageToClient("Неверный формат", clientSocket);
        } else if (!sh.creator.equals(username)){
            Commands.sendMessageToClient("Нет доступа к элементу",clientSocket);

        } else if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Элементы не удалены.", clientSocket);
        } else Commands.sendMessageToClient("Элементы удалены.", clientSocket);
        ships.removeAll(toRemove);


    }


    public static void remove(
                              CopyOnWriteArrayList<Ship> ships, Socket clientSocket,int num, String userName) {




            int numberOfElement;
            //получаем число, которое содержится в фигурных скобках

            numberOfElement = num;
            if (numberOfElement > ships.size() - 1) {
                Commands.sendMessageToClient("Такого элемента не существует", clientSocket);
            } else if (!ships.get(numberOfElement).getCreator().equals(userName)){
                Commands.sendMessageToClient("Нет доступа к данному элементу", clientSocket);
            } else {
                ships.remove(numberOfElement);
                Commands.sendMessageToClient("Успешно удален " + numberOfElement + " элемент из коллекции", clientSocket);
            }
        }

    public static void removeFirst(CopyOnWriteArrayList<Ship> ships, Socket clientSocket, String userName) {
        if (ships.get(0).getCreator().equals(userName)) {
            ships.remove(0);
            sendMessageToClient("Успешно удален первый элемент из коллекции", clientSocket);
        } else {
            sendMessageToClient("Не удалось удалить элемент. Нет доступа к элементу", clientSocket);
        }
    }*/

        }
}



