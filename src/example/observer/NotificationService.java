package example.observer;

public class NotificationService {
    public void notifyOrderDispatched(String orderID) {
        System.out.println("Order " + orderID + " has been dispatched. Notification sent.");
    }
}
