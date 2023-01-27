package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import Schiffeversenken.SettingsHandler;

/**
 * Wird als Textfeld, das den Userstyles folgt, genutzt.
 */
public class InputTextField extends JTextField {
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
	 * Erstellt ein Textfeld mit den Userstyles.
	 */
	public InputTextField() {
		super();

		this.setFont(new Font("Titel", Font.PLAIN, fontSizeLarge));
		this.setBackground(buttonBackground);
		this.setForeground(buttonFontColor);
		this.setBorder(new MatteBorder(0, 0, borderWidth, 0, borderColor));
	}

	/**
	 * Ändert den Wert des Textfeldes
	 *
	 * @param value Der neue Wert als <code>String</code>
	 */
	public void setValue(String value) {
		this.setText(value);
	}

	/**
	 * Ändert den Wert des Textfeldes
	 *
	 * @param value Der neue Wert als <code>int</code>
	 */
	public void setValue(int value) {
		this.setText(Integer.toString(value));
	}

	/**
	 * Liefert den Wert des Textfelds zurück.
	 *
	 * @return Der Wert als <code>String</code>
	 */
	public String getStringValue() {
		return this.getText();
	}

	/**
	 * Liefert den Wert des Textfelds zurück.
	 *
	 * @return Der Wert als <code>int</code>
	 */
	public int getIntValue() {
		return Integer.parseInt(this.getStringValue());
	}
}
