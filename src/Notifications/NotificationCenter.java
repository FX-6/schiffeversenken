package Notifications;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

/**
 * Diese Klasse bietet eine Kommunikationsebene für Objekte, die sich gegenseitig nicht referenzieren.
 * Um diese zu nutzen, muss die empfangende Klasse das Interface Notification implementieren und mit NotificationCenter.addObserver()
 * als Empfänger registriert werden.
 * Um eine Notification zu senden, wird NotificationCenter.sendNotification(String type, Object object) aufgerufen, wobei "type"
 * der identifizierende Name und "object" ein gegebenenfalls existierender Übergabeparameter (kann auch null sein) ist.
 */

public final class NotificationCenter {

	/**
	 * Speichert alle Abbonenten einer Nachricht / eines Events in einer Liste unter dem Namen der Nachricht / des Events.
	 */
	private static HashMap<String, ArrayList<Notification>> listeners = new HashMap<String, ArrayList<Notification>>();
	
	/**
	 * Sendet eine Nachricht an alle Abbonenten der Nachricht / des Events "type"
	 * 
	 * @param type Name der Nachricht / des Events.
	 * @param object Eventueller Anhang (kann auch "null" sein).
	 */
	public static void sendNotification(String type, Object object) {
		List<Notification> list = listeners.get(type);
		if (list == null) return;
		for (Notification listener : list) {
			try {
				listener.processNotification(type, object);				// Ruft die Methode zur Abarbeitung der Nachricht / des Events bei allen Abbonenten auf
			} catch (ConcurrentModificationException e) {
				// Es kann vorkommen, dass der Inhalt von "list" durch  den Aufruf von removeAllObservers(Object object) in einem anderen Thread verändert wird, während obige Schleife gerade durch die Liste iteriert.
				// Dies ist für diese Anwendung nicht weiter schlimm. Das Objekt wird zwar nicht aus der Liste enftfernt, der Sinn des Entfernen ist allerdings auch nur, dass es nicht mehr referenziert wird, sodass es von
				// der GarbageCollection freigegeben wird.
			}
		}
	}
	
	/**
	 * Abboniert eine Nachricht / ein Event.
	 * 
	 * @param type Name der Nachricht / des Events, welche(s) abboniert werden soll.
	 * @param notification Objekt, welches die Nachricht / das Event abboniert.
	 */
	public static void addObserver(String type, Notification notification) {
		// Wenn es der erste Abbonent für diese Nachricht / dieses Event ist -> es existiert noch keine Liste der Abbonenten
		if (listeners.get(type) == null) {
			ArrayList<Notification> list = new ArrayList<Notification>();
			list.add(notification);
			listeners.put(type, list);
		}
		// Andernfalls
		else {
			ArrayList<Notification> list = listeners.get(type);
			list.add(notification);
			listeners.put(type, list);
		}
	}
	
	
	/**
	 * Deabboniert alle Nachrichten / Events, die übergebenes Objekt abboniert hat.
	 * 
	 * @param object Objekt, dessen Abonements entfernt werden sollen.
	 */
	public static void removeAllObservers(Object object) {
		for (ArrayList<Notification> list : listeners.values()) {
			list.remove(object);
		}
	}
	
}
