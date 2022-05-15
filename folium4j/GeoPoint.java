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

/**
 * GeoPoint
 * Geographic coordinates (latitude and longitude coordinates)
 * Author : Wang Kang
 * Date : 5/15/2022
 */
public class GeoPoint {

    public double latitude;
    public double longitude;

    public GeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", latitude, longitude);
    }

    /**
     * Calculate the distance between two location
     * @param pt
     * @return
     */
    public double distanceTo(GeoPoint pt) {
        double lat1 = Math.toRadians(this.latitude);
        double lng1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(pt.latitude);
        double lng2 = Math.toRadians(pt.longitude);
        double dist = 6371.01 * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lng1 - lng2));
        return dist;
    }
}
