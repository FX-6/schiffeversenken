package UserInterface.UIComponents;

import java.awt.Color;

import javax.swing.JLabel;

import Schiffeversenken.SettingsHandler;

/**
 * Wird genutzt um die Userstyles zu laden.
 * Sollte nicht als Komponente in der UI genutzt werden.
 */
public class UILabel extends JLabel {
	/**
	 * Wird zur serialization genutzt.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Größe eines Bildes, als auch eines Tiles.
	 */
	protected int imageSize = SettingsHandler.getSettingInt("settings.image.size");
	/**
	 * Die kleine Schriftgröße.
	 */
	protected int fontSizeSmall = SettingsHandler.getSettingInt("settings.fontsize.small");
	/**
	 * Die große Schriftgröße.
	 */
	protected int fontSizeLarge = SettingsHandler.getSettingInt("settings.fontsize.large");
	/**
	 * Normale Breite eines UI Elements.
	 */
	protected int itemWidth = SettingsHandler.getSettingInt("settings.items.width");
	/**
	 * Normale Höhe eines UI Elements.
	 */
	protected int itemHeigth = SettingsHandler.getSettingInt("settings.items.height");
	/**
	 * Kleine Breite eines UI Elements.
	 */
	protected int itemSmallWidth = SettingsHandler.getSettingInt("settings.items.small.width");
	/**
	 * Kleine Höhe eines UI Elements.
	 */
	protected int itemSmallHeigth = SettingsHandler.getSettingInt("settings.items.small.height");
	/**
	 * Normales Padding von UI Elementen.
	 */
	protected int padding = SettingsHandler.getSettingInt("settings.items.padding");
	/**
	 * Normale Borderbreite von UI Elementen.
	 */
	protected int borderWidth = SettingsHandler.getSettingInt("settings.border.width");
	/**
	 * Normaler Borderradius von UI Elementen.
	 */
	protected int borderRadius = SettingsHandler.getSettingInt("settings.border.radius");

	/**
	 * Hintergrundfarbe der UI.
	 */
	protected Color backgroundColor = Color.decode(SettingsHandler.getSettingString("color.background"));
	/**
	 * Borderdarbe der UI.
	 */
	protected Color borderColor = Color.decode(SettingsHandler.getSettingString("color.border"));
	/**
	 * Schriftfarbe der UI.
	 */
	protected Color fontColor = Color.decode(SettingsHandler.getSettingString("color.foreground"));
	/**
	 * Button Hintergrundfarbe der UI.
	 */
	protected Color buttonBackground = Color.decode(SettingsHandler.getSettingString("color.button.background"));
	/**
	 * Button Schriftfarbe der UI.
	 */
	protected Color buttonFontColor = Color.decode(SettingsHandler.getSettingString("color.button.foreground"));
	/**
	 * Error Schriftfarbe der UI.
	 */
	protected Color errorColor = Color.decode(SettingsHandler.getSettingString("color.error"));

	/**
	 * Erstellt Label mit den Userstyles und text.
	 *
	 * @param text Text den das Label zeigen soll.
	 */
	public UILabel(String text) {
		super(text);

		this.setForeground(fontColor);
	}
}
