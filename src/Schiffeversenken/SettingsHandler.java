package Schiffeversenken;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;

/**
 * Verwaltet die Einstellungen die der Spieler macht und speichert sie
 * zugänglich für den Rest des Codes.
 */
public class SettingsHandler {
	/**
	 * Name des Ordners in dem alles gespeichert wird.
	 */
	private static String appName = "Schiffeversenken";
	/**
	 * Pfad des Ordners in dem alles gespeichert wird. OS abhängig. Ohne tailing
	 * separator.
	 */
	public static String appDirectory = ((System.getProperty("os.name")).toLowerCase().contains("win")
			? (System.getenv("AppData") + "\\" + appName)
			: ((System.getProperty("os.name")).toLowerCase().contains("mac")
					? (System.getProperty("user.home") + "/Library/Application Support/" + appName)
					: (System.getProperty("user.home") + "/." + appName)));
	/**
	 * Pfad zur <code>settings.json</code> file.
	 */
	public static String settingsFilePath = appDirectory + File.separator + "settings.json";
	/**
	 * Pfad zum <code>Themes</code> folder. Ohne tailing separator.
	 */
	public static String themesFolderPath = appDirectory + File.separator + "Themes";
	/**
	 * Pfad zum aktuellen Theme folder. Ohne tailing separator.
	 */
	public static String currentThemePath = themesFolderPath + File.separator;
	/**
	 * Pfad zum <code>Saves</code> folder. Ohne tailing separator.
	 */
	public static String saveGamesPath = appDirectory + File.separator + "Saves";
	/**
	 * Alle werte der Einstellungen.
	 */
	public static SettingsValues settingsValues;

	/**
	 * Läd alle Einstellungen aus den jeweiligen Files, muss zu beginn des Programms
	 * aufgerufen werden.
	 */
	public static void initSettings() {
		File appDir = new File(appDirectory);
		File saveDir = new File(saveGamesPath);

		if (!appDir.exists()) {
			appDir.mkdir();
			saveDir.mkdir();

			try {
				URL url = new URL("https://github.com/FX-6/schiffeversenken/raw/main/Settings.zip");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				InputStream in = connection.getInputStream();
				ZipInputStream zipInStr = new ZipInputStream(in);
				ZipEntry entry = zipInStr.getNextEntry();
				byte[] buffer = new byte[1024];

				while (entry != null) {
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
				e.printStackTrace();
			}
		}

		settingsValues = new SettingsValues();
		currentThemePath += getSettingString("settings.theme.path") + File.separator;
		settingsValues.pullColors();
	}

	/**
	 * Liefert den Wert einer Einstellung als <code>String</code>.
	 *
	 * @param name Name der Einstellung als <code>String</code>
	 * @return Gibt den Wert der Einstellung als <code>String</code> zurück
	 */
	public static String getSettingString(String name) {
		return settingsValues.getValue(name);
	}

	/**
	 * Liefert den Wert einer Einstellung als <code>int</code>.
	 *
	 * @param name Name der Einstellung als <code>String</code>
	 * @return Gibt den Wert der Einstellung als <code>int</code> zurück
	 */
	public static int getSettingInt(String name) {
		return Integer.parseInt(SettingsHandler.getSettingString(name));
	}

	/**
	 * Setzt den Wert einer Einstellung auf einen neuen Wert.
	 *
	 * @param name  Name der Einstellung als <code>String</code>
	 * @param value Neuer Wert der Einstellung als <code>String</code>
	 */
	public static void setSettingString(String name, String value) {
		settingsValues.setValue(name, value);
	}

	/**
	 * Setzt den Wert einer Einstellung auf einen neuen Wert.
	 *
	 * @param name  Name der Einstellung als <code>String</code>
	 * @param value Neuer Wert der Einstellung als <code>int</code>
	 */
	public static void setSettingInt(String name, int value) {
		SettingsHandler.setSettingString(name, Integer.toString(value));
	}

	/**
	 * Liefert alle Themename in einem Array zurück.
	 *
	 * @return Ein <code>String</code> Array mit allen Themenamen.
	 */
	public static String[] getThemes() {
		return new File(themesFolderPath).list();
	}

	/**
	 * Liefert ein Bild als {@link BufferedImage} zurück.
	 *
	 * @param name Name des Bilds
	 * @return {@link BufferedImage} object des Bilds, <code>null</code> bei einem
	 *         Fehler
	 */
	public static BufferedImage getImage(String name) {
		return settingsValues.getImage(name);
	}

	/**
	 * Liefert ein um 90° im Uhrzeigersinn rotiertes Bild als {@link BufferedImage}
	 * zurück.
	 *
	 * @param name Name des Bilds
	 * @return {@link BufferedImage} object des Bilds, <code>null</code> bei einem
	 *         Fehler
	 */
	public static BufferedImage getRotatedImage(String name) {
		return rotateImage(settingsValues.getImage(name));
	}

	/**
	 * Liefert alle Dateinamen der speicherstände in einem Array zurück.
	 *
	 * @return Ein <code>String</code> Array mit allen Themenamen.
	 */
	public static String[] getSavedGames() {
		String[] folderContent = new File(saveGamesPath).list();
		int numOfValidNames = folderContent.length;

		for (int i = 0; i < folderContent.length; i++) {
			if (!SettingsHandler.validateSavefileName(folderContent[i])) {
				numOfValidNames--;
				folderContent[i] = null;
			}
		}

		String[] validFiles = new String[numOfValidNames];
		int pos = 0;

		for (String fileName : folderContent) {
			if (fileName != null) {
				validFiles[pos++] = fileName;
			}
		}

		return validFiles;
	}

	/**
	 * Prüft ob die übergebenen Inputs erlaubt sind.
	 *
	 * @param inputField Der Inputfield indem der Wert eingegeben wurde
	 * @param inputPanel Das Panel indem das InputField ist um einen error zu setzen
	 * @return <code>true</code> wenn es keinen fehler gab, ansonsten
	 *         <code>false</code>
	 */
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
			inputPanel.setError("Zu gro\u00df");
			return false;
		}

		inputPanel.setError("");
		return true;
	}

	/**
	 * Prüft ob die übergebenen Inputs erlaubt sind.
	 *
	 * @param inputFields Die Inputfields indem der Wert eingegeben wurde als Array
	 * @param inputPanels Die Panels indem die InputFields sind als Array um einen
	 *                   error zu setzen
	 * @return <code>true</code> wenn es keinen fehler gab, ansonsten
	 *         <code>false</code>
	 */
	public static boolean validateGameInput(InputTextField[] inputFields, InputPanel[] inputPanels) {
		int[] inputs = new int[5];
		int invalidInputs = 0;

		try {
			inputs[0] = inputFields[0].getIntValue();
		} catch (NumberFormatException e) {
			inputPanels[0].setError("Invalider Input");
			invalidInputs++;
		}
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

		if (invalidInputs > 0) {
			return false;
		}

		int percOccupied = (inputs[1] * 2 + inputs[2] * 3 + inputs[3] * 4 + inputs[4] * 5) * 100
				/ (inputs[0] * inputs[0]);

		if (inputs[0] < 5) {
			inputPanels[0].setError("Zu klein");
			return false;
		} else if (inputs[0] > 30) {
			inputPanels[0].setError("Zu gro\u00df");
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

	/**
	 * Liefert zurück ob der Input eine valid IPv4 ist
	 *
	 * @param ip Die IP als <code>String</code>
	 * @return <code>true</code> wenn die IP valid ist, ansonsten <code>false</code>
	 */
	public static boolean validateIP(String ip) {
		return ip.matches("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	}

	/**
	 * Liefert zurück ob der Input eine valid HEX-Color scheme ist.
	 *
	 * @param color Die HEX-Color als <code>String</code> mit vorangehendem
	 *           <code>#</code>.
	 * @return <code>true</code> wenn die HEX-Color valid ist, ansonsten
	 *         <code>false</code>
	 */
	public static boolean validateHEXColor(String color) {
		return color.matches("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
	}

	/**
	 * Liefert zurück ob der Name ein Name einer Speicherdatei sein kann.
	 *
	 * @param filename The name of the File
	 * @return <code>true</code> wenn es das Schema einer Speicherdatei hat
	 */
	public static boolean validateSavefileName(String filename) {
		return filename.matches("^\\d+#.+\\.json$");
	}

	/**
	 * Dreht ein Bild um 90° im Uhrzeigersinn.
	 *
	 * @param sourceImage Des Bild das gedreht werden soll als {@link BufferedImage}
	 * @return Das gedrehte Bild als {@link BufferedImage}
	 */
	public static BufferedImage rotateImage(BufferedImage sourceImage) {
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();

		BufferedImage rotatedImage = new BufferedImage(height, width, sourceImage.getType());

		Graphics2D graphics2D = rotatedImage.createGraphics();
		graphics2D.translate((height - width) / 2, (height - width) / 2);
		graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
		graphics2D.drawRenderedImage(sourceImage, null);

		return rotatedImage;
	}
}

/**
 * Wird intern genutzt um die Werte zu speichern, enthält auch Standardwerte.
 */
class SettingsValues {
	/**
	 * Hintergrundfarbe der UI als HEX.
	 */
	private String color_background = "#FFFFFF";
	/**
	 * Schriftfarbe der UI als HEX.
	 */
	private String color_foreground = "#000000";
	/**
	 * Button Hintergrundfarbe der UI als HEX.
	 */
	private String color_button_background = "#FFFFFF";
	/**
	 * Button Schriftfarbe der UI als HEX.
	 */
	private String color_button_foreground = "#000000";
	/**
	 * Error Schriftfarbe der UI als HEX.
	 */
	private String color_error = "#FF0000";
	/**
	 * Borderdarbe der UI als HEX.
	 */
	private String color_border = "#000000";

	/**
	 * Größe eines Bildes, als auch eines Tiles.
	 */
	private String settings_image_size = "100";
	/**
	 * Die kleine Schriftgröße.
	 */
	private String settings_fontsize_small = "15";
	/**
	 * Die große Schriftgröße.
	 */
	private String settings_fontsize_large = "25";
	/**
	 * Normale Breite eines UI Elements.
	 */
	private String settings_items_width = "500";
	/**
	 * Normale Höhe eines UI Elements.
	 */
	private String settings_items_height = "70";
	/**
	 * Kleine Breite eines UI Elements.
	 */
	private String settings_items_small_width = "350";
	/**
	 * Kleine Höhe eines UI Elements.
	 */
	private String settings_items_small_height = "55";
	/**
	 * Normales Padding von UI Elementen.
	 */
	private String settings_items_padding = "20";
	/**
	 * Normale Borderbreite von UI Elementen.
	 */
	private String settings_border_width = "2";
	/**
	 * Normaler Borderradius von UI Elementen.
	 */
	private String settings_border_radius = "15";
	/**
	 * Name des aktuellen Themes.
	 */
	private String settings_theme_path = "Default";

	/**
	 * Bild von Wasser.
	 */
	private BufferedImage image_water;
	/**
	 * Bild von Wasser auf das geschossen wurde.
	 */
	private BufferedImage image_water_destroyed;
	/**
	 * Bild von Wolken.
	 */
	private BufferedImage image_clouds;
	/**
	 * Bild eines Schiffteils, auf das geschossen wurde.
	 */
	private BufferedImage image_ship_destroyed;
	/**
	 * Bild des 2er Schiffs, zerstört.
	 */
	private BufferedImage image_ship_2_destroyed;
	/**
	 * Bild des 2er Schiffs, ganz.
	 */
	private BufferedImage image_ship_2_healthy;
	/**
	 * Bild des 3er Schiffs, zerstört.
	 */
	private BufferedImage image_ship_3_destroyed;
	/**
	 * Bild des 3er Schiffs, ganz.
	 */
	private BufferedImage image_ship_3_healthy;
	/**
	 * Bild des 4er Schiffs, zerstört.
	 */
	private BufferedImage image_ship_4_destroyed;
	/**
	 * Bild des 4er Schiffs, ganz.
	 */
	private BufferedImage image_ship_4_healthy;
	/**
	 * Bild des 5er Schiffs, zerstört.
	 */
	private BufferedImage image_ship_5_destroyed;
	/**
	 * Bild des 5er Schiffs, ganz.
	 */
	private BufferedImage image_ship_5_healthy;

	/**
	 * Pfad zur <code>colors.json</code> file.
	 */
	private String colorsFilePath = "";
	/**
	 * Der Inhalt von <code>settings.json</code> als String.
	 */
	private String settingsFileString = "";
	/**
	 * Der Inhalt von <code>colors.json</code> als String.
	 */
	private String colorsFileString = "";

	/**
	 * Holt sich die generellen Einstellungen
	 */
	public SettingsValues() {
		try {
			File settingsFile = new File(SettingsHandler.settingsFilePath);
			Scanner settingsFileReader = new Scanner(settingsFile);
			while (settingsFileReader.hasNextLine()) {
				settingsFileString += settingsFileReader.nextLine() + "\n";
			}
			settingsFileReader.close();

			int posOfColon, firstQuote, secondQuote;
			String name;

			name = "settings.image.size";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.fontsize.small";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.fontsize.large";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.items.width";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.items.height";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.items.small.width";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.items.small.height";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.items.padding";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.border.width";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.border.radius";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			name = "settings.theme.path";
			posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = settingsFileString.indexOf("\"", firstQuote);
			this.setValue(name, settingsFileString.substring(firstQuote, secondQuote));

			String internalCurrentThemePath = SettingsHandler.themesFolderPath + File.separator
					+ settingsFileString.substring(firstQuote, secondQuote) + File.separator;

			try {
				image_water = ImageIO.read(new File(internalCurrentThemePath + "water.png"));
				image_water_destroyed = ImageIO.read(new File(internalCurrentThemePath + "water_destroyed.png"));
				image_clouds = ImageIO.read(new File(internalCurrentThemePath + "clouds.png"));
				image_ship_destroyed = ImageIO.read(new File(internalCurrentThemePath + "ship_destroyed.png"));
				image_ship_2_destroyed = ImageIO.read(new File(internalCurrentThemePath + "ship_2_destroyed.png"));
				image_ship_2_healthy = ImageIO.read(new File(internalCurrentThemePath + "ship_2_healthy.png"));
				image_ship_3_destroyed = ImageIO.read(new File(internalCurrentThemePath + "ship_3_destroyed.png"));
				image_ship_3_healthy = ImageIO.read(new File(internalCurrentThemePath + "ship_3_healthy.png"));
				image_ship_4_destroyed = ImageIO.read(new File(internalCurrentThemePath + "ship_4_destroyed.png"));
				image_ship_4_healthy = ImageIO.read(new File(internalCurrentThemePath + "ship_4_healthy.png"));
				image_ship_5_destroyed = ImageIO.read(new File(internalCurrentThemePath + "ship_5_destroyed.png"));
				image_ship_5_healthy = ImageIO.read(new File(internalCurrentThemePath + "ship_5_healthy.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.colorsFilePath = internalCurrentThemePath + "colors.json";
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Holt sich das Colorscheme des aktuellen Themes
	 */
	public void pullColors() {
		try {
			File colorsFile = new File(this.colorsFilePath);
			Scanner colorsFileReader = new Scanner(colorsFile);
			while (colorsFileReader.hasNextLine()) {
				colorsFileString += colorsFileReader.nextLine() + "\n";
			}
			colorsFileReader.close();

			int posOfColon, firstQuote, secondQuote;
			String name;

			name = "color.background";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));

			name = "color.foreground";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));

			name = "color.button.background";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));

			name = "color.button.foreground";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));

			name = "color.error";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));

			name = "color.border";
			posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			secondQuote = colorsFileString.indexOf("\"", firstQuote);
			this.setValue(name, colorsFileString.substring(firstQuote, secondQuote));
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Liefert intern den Wert einer Einstellung zurück.
	 *
	 * @param name Name der Einstellung als <code>String</code>
	 * @return Wert der Einstellung als <code>String</code>
	 */
	public String getValue(String name) {
		if (name.equals("settings.image.size")) {
			return this.settings_image_size;
		} else if (name.equals("settings.fontsize.small")) {
			return this.settings_fontsize_small;
		} else if (name.equals("settings.fontsize.large")) {
			return this.settings_fontsize_large;
		} else if (name.equals("settings.items.width")) {
			return this.settings_items_width;
		} else if (name.equals("settings.items.height")) {
			return this.settings_items_height;
		} else if (name.equals("settings.items.small.width")) {
			return this.settings_items_small_width;
		} else if (name.equals("settings.items.small.height")) {
			return this.settings_items_small_height;
		} else if (name.equals("settings.items.padding")) {
			return this.settings_items_padding;
		} else if (name.equals("settings.border.width")) {
			return this.settings_border_width;
		} else if (name.equals("settings.border.radius")) {
			return this.settings_border_radius;
		} else if (name.equals("settings.theme.path")) {
			return this.settings_theme_path;
		} else if (name.equals("color.background")) {
			return this.color_background;
		} else if (name.equals("color.foreground")) {
			return this.color_foreground;
		} else if (name.equals("color.button.background")) {
			return this.color_button_background;
		} else if (name.equals("color.button.foreground")) {
			return this.color_button_foreground;
		} else if (name.equals("color.error")) {
			return this.color_error;
		} else if (name.equals("color.border")) {
			return this.color_border;
		} else {
			return "";
		}
	}

	/**
	 * Setzt intern den Wert einer Einstellung auf einen neuen Wert.
	 *
	 * @param name  Name der Einstellung als <code>String</code>
	 * @param value Neuer Wert der Einstellugn als <code>String</code>
	 */
	public void setValue(String name, String value) {
		if (name.equals("settings.image.size")) {
			this.settings_image_size = value;
		} else if (name.equals("settings.fontsize.small")) {
			this.settings_fontsize_small = value;
		} else if (name.equals("settings.fontsize.large")) {
			this.settings_fontsize_large = value;
		} else if (name.equals("settings.items.width")) {
			this.settings_items_width = value;
		} else if (name.equals("settings.items.height")) {
			this.settings_items_height = value;
		} else if (name.equals("settings.items.small.width")) {
			this.settings_items_small_width = value;
		} else if (name.equals("settings.items.small.height")) {
			this.settings_items_small_height = value;
		} else if (name.equals("settings.items.padding")) {
			this.settings_items_padding = value;
		} else if (name.equals("settings.border.width")) {
			this.settings_border_width = value;
		} else if (name.equals("settings.border.radius")) {
			this.settings_border_radius = value;
		} else if (name.equals("settings.theme.path")) {
			this.settings_theme_path = value;
		} else if (name.equals("color.background")) {
			this.color_background = value;
		} else if (name.equals("color.foreground")) {
			this.color_foreground = value;
		} else if (name.equals("color.button.background")) {
			this.color_button_background = value;
		} else if (name.equals("color.button.foreground")) {
			this.color_button_foreground = value;
		} else if (name.equals("color.error")) {
			this.color_error = value;
		} else if (name.equals("color.border")) {
			this.color_border = value;
		}

		if (name.startsWith("settings.")) {
			int posOfColon = settingsFileString.indexOf(":", settingsFileString.indexOf(name));
			int firstQuote = settingsFileString.indexOf("\"", posOfColon) + 1;
			int secondQuote = settingsFileString.indexOf("\"", firstQuote);
			settingsFileString = settingsFileString.substring(0, firstQuote) + value
					+ settingsFileString.substring(secondQuote);

			try {
				File settingsFile = new File(SettingsHandler.settingsFilePath);
				FileWriter fileWriter = new FileWriter(settingsFile);
				fileWriter.append(settingsFileString);
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (name.startsWith("color.")) {
			int posOfColon = colorsFileString.indexOf(":", colorsFileString.indexOf(name));
			int firstQuote = colorsFileString.indexOf("\"", posOfColon) + 1;
			int secondQuote = colorsFileString.indexOf("\"", firstQuote);
			colorsFileString = colorsFileString.substring(0, firstQuote) + value
					+ colorsFileString.substring(secondQuote);

			try {
				File colorsFile = new File(colorsFilePath);
				FileWriter fileWriter = new FileWriter(colorsFile);
				fileWriter.append(colorsFileString);
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Liefert intern ein Bild {@link BufferedImage} zurück.
	 *
	 * @param name Der Name des Bilds
	 * @return Das Bild als {@link BufferedImage}
	 */
	public BufferedImage getImage(String name) {
		if (name.equals("image_clouds")) {
			return image_clouds;
		} else if (name.equals("image_water")) {
			return image_water;
		} else if (name.equals("image_water_destroyed")) {
			return image_water_destroyed;
		} else if (name.equals("image_ship_destroyed")) {
			return image_ship_destroyed;
		} else if (name.equals("image_ship_2_destroyed")) {
			return image_ship_2_destroyed;
		} else if (name.equals("image_ship_2_healthy")) {
			return image_ship_2_healthy;
		} else if (name.equals("image_ship_3_destroyed")) {
			return image_ship_3_destroyed;
		} else if (name.equals("image_ship_3_healthy")) {
			return image_ship_3_healthy;
		} else if (name.equals("image_ship_4_destroyed")) {
			return image_ship_4_destroyed;
		} else if (name.equals("image_ship_4_healthy")) {
			return image_ship_4_healthy;
		} else if (name.equals("image_ship_5_destroyed")) {
			return image_ship_5_destroyed;
		} else if (name.equals("image_ship_5_healthy")) {
			return image_ship_5_healthy;
		} else {
			return null;
		}
	}
}
