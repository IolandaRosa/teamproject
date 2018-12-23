package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private List<Spot> favouriteSpots;
    private FindPreference findPreference;
    private String spotParked;
    private boolean isLogged;

    public User(){
        this.favouriteSpots=new ArrayList<>();
    }

    public User(String id,String name, String email, FindPreference preference, String spotParked) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.favouriteSpots=new ArrayList<>();
        this.findPreference=preference;
        this.spotParked = spotParked;
        this.isLogged = false;
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

    public void addFavouriteSpot(Spot spot) {
        favouriteSpots.add(spot);
    }

    public void setFavouriteSpots(List<Spot> favouriteSpots) {
        this.favouriteSpots = favouriteSpots;
    }

    public String getSpotParked() {
        return spotParked;
    }

    public void setSpotParked(String spotParked) {
        this.spotParked = spotParked;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
