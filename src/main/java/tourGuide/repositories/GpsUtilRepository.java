package tourGuide.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;

@Repository
public class GpsUtilRepository {

	private List<Attraction> attractionsList;

	public GpsUtilRepository() {
		GpsUtil gpsUtil = new GpsUtil();
		attractionsList = gpsUtil.getAttractions();
	}
	
	/**
	 * @return the attractionsList
	 */
	public List<Attraction> getAttractionsList() {
		return attractionsList;
	}
		
}
