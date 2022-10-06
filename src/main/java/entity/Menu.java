package entity;

import java.util.Scanner;

public class Menu {
    private final static String REGISTRATION = "РЕГИСТРАЦИЯ";
    private final static String AUTHORIZATION = "АВТОРИЗАЦИЯ";
    private final static String HISTORY = "ИСТОРИЯ";
    private final static String EXIT = "ВЫХОД";
    private Scanner scanner = new Scanner(System.in);

    private User currentUser;

    static {
        DataBase.dataBaseDownload();
    }

    public void start() {
        System.out.println("Введите АВТОРИЗАЦИЯ для входа, РЕГИСТРАЦИЯ для создания пользователя или ВЫХОД, чтобы" +
                " завершить программу:");
        String string = scanner.nextLine().trim();
        if (REGISTRATION.equalsIgnoreCase(string)) {
            registration();
        } else if (AUTHORIZATION.equalsIgnoreCase(string)) {
            authorization();
        } else if (EXIT.equalsIgnoreCase(string)) {
            DataBase.dataBaseUnload();
            System.out.println("До свидания!");
        } else {
            System.out.println("Введите корректные данные!");
            start();
        }
    }

    private void registration() {
        while (true) {
            System.out.println("Создайте ЛОГИН:");
            String login = scanner.nextLine().trim();
            if (isEqualWithComplete(login)) {
                start();
                break;
            }
            if (!"".equals(login) && DataBase.getUser(login) == null) {
                System.out.println("Создайте ПАРОЛЬ:");
                String password = scanner.nextLine().trim();
                if (isEqualWithComplete(password)) {
                    start();
                    break;
                }
                if (!"".equals(password)) {
                    System.out.println("Создайте ИМЯ:");
                    String name = scanner.nextLine().trim();
                    if (isEqualWithComplete(name)) {
                        start();
                        break;
                    }
                    if (!"".equals(name)) {
                        DataBase.setUser(new User(login, password, name));
                        System.out.println("Вы успешно зарегистрировались!");
                        start();
                        break;
                    } else {
                        System.out.println("Имя недопустимого формата!");
                    }
                } else {
                    System.out.println("Пароль недопустимого формата!");
                }
            } else {
                System.out.println("Логин недопустимого формата или уже существует!");
            }
        }
    }

    private void authorization() {
        while (true) {
            System.out.println("Ваш ЛОГИН:");
            String login = scanner.nextLine().trim();
            if (isEqualWithComplete(login)) {
                start();
                break;
            }
            System.out.println("Ваш ПАРОЛЬ:");
            String password = scanner.nextLine().trim();
            if (isEqualWithComplete(password)) {
                start();
                break;
            }
            User user = DataBase.getUser(login);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Добро пожаловать, " + currentUser.getName());
                break;
            } else {
                System.out.println("Неверный логин или пароль!");
            }
        }
    }

    private boolean isEqualWithComplete(String string) {
        return (EXIT.equalsIgnoreCase(string));
    }

    private boolean isEqualWithHistory(String string) {
        return (HISTORY.equalsIgnoreCase(string));
    }
}
