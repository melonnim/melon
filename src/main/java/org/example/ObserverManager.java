package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages notification observers and sends updates to them.
 */
public class ObserverManager {

    private static List<NotificationObserver> observers = new ArrayList<>();

    /**
     * Adds a new observer.
     *
     * @param o observer to add
     */
    public static void addObserver(NotificationObserver o) {
        observers.add(o);
    }

    /**
     * Notifies all observers with a new update.
     *
     * @param message notification message
     * @param user target user
     * @param admin related administrator
     * @param type notification type
     */
    public static void notifyObservers(String message, User user, Administrator admin, NotificationType type) {
        for (NotificationObserver o : observers) {
            o.update(message, user, admin, type);
        }
    }
}