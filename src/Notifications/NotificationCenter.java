package Notifications;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

/*
 * Diese Klasse bietet eine Kommunikationsebene für Objekte, die sich gegenseitig nicht referenzieren.
 * Um diese zu nutzen, muss die empfangende Klasse das Interface Notification implementieren und mit NotificationCenter.addObserver()
 * als Empfänger registriert werden.
 * Um eine Notification zu senden, wird NotificationCenter.sendNotification(String type, Object object) aufgerufen, wobei "type"
 * der identifizierende Name und "object" ein gegebenenfalls existierender Übergabeparameter (kann auch null sein) ist.
 */

public final class NotificationCenter {

	private static HashMap<String, ArrayList<Notification>> listeners = new HashMap<String, ArrayList<Notification>>();
	
	// Sendet eine Nachricht an alle Abbonenten des Events "type"
	public static void sendNotification(String type, Object object) {
		List<Notification> list = listeners.get(type);
		if (list == null) return;
		for (Notification listener : list) {
			try {
				listener.processNotification(type, object);
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Abboniert das Event "type" für das Object "notification"
	public static void addObserver(String type, Notification notification) {
		if (listeners.get(type) == null) {
			ArrayList<Notification> list = new ArrayList<Notification>();
			list.add(notification);
			listeners.put(type, list);
		}
		else {
			ArrayList<Notification> list = listeners.get(type);
			list.add(notification);
			listeners.put(type, list);
		}
	}
	
	public static void removeAllObservers(Object object) {
		for (ArrayList<Notification> list : listeners.values()) {
			list.remove(object);
		}
	}
	
}
