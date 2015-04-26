package plotting;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import model.MeteoPoint;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

public class ClusterPlot extends JFrame{
	
	List<List<MeteoPoint>> clusters;

//	public ClusterPlot(List<List<MeteoPoint>> clusters) {
//		Random rd = new Random();
//		this.clusters = clusters;
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setSize(800, 600);
//		DataTable data = new DataTable(Double.class, Double.class);
//		for (List<MeteoPoint> cluster : clusters) {
//			for (MeteoPoint point : cluster){
//				System.out.println(point.getLongitude()+ " " + point.getLatitude());
//				data.add(point.getLongitude(), point.getLatitude());
//			}
//		}
//        XYPlot plot = new XYPlot(data);
//        getContentPane().add(new InteractivePanel(plot));
//        Color color = new Color(rd.nextFloat(), rd.nextFloat(), rd.nextFloat());
//        plot.getPointRenderer(data).setColor(color);
//	}
	
	public ClusterPlot(List<List<MeteoPoint>> clusters) {
		Random rd = new Random();
		this.clusters = clusters;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		for (List<MeteoPoint> cluster : clusters) {
			DataTable data = new DataTable(Double.class, Double.class);
			for (MeteoPoint point : cluster){
				System.out.println(point.getLongitude()+ " " + point.getLatitude());
				data.add(point.getLongitude(), point.getLatitude());
			}
	        XYPlot plot = new XYPlot(data);
	        getContentPane().add(new InteractivePanel(plot));
	        Color color = new Color(rd.nextFloat(), rd.nextFloat(), rd.nextFloat());
	        plot.getPointRenderer(data).setColor(color);
		}

	}
	
}
