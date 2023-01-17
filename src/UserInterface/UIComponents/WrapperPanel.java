package UserInterface.UIComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

/**
 * Wird als Wrapper, der den Userstyles folgt, genutzt.
 * Sollte als Top-Level Component in der UI genutzt werden.
 */
public class WrapperPanel extends UIPanel {
	private static final long serialVersionUID = 1L;

	protected Dimension size = new Dimension(itemWidth + 2 * borderWidth + 2 * padding, 2 * borderWidth + padding);

	/**
	 * Erstellt ein Wrapperpanel mit den Userstyles.
	 */
	public WrapperPanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setSize(size);
		this.setBackground(backgroundColor);
		this.setBorder(BorderFactory.createCompoundBorder(
				new RoundedBorder(borderRadius, borderWidth, borderColor),
				new EmptyBorder(padding, padding, 0, padding)));
	}

	/**
	 * Fügt einen Komponenten zum Wrapperpanel hinzu.
	 *
	 * @param comp Die Komponente die hinzugefügt werden muss
	 * @param c    Die Constraints der Komponente
	 */
	public void add(Component comp, GridBagConstraints c) {
		super.add(comp, c);

		if (c.insets.left == 0) {
			size.setSize(this.getWidth(), size.getHeight() + comp.getHeight() + padding);
			this.setMinimumSize(size);
			this.setPreferredSize(size);
			this.setMaximumSize(size);
			this.setSize(size);
		}
	}
}
