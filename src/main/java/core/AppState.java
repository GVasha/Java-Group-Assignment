package core;

import users.User;

public class AppState {
    private static AppState INSTANCE;
    private User user;

    private AppState(){}

    public static AppState getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AppState();
        }
        return INSTANCE;
    }

    public void setUser(User user){
        this.user = user;
    }

    public int getUserId(){
        return this.user.getId();
    }
}
