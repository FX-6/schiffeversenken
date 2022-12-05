package Schiffeversenken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

// TODO Auto-generated catch blocks

public class SettingsHandler {
   private static String appName = "Schiffeversenken";
   public static String appDirectory = (
      (System.getProperty("os.name")).toLowerCase().contains("win") ? (System.getenv("AppData") + "\\" + appName) : (
      (System.getProperty("os.name")).toLowerCase().contains("mac") ? (System.getProperty("user.home") + "/Library/Application Support/" + appName) :
      (System.getProperty("user.home") + "/." + appName))
   );
   public static String settingsFilePath = appDirectory + File.separator + "settings.json";

   public static void initSettings() {
      File appDir = new File(appDirectory);
      appDir.mkdir();

      try {
         File settingsFile = new File(settingsFilePath);

         if (settingsFile.createNewFile()) {
            FileWriter fileWriter = new FileWriter(settingsFile);

            fileWriter.write("{\n   \"color.background\": \"#FFFFFF\",\n   \"color.font\": \"#000000\",\n   \"color.button.background\": \"#FFFFFF\",\n   \"color.button.font\": \"#000000\",\n   \"color.error\": \"#FF0000\",\n   \"color.border\": \"#000000\",\n   \"border.width\": \"2\",\n   \"border.radius\": \"15\",\n   \"theme.path\": \"\"\n}");

            fileWriter.close();
         }
      } catch (IOException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static String getSettingString(String name) {
      String returnVal = "";

      try {
         File settingsFile = new File(settingsFilePath);
         Scanner fileReader = new Scanner(settingsFile);

         while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            if (line.contains(name)) {
               int posOfColon = line.indexOf(":", line.indexOf(name));
               int firstQuote = line.indexOf("\"", posOfColon) + 1;
               int secondQuote = line.indexOf("\"", firstQuote);

               returnVal = line.substring(firstQuote, secondQuote);
               break;
            }
         }

         fileReader.close();
      } catch (FileNotFoundException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }

      return returnVal;
   }

   public static int getSettingInt(String name) {
      return Integer.parseInt(SettingsHandler.getSettingString(name));
   }

   public static void setSettingString(String name, String value) {
      try {
         File settingsFile = new File(settingsFilePath);
         File newSettingsFile = new File(settingsFilePath.replace("settings", "newSettings"));
         Scanner fileReader = new Scanner(settingsFile);
         FileWriter fileWriter = new FileWriter(newSettingsFile);

         while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            if (line.contains(name)) {
               int posOfColon = line.indexOf(":", line.indexOf(name));
               int firstQuote = line.indexOf("\"", posOfColon) + 1;
               int secondQuote = line.indexOf("\"", firstQuote);

               line = line.substring(0, firstQuote) + value + line.substring(secondQuote);
            }

            fileWriter.append(line);
         }

         fileReader.close();
         fileWriter.close();

         Files.move(Paths.get(settingsFilePath.replace("settings", "newSettings")), Paths.get(settingsFilePath), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
         // Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static void setSettingInt(String name, int value) {
      SettingsHandler.setSettingString(name, Integer.toString(value));
   }
}
