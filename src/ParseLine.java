import java.net.Socket;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParseLine {

   /* static void parseShortLine(String readClientStream,CopyOnWriteArrayList<Ship> ships,Socket clientSocket,Date date){
        String a= readClientStream;
         int b = Integer.parseInt(a.substring(a.indexOf("{")+1,a.lastIndexOf("}")));
         String username = a.substring(a.indexOf(",")+1);
        Commands.remove(ships, clientSocket, b,username );
        //Commands.sendMessageToClient(String.valueOf(b ),clientSocket);
        //Commands.sendMessageToClient(username,clientSocket);

    }*/

    static void parseLine(String readClientStream, CopyOnWriteArrayList<Ship> ships, String userMail){
        String creator = userMail;
        String a = readClientStream;
        String[] ab = a.split(":\"", 5);
        String name = ab[1].substring(0, ab[1].indexOf("\""));
        int size = Integer.parseInt(ab[2].substring(0, ab[2].indexOf("\"")));
        String place = ab[3].substring(0, ab[3].indexOf("\""));

        if (a.startsWith("add{")) {
            Commands.add(ships, name, size, place, creator);

        } else if (a.startsWith("add_if_max")) {
            Commands.add_if_max(ships, name, size, place ,creator);
        } else if (a.startsWith("add_if_min")) {
            Commands.add_if_min(ships, name, size, place,creator);
        }/* else if (a.startsWith("remove{")) {
            Commands.remove(ships, name, size, place, clientSocket,creator);
        } else if (a.startsWith("remove_lower")) {
            Commands.remove_lower(ships, name, size, place, date, clientSocket,creator);
        }*/


    }}
