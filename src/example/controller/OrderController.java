package example.controller;

import example.presenter.OrderManager;

import java.util.Map;

public class OrderController {
    private OrderManager presenter;

    public OrderController(OrderManager presenter) {
        this.presenter = presenter;
    }

    public void placeOrder(Map<String, Integer> items) {
        //  logic to handle placing order
    }

    public void updateMenuItemPrice(String restaurantID, String itemID, double newPrice) {
        //  logic to handle updating menu item price
    }

    public void markOrderAsDispatched(String orderID) {
        //  logic to handle marking order as dispatched
    }
}
