package model;import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String username;
    private String pass;
    private List<String> writings;

    public User(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public User(String name, String username, String pass) {
        this.name = name;
        this.username = username;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void loadWritings(String filename) {
        try {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            writings = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username) && pass.equals(user.pass);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + pass.hashCode();
        return result;
    }
}
