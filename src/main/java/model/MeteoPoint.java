package model;

public class MeteoPoint extends GeoTimePoint {
	
	private Double precipIntensity;
	private Double precipProbability;
	private String precipType;
	private Double precipAccumulation;
	private Double temperature;
	private Double windSpeed;
	private Double humidity;
	private Double pressure;
	private Double nearestStormDistance;
	
	public MeteoPoint(Double latitude, Double longitude,
			Long time, Double precipIntensity,
			Double precipProbability, String precipType,
			Double precipAccumulation, Double temperature,
			Double windSpeed, Double humidity,
			Double pressure, Double nearestStormDistance) {
		super(latitude, longitude, time);
		this.precipIntensity = precipIntensity;
		this.precipProbability = precipProbability;
		this.precipType = precipType;
		this.precipAccumulation = precipAccumulation;
		this.temperature = temperature;
		this.windSpeed = windSpeed;
		this.humidity = humidity;
		this.pressure = pressure;
		this.nearestStormDistance = nearestStormDistance;
	}
	public MeteoPoint() {}
	
	public Double getPrecipIntensity() {
		return precipIntensity;
	}
	
	public void setPrecipIntensity(Double precipIntensity) {
		this.precipIntensity = precipIntensity;
	}
	
	public Double getPrecipProbability() {
		return precipProbability;
	}
	
	public void setPrecipProbability(Double precipProbability) {
		this.precipProbability = precipProbability;
	}
	
	public String getPrecipType() {
		return precipType;
	}
	public void setPrecipType(String precipType) {
		this.precipType = precipType;
	}
	public Double getPrecipAccumulation() {
		return precipAccumulation;
	}
	public void setPrecipAccumulation(Double precipAccumulation) {
		this.precipAccumulation = precipAccumulation;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Double getNearestStormDistance() {
		return nearestStormDistance;
	}
	public void setNearestStormDistance(Double nearestStormDistance) {
		this.nearestStormDistance = nearestStormDistance;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MeteoPoint))
            return false;
		MeteoPoint point = (MeteoPoint) obj;
        if (this.getLongitude() == point.getLongitude() && 
        	this.getLatitude() == point.getLatitude() && 
        	this.getTime() == point.getTime()) {
        	return true;
        } else {
        	return false;
        }
	}
	
	@Override
	public int hashCode() {
		String hashCode = this.getLongitude() + "" + this.getLatitude() + "" + this.getTime();
		return hashCode.hashCode();
	}
}
