package rxjava.examples.retrofit;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private List<GeoName> geonames;
}
