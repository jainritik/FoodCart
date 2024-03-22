package main;

import example.factory.RestaurantFactory;
import example.model.Menu;
import example.presenter.OrderManager;
import example.model.Restaurant;
import example.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of restaurants to add: ");
        int numRestaurants = scanner.nextInt();
        scanner.nextLine();

        List<Restaurant> restaurants = new ArrayList<>();

        // Loop to add restaurants
        for (int i = 1; i <= numRestaurants; i++) {
            System.out.println("Enter details for Restaurant " + i + ":");

            System.out.print("Restaurant Name: ");
            String restaurantName = scanner.nextLine();

            System.out.print("Enter items and quantities (e.g., item1:50,item2:40,item3:30): ");
            String itemInput = scanner.nextLine();

            // Parse items and quantities
            Map<String, Integer> items = new HashMap<>();
            String[] itemPairs = itemInput.split(",");
            for (String itemPair : itemPairs) {
                String[] parts = itemPair.split(":");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    items.put(itemName, quantity);
                }
            }

            System.out.print("Processing Capacity: ");
            int processingCapacity = scanner.nextInt();
            scanner.nextLine();

            Menu menu = new Menu();
            items.forEach((itemName, price) -> menu.addItem(itemName, price));

            Restaurant restaurant = RestaurantFactory.createRestaurant(Integer.toString(i), restaurantName, menu.getItems(), processingCapacity);
            restaurants.add(restaurant);
        }

        OrderManager orderManager = new OrderManager(restaurants);

        // Place order
        System.out.println("Enter the items for the order:");
        System.out.print("Enter items and quantities (e.g., item1:2,item2:1): ");
        String itemOrderInput = scanner.nextLine();

        // Parse items and quantities for order
        Map<String, Integer> orderItems = new HashMap<>();
        String[] orderItemPairs = itemOrderInput.split(",");
        for (String orderItemPair : orderItemPairs) {
            String[] parts = orderItemPair.split(":");
            if (parts.length == 2) {
                String itemName = parts[0].trim();
                int quantity = Integer.parseInt(parts[1].trim());
                orderItems.put(itemName, quantity);
            }
        }

        // Place order
        Order order = orderManager.placeOrder(orderItems);

        // Display order details
        if (order != null) {
            System.out.println("Order placed successfully:");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total Price: " + order.getTotalPrice());
            System.out.println("Status: " + order.getStatus());
        } else {
            System.out.println("Failed to place order. Please try again.");
        }

        scanner.close();
    }
}
