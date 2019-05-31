import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseCommands {
    /**
     * добавляем пользователей в список пользователей на сервере из таблицы в базе данных
     * отдельный список пользователей на сервере в коллекции HashMap (ключ логин, пароль) нужен для проверки
     * авторизации пользователя
     * @param database база данны, из которой мы будем выгружать список пользователей
     * @return обновленный список пользователей HashMap
     */
    public static HashMap<String, String> importUsers(Connection database){
        HashMap<String, String> Users = new HashMap<>();
        try {
            ResultSet data = database.createStatement().executeQuery("select * from \"Users\"");
            String login;
            String password;
            while (data.next()) {
                login = data.getString("LOGIN");
                password = data.getString("PASSWORD");
                Users.put(login, password);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return Users;
    }

    /**
     * загрузить коллекцию из базы данных
     * @param database
     * @param ships
     */
    public static void ImportDatabase(Connection database, CopyOnWriteArrayList<Ship> ships){
        try {
            ResultSet data = database.createStatement().executeQuery("select * from \"Ships\"");
            String name;
            int size;
            String place;
            OffsetDateTime dateOfCreation;
            String creator;
            while (data.next()) {
                name = data.getString("NAME");
                size = data.getInt("SIZE");
                place = data.getString("PLACE");
                dateOfCreation = OffsetDateTime.parse(data.getString("DATEOFCREATION"));
                creator = data.getString("CREATOR");

                    ships.add(new Ship(name, size, place, creator, dateOfCreation));

            }
            System.out.println("База данных успешно загружена");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * хэширование пароля
     * @param st строка, которую будем хэшировать
     * @return хэшированная строка
     */
    public static String MD5hash(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // ошибка возникает, если передаваемый алгоритм в getInstance(...) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }

    /**
     * метод для добавления шоу в базу данных
     * @param database база, в которую будем загружать
     * @param ships коллекция наших шоу
     */
    public static void uploadShips(Connection database, CopyOnWriteArrayList<Ship> ships, Socket clientSocket){
        try {
            database.createStatement().executeUpdate("delete from \"Ship\"");
        } catch (SQLException e){
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < ships.size(); i++) {
                PreparedStatement pstmt = database.prepareStatement("insert into " +
                        "\"Ships\"(\"NAME\", \"SIZE\", \"PLACE\", \"DATEOFCREATION\", \"CREATOR\")" +
                        " values (?, ?, ?, ?, ?)");
                pstmt.setString(1, ships.get(i).getName());
                pstmt.setString(2, Integer.toString(ships.get(i).getSize()));
                pstmt.setString(3, ships.get(i).getPlace());
                pstmt.setString(4, ships.get(i).getDatу().toString());
                pstmt.setString(5, ships.get(i).getCreator());
                pstmt.executeUpdate();
            }
            Commands.sendMessageToClient("Коллекция успешно загружена в базу данных", clientSocket);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * если не надо отправлять месседж клиенту
     * @param database
     * @param ships
     */
    public static void uploadShips(Connection database, CopyOnWriteArrayList<Ship> ships){
        try {
            database.createStatement().executeUpdate("delete from \"Ships\"");
        } catch (SQLException e){
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < ships.size(); i++) {
                PreparedStatement pstmt = database.prepareStatement("insert into " +
                        "\"Ships\"(\"NAME\", \"SIZE\", \"PLACE\", \"DATEOFCREATION\", \"CREATOR\")" +
                        " values (?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, ships.get(i).getName());

                pstmt.setString(2, Integer.toString(ships.get(i).getSize()));
                pstmt.setString(3, ships.get(i).getPlace());
                pstmt.setString(4, ships.get(i).getDatу().toString());
                pstmt.setString(5, ships.get(i).getCreator());
                pstmt.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}