package tourGuide.DTO;

import gpsUtil.location.Location;

public class TouristAttractionDetailsDTO {

    private String name;
    private Location touristAttractionLocation;
    private Location userLocation;
    private Double distance;
    private int rewardPoints;

    public TouristAttractionDetailsDTO(String name, Location touristAttractionLocation, Location userLocation, Double distance, int rewardPoints) {
        this.name = name;
        this.touristAttractionLocation = touristAttractionLocation;
        this.userLocation = userLocation;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getTouristAttractionLocation() {
        return touristAttractionLocation;
    }

    public void setTouristAttractionLocation(Location touristAttractionLocation) {
        this.touristAttractionLocation = touristAttractionLocation;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

}
