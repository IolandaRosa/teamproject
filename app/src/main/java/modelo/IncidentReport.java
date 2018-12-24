package modelo;

public class IncidentReport {
    private String description;
    private String location;
    private Spot spot;

    public IncidentReport() {
    }

    public IncidentReport(String description, String location, Spot spot) {
        this.description = description;
        this.location = location;
        this.spot = spot;
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

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
}
