package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Font;

/**
 * Ein spezielle gestylter Button der genutzt werden sollte um zum Menü
 * zurückzukommen.
 */
public class MenuButton extends InputButton {
	private static final long serialVersionUID = 1L;

	private Dimension size = new Dimension(75, 25);

	/**
	 * Erstellt den Button, der genutzt werden sollte um zum Menü zurückzukommen.
	 */
	public MenuButton() {
		super("Menü", true);

		defaultFont = new Font("Titel", Font.PLAIN, fontSizeSmall);
		hoverFont = new Font("Titel", Font.BOLD, fontSizeSmall);

		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setSize(size);
		this.setLocation(padding, padding);
		this.setBorder(new RoundedBorder(borderRadius, borderWidth, borderColor));
		this.setFont(defaultFont);
	}
}
