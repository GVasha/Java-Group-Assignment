package core;

public class AppState {
    private static AppState INSTANCE;
    private int user_id;

    private AppState(){}

    public static AppState getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AppState();
        }
        return INSTANCE;
    }

    public void setUserId(int user_id){
        this.user_id = user_id;
    }

    public int getUserId(){
        return this.user_id;
    }
}
