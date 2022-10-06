package entity;

public class User {
    private String login;
    private String password;
    private String name;
    private double[] values = new double[1000];
    private int countValues = 0;

    public void setValue(double value) {
        if (countValues < values.length) {
            values[countValues] = value;
            countValues++;
        } else {
            countValues = 0;
            setValue(value);
        }
    }

    public void printValues() {
        for (Double value : values) {
            if (value != 0) {
                System.out.println(value);
            }
        }
    }

    public User(String login, String password, String name) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
