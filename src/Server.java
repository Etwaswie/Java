import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;

    public static void main(String[] args) throws IOException {
        try {
            try {
                server = new ServerSocket(666);
                System.out.println("Сервер запущен!");
                clientSocket = server.accept();
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    String argue = null, newCommand;
                    ArrayBlockingQueue<Posuda> posuda = new ArrayBlockingQueue<>(5);

                    while (clientSocket.isConnected()) {
                        String command = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        out.write("Привет, это Сервер! Подтверждаю, вы ввели команду: " + command + "\n");
                        out.flush();
                        command = command.replaceAll("\\s+", "");

                        if (command.contains("{") & command.contains("}")) {
                            argue = command.substring(command.indexOf("{") + 1, command.indexOf("}"));
                            newCommand = command.substring(0, command.indexOf("{"));

                        } else {
                            newCommand = command;
                        }

                        switch (newCommand) {
                            case "add":

                                Commands.add(argue, posuda);
                                out.write("Item " + argue + " was successfully added");
                                out.flush();
                                break;

                            case "show":

                                Commands.show(posuda);
                                break;

                            case "info":
                            case "Info":

                                Commands.info(posuda);
                                break;

                            case "clear":
                            case "Clear":

                                Commands.clear(posuda);
                                break;

                            case "remove":
                            case "Remove":
                            case "delete":
                            case "Delete":

                                Commands.delete(argue, posuda);
                                break;

                            case "remove_lower":

                                Commands.remove_lower(argue, posuda);
                                break;

                            case "exit":
                            case "Exit":

                                Commands.exit();
                                break;

                            case "import":

                                Commands.importer(argue, posuda);
                                break;

                            default:
                                System.out.println("You entered the wrong command! Read the list of commands carefully!");
                                break;
                        }
                    }

                } finally {

                    System.out.println("dfjkhgkdf");
                    clientSocket.close();
                    in.close(); //close  streams
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


            //*------------------------------------------------------------------------------------------------------------*//



        /*while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains("<Name")) {
                String name = line.substring(line.indexOf("<") + 3 + 3, line.lastIndexOf(">") - 6);
                posuda.add(new Posuda(name));
            }
        }*/

            /*System.out.println("You can use this commands: " + "\n" + "     add {element} - adds an item to your collection");
            System.out.println("     info - shows type of collection, amount of elements, etc");
            System.out.println("     show - shows all items of collection");
            System.out.println("     remove {element} - deletes this element from your collection");
            System.out.println("     remove_lower {element} - deletes all elements lower than this");
            System.out.println("     clear - clears all elements from your collection");
            System.out.println("     exit - close application");
            System.out.println("     import {path} - import elements from the file");


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try (PrintWriter writer = new PrintWriter("C:\\Users\\Yulia\\IdeaProjects\\Laba\\src\\Posuda")) {
                    writer.write("<Posuda>");
                    writer.write("\n");
                    for (Posuda p : posuda) {
                        writer.write("  <Name>" + p.getName() + "</Name> ");
                        writer.write("\n");
                    }
                    writer.write("</Posuda>");
                    writer.flush();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }));*/

}



