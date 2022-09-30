package rxjava.examples.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Weather {
    Temperature temperature;
    Wind wind;

    public static Weather build(Temperature t, Wind w){
        return new WeatherBuilder().temperature(t).wind(w).build();
    }

    Cloud cloud;

    public float feelsLikeCelsius(){
        return temperature.getCelsiius() + factor();
    }
    public float feelsLikeFahrenheit(){
        return temperature.getFahrenheit() + factor();
    }

    float factor(){

        float windFactor =  wind.getSpeed() > 10 ? 2.0f :
                (wind.getSpeed() > 5 ? 1.0f : 0f);

        return temperature.getFahrenheit() < 40 ? -windFactor : windFactor;

    }

    public boolean isSunny(){
        return temperature.getFahrenheit() > 60 && cloud.getPercentage() < 20;
    }
}
