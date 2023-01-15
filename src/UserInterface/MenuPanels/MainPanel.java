package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;

import Schiffeversenken.GameType;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.WrapperPanel;

public class MainPanel extends BackgroundPanel {
   private static final long serialVersionUID = 1L;

   Menu parent;

   public MainPanel(Menu parent) {
      this.parent = parent;

      // fill with content
      WrapperPanel wrapperPanel = new WrapperPanel();

      // title
      JLabel headerLabel = new HeaderLabel("Schiffeversenken", true);
      GridBagConstraints headerLabelConstraints = defaultConstraints;
      headerLabelConstraints.gridy = 0;
      wrapperPanel.add(headerLabel, headerLabelConstraints);

      // start singleplayer
      JButton singlePlayerButton = new InputButton("Einzelspieler", true);
      GridBagConstraints singlePlayerButtonConstraints = defaultConstraints;
      singlePlayerButtonConstraints.gridy = 1;
      singlePlayerButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showCreateSingleplayerGame(GameType.AI);
         }
      });
      wrapperPanel.add(singlePlayerButton, singlePlayerButtonConstraints);

      // start multiplayer
      JButton multiPlayerNetworkButton = new InputButton("Mehrspieler", true);
      GridBagConstraints multiPlayerNetworkButtonConstraints = defaultConstraints;
      multiPlayerNetworkButtonConstraints.gridy = 2;
      multiPlayerNetworkButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showNetworkGame(GameType.NETWORK_CLIENT);
         }
      });
      wrapperPanel.add(multiPlayerNetworkButton, multiPlayerNetworkButtonConstraints);

      // go to description
      JButton descriptionButton = new InputButton("Spielanleitung", true);
      GridBagConstraints descriptionButtonConstraints = defaultConstraints;
      descriptionButtonConstraints.gridy = 3;
      descriptionButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showTutorial();
         }
      });
      wrapperPanel.add(descriptionButton, descriptionButtonConstraints);

      // go to settings
      JButton settingsButton = new InputButton("Einstellungen", true);
      GridBagConstraints settingsButtonConstraints = defaultConstraints;
      settingsButtonConstraints.gridy = 4;
      settingsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showSettings();
         }
      });
      wrapperPanel.add(settingsButton, settingsButtonConstraints);

      // wrapperPanel
      wrapperPanel.setLocation(170, 68);
      this.add(wrapperPanel);

      // recenter wrapperPanel
      this.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent e) {
            wrapperPanel.setLocation(e.getComponent().getWidth() / 2 - wrapperPanel.getWidth() / 2, e.getComponent().getHeight() / 2 - wrapperPanel.getHeight() / 2);
         }
      });
   }
}
