import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Commands {
    public static LinkedList<Ship> makeLinkedList() {


        LinkedList<Ship> ships = new LinkedList<>();
        String inLine;
        int index = 0;
        String name = null;
        int size = 0;
        String place = null;
        try (
                BufferedReader reader = new BufferedReader(new FileReader("src/ships.xml"))) {
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
                    } else {
                        System.out.println("некоррекные данные");
                    }
                    index++;
                }
                index = 0;
                Date dateOfCReating = new Date();
                ships.add(new Ship(name, size, place, dateOfCReating));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Невозможно получить доступ к файлу");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода");
        }
        return ships;
    }

    public static void info(LinkedList<Ship> ships, Date date, Date dateOfChanging, Socket clientSocket) {
        String allInfo = "Тип коллекции: " + ships.getClass() + "\n" + "Кол-во элементов: " + ships.size() +
                "\n" + "Дата создания: " + date + "\n" + "Дата последнего взаимодействия: " + dateOfChanging;
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

    public static void show(LinkedList<Ship> ships, Socket clientSocket) {
        StringBuilder message = new StringBuilder();
        ships.sort(Ship::compareTo);
        ships.forEach(ship -> message.append(ship.toString()));
        Commands.sendMessageToClient(message.toString(), clientSocket);
    }

    public static void add(LinkedList<Ship> ships, String name, int size, String place, Socket clientSocket) {
        Date dateOfCreating = new Date();
        Ship s = new Ship(name, size, place, dateOfCreating);
        ships.add(s);

        Commands.sendMessageToClient("Элемент добавлен.", clientSocket);
    }

    public static void add_if_max(LinkedList<Ship> ships, String name, int size, String place, Socket clientSocket) {
        Ship ship = new Ship(name, size, place);
        if (ship.getSize() > Collections.max(ships, new ShipComparator()).size) {
            ships.add(ship);
            Commands.sendMessageToClient("Элемент добавлен.", clientSocket);
        } else Commands.sendMessageToClient("Элемент не добавлен.", clientSocket);
    }

    public static void add_if_min(LinkedList<Ship> ships, String name, int size, String place, Socket clientSocket) {
        Ship ship = new Ship(name, size, place);
        if (ship.getSize() < Collections.min(ships, new ShipComparator()).size) {
            ships.add(ship);
            Commands.sendMessageToClient("Элемент добавлен.", clientSocket);
        } else Commands.sendMessageToClient("Элемент не добавлен.", clientSocket);
    }

    public static void remove(LinkedList<Ship> ships, String name, int size, String place, Socket clientSocket) {
        LinkedList<Ship> toRemove = new LinkedList<>();
        for (Ship s : ships) {
            if ((s.getSize() == (size)) && (s.getName().equals(name)) && (s.getPlace().equals(place))) {
                toRemove.add(s);
            }
        }
        if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Нет такого элемента.", clientSocket);
        } else Commands.sendMessageToClient("Элемент удален.", clientSocket);
        ships.removeAll(toRemove);
    }

    public static void remove_lower(LinkedList<Ship> ships, String name, int size, String place, Date date, Socket clientSocket) {
        LinkedList<Ship> toRemove = new LinkedList<>();
        Ship sh = new Ship(name, size, place, date);
        for (Ship s : ships) {
            if (s.getSize() > 0) {
                if (s.getSize() < sh.getSize()) {
                    toRemove.add(s);
                }
            }
        }

        if (sh.getSize() == 0) {
            Commands.sendMessageToClient("Неверный формат", clientSocket);
        } else if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Элементы не удалены.", clientSocket);
        } else Commands.sendMessageToClient("Элементы удалены.", clientSocket);
        ships.removeAll(toRemove);


    }
}



