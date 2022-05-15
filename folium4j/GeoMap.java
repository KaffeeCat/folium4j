/**
 * MIT License

Copyright (c) 2022 Wang Kang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
package folium4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.awt.Desktop;

public class GeoMap {

    public enum MapStyle {
        Default,
        Satellite,
        ChineseEnglish,
        Grayscale,
        Darkblue,
    }

    String map_id;
    MapStyle map_style;
    GeoPoint map_location;
    int map_zoom_level;

    String html_header;
    String html_script_canvas;

    public GeoMap(GeoPoint location, int zoomLevel, MapStyle mapType) {

        // Initialize map ID, location and zoom level
        map_id = String.format("map_%d", System.nanoTime());
        map_style = mapType;
        map_location = location;
        map_zoom_level = zoomLevel;

        // Prepare HTML header
        html_header = "";
        html_header += "<!DOCTYPE html>\n<head>\n\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n";
        html_header += "\t<script>\n\t\tL_NO_TOUCH = false;\n\t\tL_DISABLE_3D = false;\n\t</script>\n";
        html_header += "\t<style>html, body {width: 100%;height: 100%;margin: 0;padding: 0;}</style>\n";
        html_header += "\t<style>#map {position:absolute;top:0;bottom:0;right:0;left:0;}</style>\n";

        String[] script_src_list = {
            "https://cdn.jsdelivr.net/npm/leaflet@1.6.0/dist/leaflet.js", 
            "https://code.jquery.com/jquery-1.12.4.min.js",
            "https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js",
            "https://cdnjs.cloudflare.com/ajax/libs/Leaflet.awesome-markers/2.0.2/leaflet.awesome-markers.js"
        };

        for (String script_src : script_src_list) {
            html_header += String.format("\t<script src=\"%s\"></script>\n", script_src);
        }

        String[] href_list = {
            "https://cdn.jsdelivr.net/npm/leaflet@1.6.0/dist/leaflet.css",
            "https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css",
            "https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css",
            "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css",
            "https://cdnjs.cloudflare.com/ajax/libs/Leaflet.awesome-markers/2.0.2/leaflet.awesome-markers.css",
            "https://cdn.jsdelivr.net/gh/python-visualization/folium/folium/templates/leaflet.awesome.rotate.min.css"
        };

        for (String href : href_list) {
            html_header += String.format("\t<link rel=\"stylesheet\" href=\"%s\"/>\n", href);
        }

        html_header += "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />\n";
        html_header += String.format("\t<style>\n\t\t#%s {\n\t\t\tposition: relative;\n\t\t\twidth: 100.0%%;\n\t\t\theight: 100.0%%;\n\t\t\tleft: 0.0%%;\n\t\t\ttop: 0.0%%;\n\t\t}\n\t</style>\n</head>\n", map_id);
        html_header += String.format("<body>\n\t<div class=\"folium-map\" id=\"%s\" ></div>\n</body>\n", map_id);

        html_script_canvas = "";
    }
    
    /**
     * Set the location of map
     * @param location
     */
    public void setLocation(GeoPoint location) {
        this.map_location = location;
    }

    /**
     * Set the zoom level of map
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        this.map_zoom_level = zoomLevel;
    }

    /**
     * Reset the canvas
     */
    public void reset() {
        html_script_canvas = "";
    }

    /**
     * save the map to html file
     * @param filename
     */
    public void save(String filename) {

        String script_header_1 = String.format("var %s=L.map(\"%s\",{center:[%f,%f],crs: L.CRS.EPSG3857,zoom: %d,zoomControl: true,preferCanvas: false});", 
            map_id, map_id, map_location.latitude, map_location.longitude, map_zoom_level);

        String title_layer_url = "";
        String title_layer_attribute = "";

        switch (map_style) {
            case Satellite:
                title_layer_url = "https://webst02.is.autonavi.com/appmaptile?style=6\u0026x={x}\u0026y={y}\u0026z={z}";
                title_layer_attribute = "\u9ad8\u5fb7-\u536b\u661f\u5f71\u50cf\u56fe";
                break;
            case ChineseEnglish:
                title_layer_url = "https://webrd02.is.autonavi.com/appmaptile?lang=zh_en\u0026size=1\u0026scale=1\u0026style=8\u0026x={x}\u0026y={y}\u0026z={z}";
                title_layer_attribute = "\u9ad8\u5fb7-\u4e2d\u82f1\u6587\u5bf9\u7167";
                break;
            case Grayscale:
                title_layer_url = "https://stamen-tiles-{s}.a.ssl.fastly.net/toner/{z}/{x}/{y}.png";
                title_layer_attribute = "Map tiles by \u003ca href=\\\"http://stamen.com\\\"\u003eStamen Design\u003c/a\u003e, under \u003ca href=\\\"http://creativecommons.org/licenses/by/3.0\\\"\u003eCC BY 3.0\u003c/a\u003e. Data by \u0026copy; \u003ca href=\\\"http://openstreetmap.org\\\"\u003eOpenStreetMap\u003c/a\u003e, under \u003ca href=\\\"http://www.openstreetmap.org/copyright\\\"\u003eODbL\u003c/a\u003e.";
                break;
            case Darkblue:
                title_layer_url = "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}";
                title_layer_attribute = "\u84dd\u9ed1\u7248";
                break;
            default:
                title_layer_url = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png";
                title_layer_attribute = "Data by \u0026copy; \u003ca href=\\\"http://openstreetmap.org\\\"\u003eOpenStreetMap\u003c/a\u003e, under \u003ca href=\\\"http://www.openstreetmap.org/copyright\\\"\u003eODbL\u003c/a\u003e.";
                break;
        }

        String script_header_2 = String.format("var tile_layer_%d = L.tileLayer(\"%s\",{\"attribution\": \"%s\", \"detectRetina\": false, \"maxNativeZoom\": 18, \"maxZoom\": 18, \"minZoom\": 0, \"noWrap\": false, \"opacity\": 1, \"subdomains\": \"abc\", \"tms\": false}).addTo(%s);", 
            System.nanoTime(), title_layer_url, title_layer_attribute, map_id);

        String html_script = String.format("\n<script>\n\t%s\n\n\t%s\n%s</script>", script_header_1, script_header_2, html_script_canvas);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(html_header + html_script);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * visualize the map in web browser (save the map by the way)
     * @param filename
     */
    public void visualize(String filename) {

        this.save(filename);
        Desktop desktop = Desktop.getDesktop();
        try {
            File file = new File(filename);
            desktop.browse(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw polyline on map
     * @param polyline
     * @param color
     * @param opacity
     * @param weight
     */
    public void drawPolyline(List<GeoPoint> polyline, String color, double opacity, int weight) {

        if (polyline.size() == 0) {
            return;
        }

        String locations = "";
        
        for (GeoPoint loc : polyline) {
            locations += String.format("[%f,%f],", loc.latitude, loc.longitude);
        }
        StringBuffer buf = new StringBuffer(locations);
        buf.deleteCharAt(buf.length()-1);
        
        String canvas = String.format("\n\tvar poly_line_%d = L.polyline([%s],{\"bubblingMouseEvents\": true, \"color\": \"%s\", \"dashArray\": null, \"dashOffset\": null, \"fill\": false, \"fillColor\": \"%s\", \"fillOpacity\": %f, \"fillRule\": \"evenodd\", \"lineCap\": \"round\", \"lineJoin\": \"round\", \"noClip\": false, \"opacity\": %f, \"smoothFactor\": 1.0, \"stroke\": true, \"weight\": %d}).addTo(%s);\n",
            System.nanoTime(), buf, color, color, opacity, opacity, weight, map_id);

        html_script_canvas += canvas;
    }

    /**
     * Draw hollow circle on map
     * @param location
     * @param radius
     * @param weight
     * @param color
     * @param opacity
     */
    public void drawCircle(GeoPoint location, int radius, int weight, String color, double opacity, String popup) {
        this.drawCircle(location, radius, weight, color, opacity, false, color, opacity, popup);
    }

    /**
     * Draw solid circle on map
     * @param location
     * @param radius
     * @param weight
     * @param color
     * @param opacity
     * @param fill
     * @param fillColor
     * @param fillOpacity
     */
    public void drawCircle(GeoPoint location, int radius, int weight, String color, double opacity, boolean fill, String fillColor, double fillOpacity, String popup) {

        long ts = System.nanoTime();

        String circle_id = String.format("circle_%d", ts);

        String canvas = String.format("\n\tvar %s = L.circle([%f, %f],{\"bubblingMouseEvents\": true, \"color\": \"%s\", \"dashArray\": null, \"dashOffset\": null, \"fill\": %s, \"fillColor\": \"%s\", \"fillOpacity\": %f, \"fillRule\": \"evenodd\", \"lineCap\": \"round\", \"lineJoin\": \"round\", \"opacity\": %f, \"radius\": %d, \"stroke\": true, \"weight\": %d}).addTo(%s);\n",
            circle_id, location.latitude, location.longitude, color, fill, fillColor, fillOpacity, opacity, radius, weight, map_id);

        if (popup != null) {
            
            String popup_id = String.format("popup_%d", ts);
            String html_id = String.format("html_%d", ts);

            canvas += String.format("\tvar %s = L.popup({\"maxWidth\": \"100%%\"});\n", popup_id);
            canvas += String.format("\tvar %s = $(`<div id=\"%s\" style=\"width: 100.0%%; height: 100.0%%;\">%s</div>`)[0];\n", html_id, html_id, popup);
            canvas += String.format("\t%s.setContent(%s);\n", popup_id, html_id);
            canvas += String.format("\t%s.bindPopup(%s);\n", circle_id, popup_id);
        }

        html_script_canvas += canvas;
    }

    /**
     * Draw marker on map
     * @param location
     * @param popup
     */
    public void drawMarker(GeoPoint location, String popup) {
        this.drawMarker(location, popup, null, null, null);
    }

    /**
     * Draw marker on map with icon style
     * https://fontawesome.com/icons?d=gallery
     * @param location
     * @param popup
     * @param iconName
     */
    public void drawMarker(GeoPoint location, String popup, String iconName) {
        this.drawMarker(location, popup, iconName, null, null);
    }

    /**
     * Draw marker on map with colored icon
     * https://fontawesome.com/icons?d=gallery
     * @param location
     * @param popup
     * @param iconName # car, bus, plane, home, bed, bank, heart, circle, square, wifi, phone, paw, tree, tv, key
     * @param iconColor
     * @param markerColor
     */
    public void drawMarker(GeoPoint location, String popup, String iconName, String iconColor, String markerColor) {

        long ts = System.nanoTime();
        String marker_id = String.format("marker_%d", ts);

        String canvas = "\n";

        canvas += String.format("\tvar %s = L.marker([%f, %f],{}).addTo(%s);\n", marker_id, location.latitude, location.longitude, map_id);

        if (iconName != null) {
            String icon_id = String.format("icon_%d", ts);
            iconColor = iconColor == null ? "white" : iconColor;
            markerColor = markerColor == null ? "blue" : markerColor;
            canvas += String.format("\tvar %s = L.AwesomeMarkers.icon({\"extraClasses\": \"fa-rotate-0\", \"icon\": \"%s\", \"iconColor\": \"%s\", \"markerColor\": \"%s\", \"prefix\": \"fa\"});\n", icon_id, iconName, iconColor, markerColor);
            canvas += String.format("\t%s.setIcon(%s);\n", marker_id, icon_id);
        }

        if (popup != null) {
            String popup_id = String.format("popup_%d", ts);
            String html_id = String.format("html_%d", ts);
            canvas += String.format("\tvar %s = L.popup({\"maxWidth\": \"100%%\"});\n", popup_id);
            canvas += String.format("\tvar %s = $(`<div id=\"%s\" style=\"width: 100.0%%; height: 100.0%%;\">%s</div>`)[0];\n", html_id, html_id, popup);
            canvas += String.format("\t%s.setContent(%s);\n", popup_id, html_id);
            canvas += String.format("\t%s.bindPopup(%s);\n", marker_id, popup_id);
        }

        html_script_canvas += canvas;
    }
}
