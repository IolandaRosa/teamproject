package modelo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static java.security.spec.MGF1ParameterSpec.SHA1;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private List<Spot> favouriteSpots;
    private FindPreference findPreference;

    public User(){

    }

    public User(String id,String name, String email, FindPreference preference, String password) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.password=md5_Hash(password);
        this.favouriteSpots=new ArrayList<>();
        this.findPreference=preference;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword () {
        return this.password;
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

    private String md5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
