package UserInterface.UIComponents;

import java.awt.Dimension;

/**
 * Wird als Wrapper für das Menü während der Runde genutzt.
 */
public class GameMenuPanel extends WrapperPanel {
	/**
	 * Wird zur serialization genutzt.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Erstellt den Wrapper für das ingame Menü.
	 */
	public GameMenuPanel() {
		super();

		this.itemWidth = itemSmallWidth;
		this.itemHeigth = itemSmallHeigth;
		this.setSize(2 * borderWidth + 2 * padding + this.itemWidth, this.getWidth());
	}

	/**
	 * Liefert die Werte für eine kleine UI-Komponente.
	 *
	 * @return Die {@link Dimension} einer kleinen Komponente
	 */
	public Dimension smallDimension() {
		return new Dimension(this.itemWidth / 2 - padding / 2, this.itemHeigth);
	}

	/**
	 * Liefert die Werte für eine große UI-Komponente.
	 *
	 * @return Die {@link Dimension} einer großen Komponente
	 */
	public Dimension largeDimension() {
		return new Dimension(this.itemWidth, this.itemHeigth);
	}
}
