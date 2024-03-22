package example.presenter;

import example.model.Order;
import example.model.Restaurant;
import example.strategy.RestaurantSelector;
import example.observer.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private List<Restaurant> restaurants;
    private Map<Restaurant, Integer> ordersInProgress;
    private RestaurantSelector restaurantSelector;
    private NotificationService notificationService;


    public OrderManager(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.ordersInProgress = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            ordersInProgress.put(restaurant, 0);
        }
        this.restaurantSelector = new RestaurantSelector();
        this.notificationService = new NotificationService();
    }

    public Order placeOrder(Map<String, Integer> items) {
        Restaurant selectedRestaurant = selectRestaurant(items, restaurants);

        if (selectedRestaurant != null) {
            if (canFulfillOrder(selectedRestaurant, items)) {
                if (canAcceptOrder(selectedRestaurant)) {
                    Order order = new Order(items, selectedRestaurant);
                    notifyOrderDispatched(order.getId());
                    ordersInProgress.put(selectedRestaurant, ordersInProgress.get(selectedRestaurant) + 1);
                    return order;
                } else {
                    System.out.println("Selected restaurant " + selectedRestaurant.getName() +
                            " has reached its processing capacity. Please try again later.");
                    return null;
                }
            } else {
                System.out.println("Selected restaurant " + selectedRestaurant.getName() +
                        " cannot fulfill all the items in the order.");
                return null;
            }
        } else {
            System.out.println("No restaurants available to accept the order.");
            return null;
        }
    }

    private boolean canFulfillOrder(Restaurant restaurant, Map<String, Integer> items) {
        Map<String, Double> menu = restaurant.getMenu();

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemID = entry.getKey();
            int quantity = entry.getValue();

            if (!menu.containsKey(itemID) || quantity > restaurant.getAvailableQuantity(itemID)) {
                return false;
            }
        }

        return true;
    }

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

    private boolean canAcceptOrder(Restaurant restaurant) {
        return ordersInProgress.get(restaurant) < restaurant.getProcessingCapacity();
    }

    private void notifyOrderDispatched(String orderID) {
        notificationService.notifyOrderDispatched(orderID);
    }

    public void updateMenuItemPrice(String restaurantID, String itemID, double newPrice) {
        Restaurant restaurant = findRestaurantById(restaurantID);
        if (restaurant != null) {
            restaurant.updateMenuItemPrice(itemID, newPrice);
            System.out.println("Menu item price updated successfully.");
        } else {
            System.out.println("Restaurant not found.");
        }
    }

    private Restaurant findRestaurantById(String restaurantID) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(restaurantID)) {
                return restaurant;
            }
        }
        return null;
    }

    // we can call it once order is ready
    public void markOrderAsDispatched(String orderID) {
        Order order = findOrderById(orderID);
        if (order != null) {
            order.dispatchOrder();
            Restaurant restaurant = order.getRestaurant();
            ordersInProgress.put(restaurant, ordersInProgress.get(restaurant) - 1);
            updateRestaurantCapacity(restaurant, order.getItems());
        }
    }

    private Order findOrderById(String orderID) {
        for (Restaurant restaurant : restaurants) {
            for (Order order : restaurant.getOrders()) {
                if (order.getId().equals(orderID)) {
                    return order;
                }
            }
        }
        return null;
    }

    private void updateRestaurantCapacity(Restaurant restaurant, Map<String, Integer> items) {
        int totalQuantityPrepared = items.values().stream().mapToInt(Integer::intValue).sum();
        int updatedCapacity = restaurant.getProcessingCapacity() - totalQuantityPrepared;
        if (updatedCapacity >= 0) {
            restaurant.setProcessingCapacity(updatedCapacity);
            System.out.println("Updated capacity of restaurant " + restaurant.getName() + " after order dispatch: " + updatedCapacity);
        } else {
            System.out.println("Warning: Capacity of restaurant " + restaurant.getName() + " is negative after order dispatch.");
        }
    }
}
