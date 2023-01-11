package UserInterface.MenuPanels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;

import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.TextLabel;
import UserInterface.UIComponents.WrapperPanel;

public class TutorialPanel extends BackgroundPanel {
	private static final long serialVersionUID = 1L;

   Menu parent;

   public TutorialPanel(Menu parent) {
      this.parent = parent;

      // fill with content
      WrapperPanel wrapperPanel = new WrapperPanel();

      // header
      JLabel headerLabel = new HeaderLabel("Anleitung");
      GridBagConstraints headerLabelConstraints = defaultConstraints;
      headerLabelConstraints.gridy = 0;
      wrapperPanel.add(headerLabel, headerLabelConstraints);

      // text, html tags für'n Textwrap
      JLabel textLabel = new TextLabel("<html>Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia, molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit, tenetur error, harum nesciunt ipsum debitis quas aliquid. Reprehenderit, quia. Quo neque error repudiandae fuga? Ipsa laudantium molestias eos  sapiente officiis modi at sunt excepturi expedita sint? Sed quibusdam recusandae alias error harum maxime adipisci amet laborum. Perspiciatis  minima nesciunt dolorem! Officiis iure rerum voluptates a cumque velit  quibusdam sed amet tempora. Sit laborum ab, eius fugit doloribus tenetur  fugiat, temporibus enim commodi iusto libero magni deleniti quod quam  consequuntur! Commodi minima excepturi repudiandae velit hic maxime doloremque. Quaerat provident commodi consectetur veniam similique ad  earum omnis ipsum saepe, voluptas, hic voluptates pariatur est explicabo  fugiat, dolorum eligendi quam cupiditate excepturi mollitia maiores labore  suscipit quas? Nulla, placeat. Voluptatem quaerat non architecto ab laudantium modi minima sunt esse temporibus sint culpa, recusandae aliquam numquam  totam ratione voluptas quod exercitationem fuga. Possimus quis earum veniam  quasi aliquam eligendi, placeat qui corporis!</html>", true);
      Dimension textLabelDimension = new Dimension(textLabel.getWidth(), itemHeigth * 6);
      textLabel.setMinimumSize(textLabelDimension);
      textLabel.setPreferredSize(textLabelDimension);
      textLabel.setMaximumSize(textLabelDimension);
      textLabel.setSize(textLabelDimension);
      GridBagConstraints textLabelConstraints = defaultConstraints;
      textLabelConstraints.gridy = 1;
      wrapperPanel.add(textLabel, textLabelConstraints);

      // wrapperPanel
      wrapperPanel.setLocation(170, 28);
      this.add(wrapperPanel);

      // menu button
      JButton menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });
      this.add(menuButton);

      // recenter wrapperPanel
      this.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent e) {
            wrapperPanel.setLocation(e.getComponent().getWidth() / 2 - wrapperPanel.getWidth() / 2, e.getComponent().getHeight() / 2 - wrapperPanel.getHeight() / 2);
         }
      });
   }
}
