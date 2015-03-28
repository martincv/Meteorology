package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.GeoPoint;
import model.MeteoPoint;
import model.SearchQuery;

public class SearchQueryDAO {
	
	public static List<MeteoPoint> findRelatedPoints(Long timeFrom, Long timeTo) throws SQLException {
		List<MeteoPoint> relatedPoints = new ArrayList<MeteoPoint>();
		String sql = "SELECT * FROM MeteoPoints where time >= ? AND time <= ?";
		Connection dbConnection = null;
		PreparedStatement statement = null;
 
		try {
			
			dbConnection = JdbcConnectionHelper.getDBConnection();
			
			statement = dbConnection.prepareStatement(sql);
			statement.setLong(1, timeFrom);
			statement.setLong(2, timeTo);
			 
			// execute select SQL stetement
			ResultSet rs = statement.executeQuery();
 
			while (rs.next()) {
 
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");
				Long time = rs.getLong("time");
				Double precipIntensity = rs.getDouble("precipIntensity");
				Double precipProbability = rs.getDouble("precipProbability");
				String precipType = rs.getString("precipType");
				Double precipAccumulation = rs.getDouble("precipAccumulation");
				Double temperature = rs.getDouble("temperature");
				Double windSpeed = rs.getDouble("windSpeed");
				Double humidity = rs.getDouble("humidity");
				Double pressure = rs.getDouble("pressure");
				Double nearestStormDistance = rs.getDouble("nearestStormDistance");
				MeteoPoint meteoPoint = new MeteoPoint(latitude, longitude, time, precipIntensity,
						precipProbability, precipType, precipAccumulation,
						temperature, windSpeed, humidity, pressure,nearestStormDistance);
				relatedPoints.add(meteoPoint);
			}
			return relatedPoints;
		} finally {
 
			if (statement != null) {
				statement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
 
		}
	}
 
	public void insertNewQuery(SearchQuery search) throws SQLException{
 
		String sql = "INSERT INTO Searches (timeFrom, timeTo) VALUES (?, ?)";
		Connection dbConnection = null;
		PreparedStatement statement = null;
 
		try {
			
			dbConnection = JdbcConnectionHelper.getDBConnection();
			dbConnection.setAutoCommit(false);
			
			statement = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, search.getTimeFrom());
			statement.setLong(2, search.getTimeTo());
			statement.executeUpdate();
			
			ResultSet rs = statement.getGeneratedKeys();
		    rs.next();
		    int searchId = rs.getInt(1);
		    insertMeteoPoints(search.getQueriedPoints(), searchId, dbConnection);
		    insertAreaPoints(search.getAreaPoints(), searchId, dbConnection);
		    
			dbConnection.commit();
		} finally {
 
			if (statement != null) {
				statement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
 
		}
	}
	
	private void insertMeteoPoints(List<MeteoPoint> meteoPoints, int searchId, Connection dbConnection) throws SQLException {
		String sql = "INSERT INTO Meteopoints"
				+ " (searchId, latitude,longitude,time,precipIntensity, precipProbability, precipType,"
				+ " precipAccumulation, temperature, windSpeed, humidity, pressure, nearestStormDistance)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = dbConnection.prepareStatement(sql);
		
		for (MeteoPoint meteoPoint : meteoPoints) {
			statement.setInt(1, searchId);
			statement.setDouble(2, meteoPoint.getLatitude());
			statement.setDouble(3, meteoPoint.getLongitude());
			statement.setLong(4, meteoPoint.getTime());
			statement.setDouble(5, meteoPoint.getPrecipIntensity());
			statement.setDouble(6, meteoPoint.getPrecipProbability());
			statement.setString(7, meteoPoint.getPrecipType());
			statement.setDouble(8, meteoPoint.getPrecipAccumulation());
			statement.setDouble(9, meteoPoint.getTemperature());
			statement.setDouble(10, meteoPoint.getWindSpeed());
			statement.setDouble(11, meteoPoint.getHumidity());
			statement.setDouble(12, meteoPoint.getPressure());
			statement.setDouble(13, meteoPoint.getNearestStormDistance());
			statement.executeUpdate();
		}
		
	}
	
	private void insertAreaPoints(List<GeoPoint> geoPoints, int searchId, Connection dbConnection)  throws SQLException {
		String sql = "INSERT INTO Areapoints"
				+ " (searchId, latitude,longitude)"
				+ " VALUES (?, ?, ?)";
		PreparedStatement statement = dbConnection.prepareStatement(sql);
		
		for (GeoPoint geoPoint : geoPoints) {
			statement.setInt(1, searchId);
			statement.setDouble(2, geoPoint.getLatitude());
			statement.setDouble(3, geoPoint.getLongitude());
			statement.executeUpdate();
		}
	}
 
}
