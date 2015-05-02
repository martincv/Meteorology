package plotting;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import model.GeoPoint;

public class SelectionPlotTest extends Application{
	
	public final static Integer DEFAULT_POINTS = 1000; 
	
	public Series getPoints(Double minLat, Double maxLat, Double minLong, Double maxLong, Integer numberOfPossiblePoints){
		double a,b;
		double len1 = Math.abs(minLat - maxLat);
		double len2 = Math.abs(minLong - maxLong);
		
		b = Math.sqrt(numberOfPossiblePoints * len2 / len1);
		a = numberOfPossiblePoints / b;
		
		System.out.println("a="+ a+",b="+b);
		double dx1 = len1 / a;
		double dx2 = len2 / b;
		
		List<GeoPoint> possiblePoints = new ArrayList<GeoPoint>();
		XYChart.Series series1 = new XYChart.Series();
        series1.setName("Equities");
		for (double lat = minLat; lat <= maxLat; lat += dx1){
			for (double lon = minLong; lon <= maxLong; lon += dx2){
				possiblePoints.add(new GeoPoint(lat, lon));
				series1.getData().add(new XYChart.Data(lat, lon));
			}
		}
		System.out.println(possiblePoints.size());
		return series1;
	}
	
	@Override public void start(Stage stage) {
		
		double minLat=100.0, maxLat=1100.0, minLong=200.0, maxLong=300.0;
		
        stage.setTitle("Scatter Chart Sample");
        final NumberAxis xAxis = new NumberAxis(minLat-20, maxLat+20, 20);
        final NumberAxis yAxis = new NumberAxis(minLong-10, maxLong+10, 10);        
        final ScatterChart<Number,Number> sc = new
            ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Longitude");                
        yAxis.setLabel("Latitude");
        sc.setTitle("Point Distribution");
       
        XYChart.Series series1 = getPoints(minLat, maxLat, minLong, maxLong, DEFAULT_POINTS);
 
        sc.getData().addAll(series1);
        Scene scene  = new Scene(sc, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
