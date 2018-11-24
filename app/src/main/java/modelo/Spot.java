package modelo;

public class Spot {
    private String spotId;
    private String park;
    private String locationGeo;
    private int status;
    private int rating;

    public Spot(String spotId, String park, String locationGeo, int status, int rating) {
        this.spotId = spotId;
        this.locationGeo = locationGeo;
        this.status = status;
        this.park = park;
        this.rating = rating;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getPark() {
        return park;
    }

    public String getLocationGeo() {
        return locationGeo;
    }

    public int getStatus() {
        return status;
    }

    public int getRating() {
        return rating;
    }
}
