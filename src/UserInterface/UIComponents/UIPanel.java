package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import Schiffeversenken.SettingsHandler;

/**
 * Wird genutzt um die Userstyles und standard Constraints zu laden.
 * Sollte nicht als Komponente in der UI genutzt werden.
 */
public class UIPanel extends JPanel {
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
	 * Die Constraints eines normalen UI Elements.
	 */
	public GridBagConstraints defaultConstraints = new GridBagConstraints();
	/**
	 * Die Constraints des ersten UI Elements wenn 2 nebeneinander sind.
	 */
	public GridBagConstraints doubleFirstConstraints = new GridBagConstraints();
	/**
	 * Die Constraints des zweiten UI Elements wenn 2 nebeneinander sind.
	 */
	public GridBagConstraints doubleSecondConstraints = new GridBagConstraints();

	/**
	 * Erstellt ein Panel mit den Userstyles und den standard Constraints.
	 */
	public UIPanel() {
		super();

		defaultConstraints.gridx = 0;
		defaultConstraints.gridwidth = 2;
		defaultConstraints.gridheight = 1;
		defaultConstraints.insets = new Insets(0, 0, padding, 0);

		doubleFirstConstraints.gridx = 0;
		doubleFirstConstraints.gridwidth = 1;
		doubleFirstConstraints.gridheight = 1;
		doubleFirstConstraints.insets = new Insets(0, 0, padding, 0);

		doubleSecondConstraints.gridx = 1;
		doubleSecondConstraints.gridwidth = 1;
		doubleSecondConstraints.gridheight = 1;
		doubleSecondConstraints.insets = new Insets(0, padding, padding, 0);
	}
}
