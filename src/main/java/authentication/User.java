package authentication;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String location;
    private Role role;

    protected User(String email, String firstName, String lastName, String password, String location, Role role) {
        this.email = email;
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.location = location;
        this.role = role;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLocation() {
        return location;
    }

    public Role getRole() {
        return role;
    }


}
