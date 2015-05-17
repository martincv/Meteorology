package comparator;

import java.util.Comparator;
import model.MeteoPoint;

public class MeteoPointTemperatureComparator implements Comparator<MeteoPoint> {
    @Override
    public int compare(MeteoPoint met1, MeteoPoint met2) {
    	if (met1.getTemperature() < met2.getTemperature()) {
    		return -1;
    	} else if (met1.getTemperature() == met2.getTemperature()) {
    		return 0;
    	} else {
    		return 1;
    	}
    }
}
