package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Wird als Container für Inpus genutzt.
 */
public class InputPanel extends UIPanel {
	/**
	 * Wird zur serialization genutzt.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Die Größe des Panels.
	 */
	protected Dimension size = new Dimension(itemWidth, itemHeigth);

	/**
	 * Dient als Container um die beiden Labels zu halten.
	 */
	private JPanel inputLabelRow;
	/**
	 * Das Label das den Namen des Inputs zeigt.
	 */
	private JLabel inputLabel;
	/**
	 * Das Label das den Error zeigt.
	 */
	private JLabel errorLabel;
	/**
	 * Das Layout der {@link #inputLabelRow}
	 */
	private GridLayout singleGridLayout = new GridLayout(2, 1, 0, 0);
	/**
	 * Die Schriftart der beiden kleinen Label {@link #inputLabel} und
	 * {@link #errorLabel}
	 */
	private Font font = new Font("Titel", Font.PLAIN, fontSizeSmall);

	/**
	 * Erstellt ein Panel mit den Userstyles, einem Label und passender Größe.
	 *
	 * @param label  Text den das Label zeigen soll
	 * @param single <code>true</code> wenn es alleine in einer Zeile steht,
	 *               ansonsten <code>false</code>
	 */
	public InputPanel(String label, boolean single) {
		super();

		if (!single) {
			size.setSize(itemWidth / 2 - padding / 2, size.getHeight());
		}
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setSize(size);
		this.setLayout(singleGridLayout);

		inputLabelRow = new InputLabelRow();
		inputLabel = new InputLabel(label);
		errorLabel = new ErrorLabel();
		errorLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		inputLabelRow.add(inputLabel);
		inputLabelRow.add(errorLabel);

		this.add(inputLabelRow);
	}

	/**
	 * Ändert die angezeigt Fehlermeldung.
	 *
	 * @param text Neue Fehlermeldung
	 */
	public void setError(String text) {
		errorLabel.setText(text);
	}

	/**
	 * Wird intern genutzt um die Label anzuordnen.
	 */
	private class InputLabelRow extends UIPanel {
		private GridLayout dualGridLayout = new GridLayout(1, 2, 0, 0);

		InputLabelRow() {
			super();
			this.setLayout(dualGridLayout);
			this.setBackground(backgroundColor);
		}
	}

	/**
	 * Wird intern als Label genutzt.
	 */
	private class InputLabel extends UILabel {
		InputLabel(String text) {
			super(text);
			this.setFont(font);
		}
	}

	/**
	 * Wird intern als Errorlabel genutzt.
	 */
	private class ErrorLabel extends UILabel {
		ErrorLabel() {
			super("");
			this.setFont(font);
			this.setForeground(errorColor);
		}
	}
}
