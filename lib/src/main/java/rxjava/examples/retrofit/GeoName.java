package rxjava.examples.retrofit;

import lombok.Data;

@Data
public class GeoName {
    private String lat;
    private String lon;
    private Integer geonameId;
    private Integer population;
    private String countryCode;
    private String name;
}
