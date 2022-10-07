package rxjava.examples.retrofit;


import lombok.Data;

import java.util.List;

@Data
public class Cities {
    List<City> results;

    public Integer distanceTo(double warsawLat, double warsawLon) {
        return 0;
    }
}
