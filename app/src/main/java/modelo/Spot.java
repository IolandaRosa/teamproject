package modelo;

public class Spot {
    private String spotId;
    private String park;
    private String locationGeo;
    private int status;

    public Spot(String spotId, String park, String locationGeo, int status) {
        this.spotId = spotId;
        this.locationGeo = locationGeo;
        this.status = status;
        this.park = park;
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
}
