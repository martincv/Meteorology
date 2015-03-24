package core;

import java.util.ArrayList;

import model.GeoPoint;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class LatLngTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		GeoPoint point = new GeoPoint(48.887690, 2.378283);
		GeoPoint point = new GeoPoint(41.756382, 5.696156);
		ArrayList<GeoPoint> border = new ArrayList<GeoPoint>();
		border.add(new GeoPoint(48.379457, -4.521131));
		border.add(new GeoPoint(51.063354, 2.466174));
		border.add(new GeoPoint( 48.959877, 8.135119));
		border.add(new GeoPoint(46.552664, 6.047717));
		border.add(new GeoPoint(43.891399, 7.453967));
		border.add(new GeoPoint(42.481656, 3.059436));
		border.add(new GeoPoint(43.430422, -1.774549));
		System.out.println(isLocationInside(point, border));
	}
	

    /**
     * Returns true if a given location (LatLon) is located inside a given polygon
     *
     * @param point the location
     * @param positions the list of positions describing the polygon. Last one should be the same as the first one.
     * @return true if the location is inside the polygon.
     */
    public static boolean isLocationInside(GeoPoint point, ArrayList<? extends GeoPoint> positions)
    {

        boolean result = false;
        GeoPoint p1 = positions.get(0);
        for (int i = 1; i < positions.size(); i++)
        {
            GeoPoint p2 = positions.get(i);

// Developped for clarity
//            double lat = point.getLatitude().degrees;
//            double lon = point.getLongitude().degrees;
//            double lat1 = p1.getLatitude().degrees;
//            double lon1 = p1.getLongitude().degrees;
//            double lat2 = p2.getLatitude().degrees;
//            double lon2 = p2.getLongitude().degrees;
//            if ( ((lat2 <= lat && lat < lat1) || (lat1 <= lat && lat < lat2))
//                    && (lon < (lon1 - lon2) * (lat - lat2) / (lat1 - lat2) + lon2) )
//                result = !result;

            if ( ((p2.getLatitude() <= point.getLatitude()
                    && point.getLatitude() < p1.getLatitude()) ||
                    (p1.getLatitude() <= point.getLatitude()
                            && point.getLatitude() < p2.getLatitude()))
                    && (point.getLongitude() < (p1.getLongitude() - p2.getLongitude())
                    * (point.getLatitude() - p2.getLatitude())
                    / (p1.getLatitude() - p2.getLatitude()) + p2.getLongitude()) )
                result = !result;

            p1 = p2;
        }
        return result;
    }

}
