package example.model;

import java.util.Map;
import java.util.UUID;

public class Order {
    private String id;
    private Map<String, Integer> items;
    private String status;
    private double totalPrice;
    private Restaurant restaurant;

    public Order(Map<String, Integer> items, Restaurant restaurant) {
        this.id = UUID.randomUUID().toString();
        this.items = items;
        this.restaurant = restaurant;
        this.status = "Pending";
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalPrice = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemID = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = getItemPrice(itemID);
            totalPrice += itemPrice * quantity;
        }
    }

    private double getItemPrice(String itemID) {
        Map<String, Double> menu = restaurant.getMenu();

        if (menu.containsKey(itemID)) {
            return menu.get(itemID);
        } else {
            System.out.println("Item " + itemID + " does not exist in the menu.");
            return 0.0;
        }
    }

    public void dispatchOrder() {
        this.status = "Dispatched";
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
