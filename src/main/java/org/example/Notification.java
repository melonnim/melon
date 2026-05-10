package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.Constants.*;

/**
 * Represents a notification sent to a user.
 * Handles notification storage and retrieval.
 */
public class Notification implements NotificationObserver {

    private String dateSent;
    private String message;
    private boolean active;
    private User user;
    private Administrator admin;
    private NotificationType type;

    /**
     * Creates a notification object.
     *
     * @param message notification message
     * @param active whether the notification is active
     * @param user target user
     * @param admin related administrator
     * @param type notification type
     */
    public Notification(String message, boolean active, User user, Administrator admin, NotificationType type) {
        this.message = message;
        this.active = active;
        this.user = user;
        this.admin = admin;
        this.type = type;
        this.dateSent = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Empty constructor required for JSON deserialization.
     */
    public Notification() {

    }

    /**
     * @return the user receiving the notification
     */
    public User getUser() { return user; }

    /**
     * @return related administrator
     */
    public Administrator getAdmin() { return admin; }

    /**
     * @return notification type
     */
    public NotificationType getType() { return type; }

    /**
     * @return notification sent date
     */
    public String getDateSent() { return dateSent; }

    /**
     * @return notification message
     */
    public String getMessage() { return message; }

    /**
     * @return true if the notification is active
     */
    public boolean isActive() { return active; }

    /**
     * Sets notification active status.
     *
     * @param active new active status
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Retrieves all notifications for a user.
     *
     * @param username target username
     * @return list of user notifications
     */
    public static List<Notification> getNotifications(String username) {
        List<Notification> all = JsonHandler.loadList(NOTIFICATIONS_FILE, Notification.class);
        List<Notification> filtered = new ArrayList<>();

        for (Notification n : all) {
            if (n != null && n.getUser() != null && n.getUser().getUsername().equals(username)) {
                filtered.add(n);
            }
        }

        return filtered;
    }

    /**
     * Deletes a specific notification.
     *
     * @param toDelete notification to remove
     */
    public static void deleteNotification(Notification toDelete) {
        List<Notification> all = JsonHandler.loadList(NOTIFICATIONS_FILE, Notification.class);

        all.removeIf(n -> n != null &&
                toDelete != null &&
                Objects.equals(n.getMessage(), toDelete.getMessage()) &&
                Objects.equals(n.getDateSent(), toDelete.getDateSent()));

        JsonHandler.saveList(all, NOTIFICATIONS_FILE);
    }

    /**
     * Deletes all notifications belonging to a user.
     *
     * @param username target username
     */
    public static void deleteAllNotifications(String username) {
        List<Notification> all = JsonHandler.loadList(NOTIFICATIONS_FILE, Notification.class);

        all.removeIf(n -> n != null &&
                n.getUser() != null &&
                n.getUser().getUsername().equals(username));

        JsonHandler.saveList(all, NOTIFICATIONS_FILE);
    }

    /**
     * Adds a new notification to storage.
     *
     * @param message notification message
     * @param user target user
     * @param admin related administrator
     * @param type notification type
     */
    public static void addNotification(String message, User user, Administrator admin, NotificationType type) {
        List<Notification> notifs = JsonHandler.loadList(NOTIFICATIONS_FILE, Notification.class);

        Notification notification = new Notification(message, true, user, admin, type);

        notifs.add(notification);
        JsonHandler.saveList(notifs, NOTIFICATIONS_FILE);
    }

    /**
     * Creates a new notification when the observer is updated.
     *
     * @param message notification message
     * @param user target user
     * @param admin related administrator
     * @param type notification type
     */
    @Override
    public void update(String message, User user, Administrator admin, NotificationType type) {
        addNotification(message, user, admin, type);
    }
}