import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws IOException {

        try {
            try {
                clientSocket = new Socket("localhost", 666); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while (clientSocket.isConnected()) {

                    System.out.println("Введите команду здесь:");

                    String command = reader.readLine();
                    out.write(command + "\n");
                    out.flush();
                    String serverWord = in.readLine();
                    System.out.println(serverWord);
                }

            } finally {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }


    }
}

