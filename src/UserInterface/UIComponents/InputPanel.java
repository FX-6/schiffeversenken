package UserInterface.UIComponents;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JLabel;

public class InputPanel extends UIPanel {
   private String label;
   private JPanel inputLabelRow;
   private JLabel inputLabel;
   private JLabel errorLabel;
   private GridLayout singleGridLayout = new GridLayout(2, 1, 0, 0);
   private Font font = new Font("Titel", Font.PLAIN, fontSizeSmall);

   public InputPanel(String label) {
      super();
      this.label = label;
      setup();
   }

   private void setup() {
      setLayout(singleGridLayout);

      inputLabelRow = new InputLabelRow();
      inputLabel = new InputLabel(label);
      errorLabel = new ErrorLabel();

      inputLabelRow.add(inputLabel);
      inputLabelRow.add(errorLabel);

      add(inputLabelRow);
   }

   public void setError(String text) {
      errorLabel.setText(text);
   }

   private class InputLabelRow extends UIPanel {
      private GridLayout dualGridLayout = new GridLayout(1, 2, 0, 0);

      InputLabelRow() {
         super();
         setLayout(dualGridLayout);
      }
   }

   private class InputLabel extends UILabel {
      InputLabel(String text) {
         super(text);
         setFont(font);
      }
   }

   private class ErrorLabel extends UILabel {
      ErrorLabel() {
         super();
         setFont(font);
         setForeground(errorColor);
      }
   }
}
