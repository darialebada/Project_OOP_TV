package fileio;

public class Credentials {
    private String name;
    private String password;
    private String accountType; // standard - premium
    private String country;
    private int balance;
    private int token;
   // private ArrayList<Movie> movies;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(final int balance) {
        this.balance = balance;
    }

    public int getToken() {
        return token;
    }

    public void setToken(final int token) {
        this.token = token;
    }
}
