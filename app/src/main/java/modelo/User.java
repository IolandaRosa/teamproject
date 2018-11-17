package modelo;

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
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance(SHA1.getDigestAlgorithm());
            instance.update(password.getBytes());

            byte messageDigest[] = instance.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            this.password= hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

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
