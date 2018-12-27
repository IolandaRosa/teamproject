package modelo;

public class IncidentReport {
    private String description;
    private String location;
    private String spotId;
    private int id;
    // 0 -> não tem, 1 -> tem
    private int hasPhoto;

    public IncidentReport() {
    }

    public IncidentReport(int id, String description, String location, String spotId, int hasPhoto) {
        this.description = description;
        this.id = id;
        this.location = location;
        this.spotId = spotId;
        this.hasPhoto = hasPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(int hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
}
