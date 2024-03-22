package example.strategy;

import example.model.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestaurantSelector {
    public Restaurant selectRestaurant(Map<String, Integer> items, List<Restaurant> restaurants) {
        Restaurant selectedRestaurant = null;
        double lowestTotalCartValue = Double.MAX_VALUE;
        int maxProcessingCapacity = Integer.MIN_VALUE;

        for (Restaurant restaurant : restaurants) {
            if (canFulfillOrder(restaurant, items)) {
                double totalCartValue = calculateTotalCartValue(restaurant, items);
                if (totalCartValue < lowestTotalCartValue ||
                        (totalCartValue == lowestTotalCartValue && restaurant.getProcessingCapacity() > maxProcessingCapacity)) {
                    selectedRestaurant = restaurant;
                    lowestTotalCartValue = totalCartValue;
                    maxProcessingCapacity = restaurant.getProcessingCapacity();
                }
            }
        }

        return selectedRestaurant;
    }

    private double calculateTotalCartValue(Restaurant restaurant, Map<String, Integer> items) {
        double totalCartValue = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemID = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = restaurant.getMenuItemPrice(itemID);
            totalCartValue += itemPrice * quantity;
        }
        return totalCartValue;
    }


    private boolean canFulfillOrder(Restaurant restaurant, Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemID = entry.getKey();
            int quantity = entry.getValue();
            if (quantity > restaurant.getAvailableQuantity(itemID)) {
                return false;
            }
        }
        return true;
    }
}
