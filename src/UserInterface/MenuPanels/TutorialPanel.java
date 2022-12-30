package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.TextLabel;
import UserInterface.UIComponents.WrapperPanel;

// TODO size of TextLabel in TextLabel (Felix)

public class TutorialPanel extends BackgroundPanel {
	private static final long serialVersionUID = 1L;

   Menu parent;

   public TutorialPanel(Menu parent) {
      this.parent = parent;
      setup();
      fillWithContent();
   }

   private void setup() {}

   private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

      JLabel headerLabel = new HeaderLabel("Anleitung");
      wrapperPanel.add(headerLabel);

      // html tags für'n Textwrap
      JLabel textLabel = new TextLabel("<html>Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia, molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit, tenetur error, harum nesciunt ipsum debitis quas aliquid. Reprehenderit, quia. Quo neque error repudiandae fuga? Ipsa laudantium molestias eos  sapiente officiis modi at sunt excepturi expedita sint? Sed quibusdam recusandae alias error harum maxime adipisci amet laborum. Perspiciatis  minima nesciunt dolorem! Officiis iure rerum voluptates a cumque velit  quibusdam sed amet tempora. Sit laborum ab, eius fugit doloribus tenetur  fugiat, temporibus enim commodi iusto libero magni deleniti quod quam  consequuntur! Commodi minima excepturi repudiandae velit hic maxime doloremque. Quaerat provident commodi consectetur veniam similique ad  earum omnis ipsum saepe, voluptas, hic voluptates pariatur est explicabo  fugiat, dolorum eligendi quam cupiditate excepturi mollitia maiores labore  suscipit quas? Nulla, placeat. Voluptatem quaerat non architecto ab laudantium modi minima sunt esse temporibus sint culpa, recusandae aliquam numquam  totam ratione voluptas quod exercitationem fuga. Possimus quis earum veniam  quasi aliquam eligendi, placeat qui corporis!</html>");
      wrapperPanel.add(textLabel);

      JButton menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

      // add all to window
      add(menuButton);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
   }
}
