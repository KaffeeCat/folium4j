import java.util.ArrayList;
import java.util.List;

import folium4j.GeoMap;
import folium4j.GeoPoint;
import folium4j.GeoMap.MapStyle;

public class example {
    
    public static void main(String[] args) {

        int example_id = 3;
        GeoPoint orientalPearlTower = new GeoPoint(31.24,121.50);
        GeoMap map = null;

        switch (example_id) {
            
            case 0 : // Simple map
                map = new GeoMap(orientalPearlTower, 15, MapStyle.Satellite);
                map.visualize("map.html");
            break;

            case 1 : // Marker
                map = new GeoMap(orientalPearlTower, 15, MapStyle.ChineseEnglish);
                map.drawMarker(orientalPearlTower, "The Oriental pearl tower", "heart", "white", "red");
                map.visualize("map.html");
            break;

            case 2 : // Circle
                map = new GeoMap(orientalPearlTower, 12, MapStyle.Darkblue);
                map.drawCircle(orientalPearlTower, 5000, 8, "#9ACD32", 0.9, true, "#9ACD32", 0.28, "The Oriental pearl tower");
                map.visualize("map.html");
            break;

            case 3 : // Polyline
                map = new GeoMap(orientalPearlTower, 12, MapStyle.Darkblue);

                List<GeoPoint> points = new ArrayList<GeoPoint>();
                double latitude_offset = -0.1;
                double longitude_offset = -0.1;
                for (int i = 0; i < 20; i++) {
                    GeoPoint point = new GeoPoint(orientalPearlTower.latitude+latitude_offset, orientalPearlTower.longitude+longitude_offset);
                    points.add(point);
                    latitude_offset += (0.01 + 0.02*(Math.random()-0.5));
                    longitude_offset += (0.01 + 0.02*(Math.random()-0.5));
                    map.drawCircle(point, 280, 2, "#FF1493", 0.9, true, "#FF1493", 0.32, null);
                }
                map.drawPolyline(points, "#9ACD32", 0.6, 6);
                map.drawMarker(points.get(0), null, "car", "white", "purple");
                map.drawMarker(points.get(points.size()-1), null, "car", "white", "purple");
                
                map.visualize("map.html");
            break;
        }
    }
}
