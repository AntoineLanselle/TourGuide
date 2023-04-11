package tourGuide.DTO;

public class UserPreferencesDTO {

    private int attractionProximity = Integer.MAX_VALUE;
    private Double lowerPricePoint = Double.valueOf(0);
    private Double highPricePoint = Double.MAX_VALUE;
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public UserPreferencesDTO() {
    }

    public UserPreferencesDTO(int attractionProximity, Double lowerPricePoint, Double highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.attractionProximity = attractionProximity;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public Double getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(Double lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public Double getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(Double highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
