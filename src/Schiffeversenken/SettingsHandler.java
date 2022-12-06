package Schiffeversenken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// TODO Change link to zip file?
// TODO Auto-generated catch blocks

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

   public static void initSettings() {
      File appDir = new File(appDirectory);

      if (!appDir.exists()) {
         appDir.mkdir();

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

      currentThemePath += getSettingString("theme.path") + File.separator;
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
}
