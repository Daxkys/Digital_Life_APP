package example.digitallife;

public class Account {
    private int ID;
    private String name;
    private String link;
    private String user;
    private String pass;

    public Account(String name, String link, String user, String pass) {
        this.ID = generateID();
        this.name = name;
        this.link = link;
        this.user = user;
        this.pass = pass;
    }

    private int generateID() {
        return ID++;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
