package authentication;
public class Authentication {
    public static boolean logIn(String email, String password) {
        // TODO: Decide method to store data
        String auxPassword = "example password";
        if (password.equals(auxPassword)) {
            return true;
        }
        return false;
    }

    // Returns true if the user could be created and false if not
    public static boolean singUp(String email, String firstName, String lastName, String password){
        if(checkUniqueEmail(email)){
            User newUser = new User(email, firstName, lastName, password);
            return true;
        }
        return false;
    }

    // Checks if the email already exists in our database
    private static boolean checkUniqueEmail(String email){
        // TODO: Decide method to store data
        String auxEmail = "example email";
        return email.equals(auxEmail);
    }
}
