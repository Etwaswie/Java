import java.io.*;
import java.net.Socket;

public class ClientW {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost",  63485);
                reader = new BufferedReader(new InputStreamReader(System.in));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


                String line;
                while ((line = reader.readLine()) != null) {
                    out.write(line + "\n");
                    out.flush();
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    String serverAnswer = in.readUTF();
                    System.out.println("Сервер ответил: \n"+ serverAnswer);
                    if (serverAnswer.equals("Работа завершена")){
                        System.exit(1);
                    }
                }


            }
            catch (NullPointerException e){
                System.out.println("no");}
            /*finally {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();*/

        } catch (IOException e) {
            System.out.println("Клиент закрыт.");
        }

    }
}