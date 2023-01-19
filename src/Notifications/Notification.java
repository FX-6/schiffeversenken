package Notifications;

/**
 * Dieses Interface muss implementiert werden, um eine Rundnachricht über das NotificationCenter zu empfangen. Beim versenden einer Nachricht
 * wird die Funktion processNotification aufgerufen. Es muss dann ueberprueft werden, ob es sich bei "type" auch tatsaechlich um das
 * abbonierte Event handelt (nur relevant, wenn mehrere Events abboniert wurden).
 */

public interface Notification {
	
	/**
	 * Verarbeitet eine Rundnachricht / ein Event, die durch {@link NotificationCenter#sendNotification(String, Object)} gesendet wurde.
	 * 
	 * @param type Name der Rundnachricht / des Events.
	 * @param object Eventueller Anhang (kann auch "null" sein).
	 */
	public void processNotification(String type, Object object);
}
