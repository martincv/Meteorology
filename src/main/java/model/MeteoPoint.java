package model;

public class MeteoPoint extends GeoTimePoint {
	
	private double precipIntensity;
	private double precipIntensityMax;
	private double precipProbability;
	private String precipType;
	private double precipAccumulation;
	private double temperature;
	private double temperatureMin;
	private double temperatureMax;
	private double apparentTemperature;
	private double apparentTemperatureMin;
	private double apparentTemperatureMax;
	private double windSpeed;
	private double humidity;
	private double pressure;
	private double nearestStormDistance;
	
	public MeteoPoint(double latitude, double longitude, long time,
			double precipIntensity, double precipIntensityMax,
			double precipProbability, String precipType,
			double precipAccumulation, double temperature,
			double temperatureMin, double temperatureMax,
			double apparentTemperature, double apparentTemperatureMin,
			double apparentTemperatureMax, double windSpeed, double humidity,
			double pressure, double nearestStormDistance) {
		super(latitude, longitude, time);
		this.precipIntensity = precipIntensity;
		this.precipIntensityMax = precipIntensityMax;
		this.precipProbability = precipProbability;
		this.precipType = precipType;
		this.precipAccumulation = precipAccumulation;
		this.temperature = temperature;
		this.temperatureMin = temperatureMin;
		this.temperatureMax = temperatureMax;
		this.apparentTemperature = apparentTemperature;
		this.apparentTemperatureMin = apparentTemperatureMin;
		this.apparentTemperatureMax = apparentTemperatureMax;
		this.windSpeed = windSpeed;
		this.humidity = humidity;
		this.pressure = pressure;
		this.nearestStormDistance = nearestStormDistance;
	}
	public MeteoPoint() {}
	
	public double getPrecipIntensity() {
		return precipIntensity;
	}
	
	public void setPrecipIntensity(double precipIntensity) {
		this.precipIntensity = precipIntensity;
	}
	
	public double getPrecipIntensityMax() {
		return precipIntensityMax;
	}
	
	public void setPrecipIntensityMax(double precipIntensityMax) {
		this.precipIntensityMax = precipIntensityMax;
	}
	
	public double getPrecipProbability() {
		return precipProbability;
	}
	
	public void setPrecipProbability(double precipProbability) {
		this.precipProbability = precipProbability;
	}
	
	public String getPrecipType() {
		return precipType;
	}
	public void setPrecipType(String precipType) {
		this.precipType = precipType;
	}
	public double getPrecipAccumulation() {
		return precipAccumulation;
	}
	public void setPrecipAccumulation(double precipAccumulation) {
		this.precipAccumulation = precipAccumulation;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getTemperatureMin() {
		return temperatureMin;
	}
	public void setTemperatureMin(double temperatureMin) {
		this.temperatureMin = temperatureMin;
	}
	public double getTemperatureMax() {
		return temperatureMax;
	}
	public void setTemperatureMax(double temperatureMax) {
		this.temperatureMax = temperatureMax;
	}
	public double getApparentTemperature() {
		return apparentTemperature;
	}
	public void setApparentTemperature(double apparentTemperature) {
		this.apparentTemperature = apparentTemperature;
	}
	public double getApparentTemperatureMin() {
		return apparentTemperatureMin;
	}
	public void setApparentTemperatureMin(double apparentTemperatureMin) {
		this.apparentTemperatureMin = apparentTemperatureMin;
	}
	public double getApparentTemperatureMax() {
		return apparentTemperatureMax;
	}
	public void setApparentTemperatureMax(double apparentTemperatureMax) {
		this.apparentTemperatureMax = apparentTemperatureMax;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getNearestStormDistance() {
		return nearestStormDistance;
	}
	public void setNearestStormDistance(double nearestStormDistance) {
		this.nearestStormDistance = nearestStormDistance;
	}

}
