package Notifications;

/*
 * Dieses Interface muss implementiert werden, um eine Nachricht über das NotificationCenter zu empfangen. Beim versenden einer Nachricht
 * wird die Funktion processNotification aufgerufen. Es muss dann ueberprueft werden, ob es sich bei "type" auch tatsaechlich um das tatsaechlich
 * abbonierte Event handelt (nur relevant, wenn mehrere Events abbiert wurden).
 */

public interface Notification {
	public void processNotification(String type, Object object);
}
