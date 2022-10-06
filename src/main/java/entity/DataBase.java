package entity;

import java.io.*;

public class DataBase {
    private static User[] users;
    private static int countUsers;

    public static void dataBaseDownload() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            users = new User[1000];
            countUsers = 0;
            while (bufferedReader.ready()) {
                String[] data = bufferedReader.readLine().trim().split(" ");
                users[countUsers] = new User(data[0], data[1], data[2]);
                countUsers++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dataBaseUnload() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
            for (int i = 0; i < countUsers; i++) {
                User currentUser = users[i];
                if (currentUser != null) {
                    bufferedWriter.write(currentUser.getLogin() + " " + currentUser.getPassword() + " "
                            + currentUser.getName() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(User user) {
        users[countUsers] = user;
        countUsers++;
    }

    public static User getUser(String login) {
        for (int i = 0; i < countUsers; i++) {
            if (users[i].getLogin().equalsIgnoreCase(login)) {
                return users[i];
            }
        }
        return null;
    }

    public static User[] getUsers() {
        return users;
    }
}
