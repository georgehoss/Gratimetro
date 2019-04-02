package ve.com.phl.gratimetro.Model;

import io.realm.RealmObject;

public class Gratitude extends RealmObject {
    public static final int GOD = 0;
    public static final int ENVIROMENT = 1;
    public static final int SELF = 2;

    private String date;
    private String body;
    private int type;
    private String user;

    public Gratitude() {
    }

    public Gratitude(String date, String body, int type, String user) {
        this.date = date;
        this.body = body;
        this.type = type;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
