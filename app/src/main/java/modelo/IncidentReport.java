package modelo;

public class IncidentReport {
    private String description;
    private String location;
    private String spotId;
    private int id;

    public IncidentReport() {
    }

    public IncidentReport(int id, String description, String location, String spotId) {
        this.description = description;
        this.id = id;
        this.location = location;
        this.spotId = spotId;
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
}
