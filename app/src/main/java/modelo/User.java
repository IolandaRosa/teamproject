package modelo;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private List<Spot> favouriteSpots;
    private FindPreference findPreference;

    public User(){

    }

    public User(String id,String name, String email, FindPreference preference) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.favouriteSpots=new ArrayList<>();
        this.findPreference=preference;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Spot> getFavouriteSpots() {
        return favouriteSpots;
    }

    public FindPreference getFindPreference() {
        return findPreference;
    }

    public void setFindPreference(FindPreference findPreference) {
        this.findPreference = findPreference;
    }
}
