package example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {
    private String id;
    private String name;
    private Map<String, Double> menu;
    private Map<String, Integer> itemQuantities;
    private int processingCapacity;
    private List<Order> orders;

    private static List<Restaurant> allRestaurants = new ArrayList<>();

    public Restaurant(String id, String name, Map<String, Double> menu, int processingCapacity) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.processingCapacity = processingCapacity;
        this.orders = new ArrayList<>();
        this.itemQuantities = new HashMap<>();

        for (String item : menu.keySet()) {
            itemQuantities.put(item, 10000);
        }

        allRestaurants.add(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, Double> menu) {
        this.menu = menu;
    }

    public int getProcessingCapacity() {
        return processingCapacity;
    }

    public void setProcessingCapacity(int processingCapacity) {
        this.processingCapacity = processingCapacity;
    }

    public void updateMenuItemPrice(String itemID, double newPrice) {
        menu.put(itemID, newPrice);
    }

    public void markOrderAsDispatched(String orderID) {
        for (Order order : orders) {
            if (order.getId().equals(orderID)) {
                order.dispatchOrder();
                System.out.println("Order " + orderID + " marked as dispatched.");
                return;
            }
        }
        System.out.println("Order " + orderID + " not found in the restaurant's orders.");
    }

    public int getAvailableQuantity(String itemID) {
        return itemQuantities.getOrDefault(itemID, 1);
    }

    public void setAvailableQuantity(String itemID, int quantity) {
        itemQuantities.put(itemID, quantity);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public static List<Restaurant> getAllRestaurants() {
        return allRestaurants;
    }

    public double getMenuItemPrice(String itemID) {
        return menu.getOrDefault(itemID, 0.0);
    }

    public void updateItemQuantity(String itemID, int quantity) {
        itemQuantities.put(itemID, quantity);
    }
}
