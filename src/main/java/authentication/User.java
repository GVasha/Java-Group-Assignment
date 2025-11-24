package authentication;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public User(String email, String firstName, String lastName, String password){
        this.email = email;
        this.firstName = firstName;
        this.password = password;
    }
}
