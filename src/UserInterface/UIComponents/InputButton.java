package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

import Schiffeversenken.SettingsHandler;

/**
 * Wird als standard Button, der den Userstyles folgt, genutzt.
 */
public class InputButton extends JButton {
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
	 * Die Größe des Buttons.
	 */
	protected Dimension size = new Dimension(itemWidth, itemHeigth);
	/**
	 * Die Schriftart des Buttons.
	 */
	protected Font defaultFont = new Font("Titel", Font.PLAIN, fontSizeLarge);
	/**
	 * Die Schriftart des Buttons, wenn er gehovert wird.
	 */
	protected Font hoverFont = new Font("Titel", Font.BOLD, fontSizeLarge);

	/**
	 * Erstellt einen Button mit Userstyles, text und passender Größe.
	 *
	 * @param text   Text der im Button stehen soll
	 * @param single <code>true</code> wenn es alleine in einer Zeile steht,
	 *               ansonsten <code>false</code>
	 */
	public InputButton(String text, boolean single) {
		super(text);

		if (!single) {
			size.setSize(itemWidth / 2 - padding / 2, size.getHeight());
		}
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setSize(size);
		this.setBackground(buttonBackground);
		this.setForeground(buttonFontColor);
		this.setFont(defaultFont);
		this.setBorder(new RoundedBorder(borderRadius, borderWidth, borderColor));

		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				setFont(hoverFont);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				setFont(defaultFont);
			}
		});
	}
}
