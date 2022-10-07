package rxjava.examples.retrofit;

import lombok.Data;

@Data
public class City {
    private String city;
    private String country;
    private Double distance;
    private Integer id;
    private Double lat;
    private String localizedCountryName;
    private Double lon;
    private Integer memberCount;
    private Integer ranking;
    private String zip;

    public double distanceTo(double warsawLat, double warsawLon) {
        return calculateDistanceInKilometer(this.lat, warsawLat, this.lon, warsawLon);
    }

    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    /**
     * @param userLat  user lat
     * @param userLng  user lon
     * @param venueLat venue lat
     * @param venueLng venue lon
     * @return dist
     * @see "https://stackoverflow.com/a/12600225"
     */
    public int calculateDistanceInKilometer(
            double userLat,
            double userLng,
            double venueLat,
            double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double angle =
                Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +

                        Math.cos(Math.toRadians(userLat))
                                * Math.cos(Math.toRadians(venueLat))
                                * Math.sin(lngDistance / 2)
                                * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1 - angle));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
}
