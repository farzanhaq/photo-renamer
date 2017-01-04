package photo_renamer;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Helper functions for Explorer class.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class ExplorerManager {

	/**
	 * Prevents instantiation of utility class.
	 */
	private ExplorerManager() {
	}

	/**
	 * Constant NUMBER_OF_ROWS for JFrame arguments to avoid magic numbers.
	 */
	public static final int NUMBER_OF_ROWS = 25;
	/**
	 * Constant NUMBER_OF_COLUMNS for JFrame arguments to avoid magic numbers.
	 */
	public static final int NUMBER_OF_COLUMNS = 75;
	/**
	 * Prefix used when displaying nested files and directories.
	 */
	public static final String PREFIX = "--";

	/**
	 * Assigns common commands for buttons in the interface.
	 *
	 * @param buttonName
	 *            name of the button
	 */
	static void buttonHelper(final JButton buttonName) {
		// Assigns the common presets for buttons
		buttonName.setVerticalTextPosition(AbstractButton.CENTER);
		buttonName.setHorizontalTextPosition(AbstractButton.LEADING);
		buttonName.setMnemonic(KeyEvent.VK_D);
		buttonName.setActionCommand("disable");
	}

	/**
	 * Displays the instructions in the window text area.
	 *
	 * @param textArea
	 *            area in which the text is displayed
	 */
	public static void buildWindowText(final JTextArea textArea) {
		textArea.setText("Welcome to Photo Renamer - Developed By Farzan Haq & "
				+ "Pratiman Shahi \n\n INSTRUCTIONS \n\n "
				+ "1) To view a list of image files in a directory, click on "
				+ "view directory contents and choose a directory. \n "
				+ "2) To choose an image file or view directory it is in"
				+ ", click on select image file and "
				+ "choose an image. \n 3) To perform an action on the selected "
				+ "image file by typing, click on perform action. \n 4) "
				+ "To perform an action by selection or edit only the master "
				+ "tag list, click on manipulate tags.\n "
				+ "5) To view the log file in an external window, "
				+ "click on view log.");
		textArea.setEditable(false);
	}

	/**
	 * Displays the instructions in the renamer text area.
	 *
	 * @param textArea
	 *            area in which the text is displayed
	 */
	public static void buildRenamerText(final JTextArea textArea) {
		textArea.setText(Explorer.getFilePath() + "\n\n" + "INSTRUCTIONS: \n\n "
				+ "1) To insert or remove a tag, enter a single tag into the "
				+ "textbox, or multiple tags seperated by a space and click on"
				+ " the appropriate button. \n NOTE: Image file must be "
				+ "re-selected after reversion to 'perform actions'. \n 2) "
				+ "To view the historical file names of the selected file"
				+ " or revert to a previous file name click on the the view"
				+ " history button.");
		textArea.setEditable(false);
	}

	/**
	 * Displays the instructions in the manipulator text area.
	 *
	 * @param textArea
	 *            area in which the text is displayed
	 */
	public static void buildManipulatorText(final JTextArea textArea) {
		textArea.setText("INSTRUCTIONS: \n \n" + "1) To add or remove a tag to "
				+ "or from the master list of tags, enter a tag into the "
				+ "textbox and click on the appropriate button.\n2) To rename a"
				+ " file with a tag from the master tag list, click on rename"
				+ " from master tags. \n NOTE: Image file must be re-selected"
				+ " after reversion to 'perform actions'");
		textArea.setEditable(false);
	}

	/**
	 * Assigns the action listener for the main window buttons. Defines
	 * dependency between objects. By implementing this design pattern,
	 * observers are assigned in the form of action listeners on the frame,
	 * which observe if a specified event occurs. The action listeners in the
	 * Explorer class are also instances of the Observer design pattern.
	 *
	 * @param buttonName
	 *            name of the button
	 * @param buildFrame
	 *            frame for the button
	 * @param mainFrame
	 *            main frame
	 */
	public static void buildWindowButtonActionListener(
			final JButton buttonName, final JFrame buildFrame,
			final JFrame mainFrame) {
		if (buttonName.getText().equals("Perform Action")
				|| buttonName.getText().equals("Manipulate Tags")) {
			buttonHelper(buttonName);
			buttonName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					// Builds a new JFrame and disposes the current one
					buildFrame.setVisible(true);
					mainFrame.dispose();
				}
			});
		} else if (buttonName.getText().equals("View Log")) {
			buttonHelper(buttonName);
			buttonName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					File f = new File("system_log.xml");
					Desktop dt = Desktop.getDesktop();
					// Opens the log file on the users system if it exists
					try {
						dt.open(f);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Deserializes files.
	 */
	public static void deserialize() {
		FileHistory.retrieveFromFile("History.ser");
		FileHistory.retrieveHistoryFromFile("History_File.ser");
		FileHistory.retrieveTagsFromFile("History_Tags.ser");
	}

	/**
	 * Serializes files and updates the logger configuration file.
	 *
	 * @param log
	 *            the log
	 */
	public static void serialize(final boolean log) {
		if (log) {
			try {
				FileHistory.saveToFile(
						"History.ser", TagManager.getInstance().getTagMap());
				FileHistory.saveHistoryToFile(
						"History_File.ser", Tag.getAllPaths());
				FileHistory.saveTagsToFile(
						"History_Tags.ser",
						TagManager.getInstance().getMasterTags());
				try {
					Log.systemLogger();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				FileHistory.saveToFile(
						"History.ser", TagManager.getInstance().getTagMap());
				FileHistory.saveHistoryToFile(
						"History_File.ser", Tag.getAllPaths());
				FileHistory.saveTagsToFile(
						"History_Tags.ser",
						TagManager.getInstance().getMasterTags());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
