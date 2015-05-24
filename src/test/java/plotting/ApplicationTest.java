package plotting;

import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import plotting.LineChartWithHover.HoveredThresholdNode;
import model.ClustersAndHulls;
import model.MeteoPoint;
import core.MeteoService;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
 
public class ApplicationTest extends Application {
 
    @Override public void start(Stage stage) throws SQLException, Exception {
    	double[] areaPoints = getAreaPoints();
		String timeFrom = "2014-01-01T00:00:00+02";
		String timeTo = "2015-12-31T23:59:59+02";
		
		MeteoService service = new MeteoService(areaPoints, timeFrom, timeTo);
		ClustersAndHulls clustersAndHulls = service.findMostExtremePoints();
		
        stage.setTitle("Scatter Chart Sample");
        final NumberAxis xAxis = new NumberAxis(13, 17, 0.1);
        final NumberAxis yAxis = new NumberAxis(45.3, 46.7, 0.1);        
        final ScatterChart<Number,Number> sc = new
            ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Longitude");                
        yAxis.setLabel("Latitude");
        sc.setTitle("Clustering points by temperature.");
        
        List<List<MeteoPoint>> clusters = clustersAndHulls.getClusters();
        int numberOfClusters = clusters.size();
        Color[] colors =  new Color[numberOfClusters];
        Random rd = new Random();
        for (int i=0; i < numberOfClusters; i++) {
        	colors[i] = new Color(rd.nextDouble(),rd.nextDouble(),rd.nextDouble(), 1.0);
        }
        for (int i=0; i < numberOfClusters; i++) {
        	List<MeteoPoint> cluster = clusters.get(i);
        	XYChart.Series series1 = new XYChart.Series();
			for (MeteoPoint point : cluster){
				XYChart.Data dataPoint = new XYChart.Data(point.getLongitude(), point.getLatitude());
				dataPoint.setNode(new HoveredThresholdNode(point.getTemperature(), i, colors));		
				series1.getData().add(dataPoint);
			}
	        sc.getData().add(series1);
		}
        Scene scene  = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
    
    /** a node which displays a value on hover, but is otherwise empty */
    class HoveredThresholdNode extends StackPane {
      HoveredThresholdNode(double temperature,int cluster, Color[] colors) {
        setPrefSize(3,3);
        final Label label = createDataThresholdLabel(temperature, cluster, colors);
        getChildren().setAll(label);
        setCursor(Cursor.NONE);
        toFront();

      }
   
	   private Label createDataThresholdLabel(double temperature,int cluster, Color[] colors) {
	        final Label label = new Label(temperature + "");
//	        label.getStyleClass().addAll("default-color1", "chart-line-symbol", "chart-series-line");
	        label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");

//	        label.setTextFill(Color.DARKGRAY);
//	        label.setTextFill(Color.FORESTGREEN);
	        label.setTextFill(colors[cluster]);
	 
	        label.setMinSize(35, 20);
	        return label;
	    }
    }
    public static void main(String[] args) {
        launch(args);
    }
    
	private static double[] getAreaPoints() {
//		double[] areaPoints = {48.379457, -4.521131, 51.063354, 2.466174, 48.959877, 8.135119, 46.552664, 6.047717
//								,43.891399, 7.453967,42.481656, 3.059436, 43.430422, -1.774549};
		
		double[] areaPoints = {46.565148, 13.501787,46.693404, 16.369219,45.520454, 16.446123,45.497356, 13.567705};

		return areaPoints;
	}
}
