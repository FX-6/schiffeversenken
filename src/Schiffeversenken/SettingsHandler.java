package Schiffeversenken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;

import java.awt.image.BufferedImage;

// TODO Change link to zip file? (Felix)
// TODO Auto-generated catch blocks (Felix)

public class SettingsHandler {
   private static String appName = "Schiffeversenken";
   public static String appDirectory = (
      (System.getProperty("os.name")).toLowerCase().contains("win") ? (System.getenv("AppData") + "\\" + appName) : (
      (System.getProperty("os.name")).toLowerCase().contains("mac") ? (System.getProperty("user.home") + "/Library/Application Support/" + appName) :
      (System.getProperty("user.home") + "/." + appName))
   );
   public static String settingsFilePath = appDirectory + File.separator + "settings.json";
   public static String themesFolderPath = appDirectory + File.separator + "Themes";
   public static String currentThemePath = themesFolderPath + File.separator;
   public static String saveGamesPath = appDirectory + File.separator + "Saves";
   public static SettingsValues settingsValues;

   public static void initSettings() {
      File appDir = new File(appDirectory);
      File saveDir = new File(saveGamesPath);

      if (!appDir.exists()) {
         appDir.mkdir();
         saveDir.mkdir();

         try {
            // vlt lieber n github link, dann kann mans gescheit updaten
            URL url = new URL("https://cdn.discordapp.com/attachments/828604182591307797/1049638280515289108/SchiffeversenkenDefaults.zip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            ZipInputStream zipInStr = new ZipInputStream(in);
            ZipEntry entry = zipInStr.getNextEntry();
            byte[] buffer = new byte[1024];

            while(entry != null) {
               File newFile = new File(appDirectory, entry.getName());

               if (!entry.isDirectory()) {
                  System.out.println("File: " + newFile.getName());

                  File parent = newFile.getParentFile();
                  parent.mkdirs();

                  FileOutputStream fileOutStr = new FileOutputStream(newFile);
                  int len;

                  while ((len = zipInStr.read(buffer)) > 0) {
                     fileOutStr.write(buffer, 0, len);
                  }

                  fileOutStr.close();
               }
               zipInStr.closeEntry();
               entry = zipInStr.getNextEntry();
            }
         } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
         }
      }

      settingsValues = new SettingsValues();
      currentThemePath += getSettingString("theme.path") + File.separator;
   }

   public static String getSettingString(String name) {
      return settingsValues.getValue(name);
   }

   public static int getSettingInt(String name) {
      return Integer.parseInt(SettingsHandler.getSettingString(name));
   }

   public static void setSettingString(String name, String value) {
      settingsValues.setValue(name, value);
   }

   public static void setSettingInt(String name, int value) {
      SettingsHandler.setSettingString(name, Integer.toString(value));
   }

   public static String[] getThemes() {
      return new File(themesFolderPath).list();
   }

   public static BufferedImage getImage(String name) {
      BufferedImage image = null;

      try {
         image = ImageIO.read(new File(currentThemePath + name));
      } catch (IOException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }

      return image;
   }

   public static String[] getSavedGames() {
      return new File(saveGamesPath).list();
   }

   public static boolean validateSizeInput(InputTextField inputField, InputPanel inputPanel) {
      int input = 0;
      try {
         input = inputField.getIntValue();
      } catch (NumberFormatException e) {
         inputPanel.setError("Invalider Input");
         return false;
      }

      if (input < 5) {
         inputPanel.setError("Zu klein");
         return false;
      } else if (input > 30) {
         inputPanel.setError("Zu groß");
         return false;
      }

      inputPanel.setError("");
      return true;
   }

   public static boolean validateGameInput(InputTextField[] inputFields, InputPanel[] inputPanels) {
      int[] inputs = new int[5];
      int invalidInputs = 0;

      try {
         inputs[1] = inputFields[1].getIntValue();
      } catch (NumberFormatException e) {
         inputPanels[1].setError("Invalider Input");
         invalidInputs++;
      }
      try {
         inputs[2] = inputFields[2].getIntValue();
      } catch (NumberFormatException e) {
         inputPanels[2].setError("Invalider Input");
         invalidInputs++;
      }
      try {
         inputs[3] = inputFields[3].getIntValue();
      } catch (NumberFormatException e) {
         inputPanels[3].setError("Invalider Input");
         invalidInputs++;
      }
      try {
         inputs[4] = inputFields[4].getIntValue();
      } catch (NumberFormatException e) {
         inputPanels[4].setError("Invalider Input");
         invalidInputs++;
      }
      try {
         inputs[5] = inputFields[5].getIntValue();
      } catch (NumberFormatException e) {
         inputPanels[5].setError("Invalider Input");
         invalidInputs++;
      }

      if (invalidInputs > 0) { return false; }

      int percOccupied = (inputs[1] * 2 + inputs[2] * 3 + inputs[3] * 4 + inputs[4] * 5) * 100 / (inputs[0] * inputs[0]);

      if (inputs[0] < 5) {
         inputPanels[0].setError("Zu klein");
         return false;
      } else if (inputs[0] > 30) {
         inputPanels[0].setError("Zu groß");
         return false;
      } else if (percOccupied < 10) {
         inputPanels[1].setError("Zu wenige");
         inputPanels[2].setError("Zu wenige");
         inputPanels[3].setError("Zu wenige");
         inputPanels[4].setError("Zu wenige");
         return false;
      } else if (percOccupied > 40) {
         inputPanels[1].setError("Zu viele");
         inputPanels[2].setError("Zu viele");
         inputPanels[3].setError("Zu viele");
         inputPanels[4].setError("Zu viele");
         return false;
      }

      inputPanels[0].setError("");
      inputPanels[1].setError("");
      inputPanels[2].setError("");
      inputPanels[3].setError("");
      inputPanels[4].setError("");

      return true;
   }

   public static boolean validateIP(String ip) {
      return ip.matches("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
   }

   public static boolean validateHEXColor(String color) {
      return color.matches("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
   }
}

class SettingsValues {
   private String color_background = "#FFFFFF";
   private String color_font = "#000000";
   private String color_button_background = "#FFFFFF";
   private String color_button_font = "#000000";
   private String color_error = "#FF0000";
   private String color_border = "#000000";
   private String border_width = "2";
   private String border_radius = "15";
   private String theme_path = "Default";
   private String settingsFileString = "";

   public SettingsValues() {
      try {
         File settingsFile = new File(SettingsHandler.settingsFilePath);
         Scanner fileReader = new Scanner(settingsFile);
         while (fileReader.hasNextLine()) { settingsFileString += fileReader.nextLine(); }
         fileReader.close();

         int posOfColon, firstQuote, secondQuote;
         String name = "color.background";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "color.font";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "color.button.background";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "color.button.font";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "color.error";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "color.border";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "border.width";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "border.radius";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

         name = "theme.path";
         posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
         firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
         secondQuote = settingsFileString.indexOf("\"", firstQuote);
         this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));
      } catch (FileNotFoundException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }
   }

   public String getValue(String name) {
      if (name == "color.background") {
         return this.color_background;
      } else if (name == "color.font") {
         return this.color_font;
      } else if (name == "color.button.background") {
         return this.color_button_background;
      } else if (name == "color.button.font") {
         return this.color_button_font;
      } else if (name == "color.error") {
         return this.color_error;
      } else if (name == "color.border") {
         return this.color_border;
      } else if (name == "border.width") {
         return this.border_width;
      } else if (name == "border.radius") {
         return this.border_radius;
      } else if (name == "theme.path") {
         return this.theme_path;
      }

      return "";
   }

   public void setValue(String name, String value) {
      if (name == "color.background") {
         this.color_background = value;
      } else if (name == "color.font") {
         this.color_font = value;
      } else if (name == "color.button.background") {
         this.color_button_background = value;
      } else if (name == "color.button.font") {
         this.color_button_font = value;
      } else if (name == "color.error") {
         this.color_error = value;
      } else if (name == "color.border") {
         this.color_border = value;
      } else if (name == "border.width") {
         this.border_width = value;
      } else if (name == "border.radius") {
         this.border_radius = value;
      } else if (name == "theme.path") {
         this.theme_path = value;
      }

      int posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
      int firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
      int secondQuote = settingsFileString.indexOf("\"", firstQuote);
      settingsFileString = settingsFileString.substring(0, firstQuote) + value + settingsFileString.substring(secondQuote);

      try {
         File settingsFile = new File(SettingsHandler.settingsFilePath);
         FileWriter fileWriter = new FileWriter(settingsFile);
         fileWriter.append(settingsFileString);
         fileWriter.close();
      } catch (IOException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }

   }
}
