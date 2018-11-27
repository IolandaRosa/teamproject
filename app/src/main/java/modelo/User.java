package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    //private String password;
    private List<Spot> favouriteSpots;
    private FindPreference findPreference;

    public User(){
        this.favouriteSpots=new ArrayList<>();
    }

    public User(String id,String name, String email, FindPreference preference/*, String password*/) {
        this.name = name;
        this.email = email;
        this.id = id;
        //this.password=UsersManager.INSTANCE.md5_Hash(password);
        this.favouriteSpots=new ArrayList<>();
        this.findPreference=preference;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*public String getPassword () {
        return this.password;
    }*/

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

    public void addFavouriteSpot(Spot spot) {
        favouriteSpots.add(spot);
    }

    public void setFavouriteSpots(List<Spot> favouriteSpots) {
        this.favouriteSpots = favouriteSpots;
    }
}
