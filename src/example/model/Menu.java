package example.model;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private Map<String, Double> items;

    public Menu() {
        this.items = new HashMap<>();
    }

    public void addItem(String itemID, double price) {
        items.put(itemID, price);
    }

    public void removeItem(String itemID) {
        items.remove(itemID);
    }

    public Map<String, Double> getItems() {
        return items;
    }
}