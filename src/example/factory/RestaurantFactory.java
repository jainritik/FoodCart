package example.factory;
import example.model.Restaurant;

import java.util.Map;

public class RestaurantFactory {
    public static Restaurant createRestaurant(String id, String name, Map<String, Double> menu, int processingCapacity) {
        return new Restaurant(id, name, menu, processingCapacity);
    }
}
