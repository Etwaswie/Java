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
                    } else if (index == 3) {
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

    public static void info(CopyOnWriteArrayList<Ship> ships, Socket clientSocket) {
        String allInfo = "Тип коллекции: " + ships.getClass() + "\n" + "Кол-во элементов: " + ships.size() +
                "\n";
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

    public static void add(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String creator, Socket clientSocket) {
        Ship s = new Ship(name, size, place, creator);
        ships.add(s);
        Commands.sendMessageToClient("Элемент добавлен", clientSocket);

    }

    public static void help(Socket clientSocket) {
        Commands.sendMessageToClient("Примеры команд:" + "\n" + "show , info , add{}, add_if_max{}, add_in_min{}, remove{}, remove_lower{}, stop" + "\n" + "Пример:" + "\n" + "add{\"name\":\"корабль\",\"size\":\"5000\",\"place\":\"гавань\"}", clientSocket);

    }

    public static void add_if_max(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username, Socket clientSocket) {
        Ship ship = new Ship(name, size, place, username);
        if (ship.getSize() > Collections.max(ships, new ShipComparator()).size) {
            ships.add(ship);
            Commands.sendMessageToClient("Элемент добавлен", clientSocket);

        } else Commands.sendMessageToClient("Элемент не добавлен", clientSocket);
    }

    public static void add_if_min(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username, Socket clientSocket) {
        Ship ship = new Ship(name, size, place, username);
        if (ship.getSize() < Collections.min(ships, new ShipComparator()).size) {
            ships.add(ship);
            Commands.sendMessageToClient("Элемент добавлен", clientSocket);
        } else Commands.sendMessageToClient("Элемент не добавлен", clientSocket);

    }


    //add_if_max{"name":"1","size":"0","place":"z"}

    public static void remove(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username, Socket clientSocket) {
        CopyOnWriteArrayList<Ship> toRemove = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Ship> toRemoveFin = new CopyOnWriteArrayList<>();
        for (Ship s : ships) {
            if ((s.getSize() == (size)) && (s.getName().equals(name)) && (s.getPlace().equals(place))) {
                toRemove.add(s);
            }
        }

        if (!toRemove.isEmpty()) {
            for (Ship t : toRemove) {
                if (t.creator == username) {
                    toRemoveFin.add(t);
                }
            }
        }

        if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Нет такого элемента", clientSocket);
        }
        if ((toRemoveFin.isEmpty()) && (!toRemove.isEmpty())) {
            Commands.sendMessageToClient("Нет доступа к элементам", clientSocket);
        }
        if ((!toRemove.isEmpty()) && (!toRemoveFin.isEmpty())) {
            ships.removeAll(toRemoveFin);
            Commands.sendMessageToClient("Элементы удалены", clientSocket);
        }
    }
// remove{"name":"t","size":"7","place":"7"}


    public static void remove_lower(CopyOnWriteArrayList<Ship> ships, String name, int size, String place, String username, Socket clientSocket) {
        CopyOnWriteArrayList<Ship> toRemove = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Ship> toRemoveFin = new CopyOnWriteArrayList<>();
        for (Ship s : ships) {
            if (s.getSize() < size) {
                toRemove.add(s);
            }
        }//remove_lower{"name":"t","size":"7","place":"7"}

        if (!toRemove.isEmpty()) {
            for (Ship t : toRemove) {
                System.out.println();
                System.out.println(t.creator);
                System.out.println(username);
                if (t.creator.equals(username)) {
                    toRemoveFin.add(t);

                }
            }
        }
        System.out.println(toRemove);
        System.out.println(toRemoveFin);

        if (toRemove.isEmpty()) {
            Commands.sendMessageToClient("Нет элементов меньше данного", clientSocket);
        }
        if ((toRemoveFin.isEmpty()) && (!toRemove.isEmpty())) {
            Commands.sendMessageToClient("Нет доступа к элементам", clientSocket);
        }
        if ((!toRemove.isEmpty()) && (!toRemoveFin.isEmpty())) {
            ships.removeAll(toRemoveFin);
            Commands.sendMessageToClient("Элементы удалены", clientSocket);
        }
    }
}






