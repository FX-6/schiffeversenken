package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.border.MatteBorder;

import Schiffeversenken.SettingsHandler;

/**
 * Wird als DropDownMenu, das den Userstyles folgt, genutzt.
 */
public class InputComboBox<E> extends JComboBox<E> {
	private static final long serialVersionUID = 1L;

	protected int imageSize = SettingsHandler.getSettingInt("settings.image.size");
	protected int fontSizeSmall = SettingsHandler.getSettingInt("settings.fontsize.small");
	protected int fontSizeLarge = SettingsHandler.getSettingInt("settings.fontsize.large");
	protected int itemWidth = SettingsHandler.getSettingInt("settings.items.width");
	protected int itemHeigth = SettingsHandler.getSettingInt("settings.items.height");
	protected int itemSmallWidth = SettingsHandler.getSettingInt("settings.items.small.width");
	protected int itemSmallHeigth = SettingsHandler.getSettingInt("settings.items.small.height");
	protected int padding = SettingsHandler.getSettingInt("settings.items.padding");
	protected int borderWidth = SettingsHandler.getSettingInt("settings.border.width");
	protected int borderRadius = SettingsHandler.getSettingInt("settings.border.radius");

	protected Color backgroundColor = Color.decode(SettingsHandler.getSettingString("color.background"));
	protected Color borderColor = Color.decode(SettingsHandler.getSettingString("color.border"));
	protected Color fontColor = Color.decode(SettingsHandler.getSettingString("color.foreground"));
	protected Color buttonBackground = Color.decode(SettingsHandler.getSettingString("color.button.background"));
	protected Color buttonFontColor = Color.decode(SettingsHandler.getSettingString("color.button.foreground"));
	protected Color errorColor = Color.decode(SettingsHandler.getSettingString("color.error"));

	/**
	 * Erstellt ein DropDownMenu mit Items.
	 * Sollte nur in einem {@link InputPanel} genutzt werden.
	 *
	 * @param items Ein Array der Objekte die eingefügt werden sollen
	 */
	public InputComboBox(E[] items) {
		super(items);

		this.setFont(new Font("Titel", Font.PLAIN, fontSizeLarge));
		this.setBackground(buttonBackground);
		this.setForeground(buttonFontColor);
		this.setBorder(new MatteBorder(0, 0, borderWidth, 0, borderColor));
	}
}
