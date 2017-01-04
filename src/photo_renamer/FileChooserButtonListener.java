package photo_renamer;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * FileChooserButtonListener for the button to choose a file.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class FileChooserButtonListener implements ActionListener {
	/** The window the button is in. */
	private static JFrame fileFrame;
	/** The label for the full path to the chosen directory. */
	private JLabel fileLabel;
	/** The file chooser to use when the user clicks. */
	private static JFileChooser fileChooser;
	/** The area to use to display the nested directory contents. */
	private JTextArea textArea;
	/** The selected filePath. */
	private static String filePath;

	/**
	 * Gets the file path.
	 *
	 * @return string of the file path
	 */
	public static String getFilePath() {
		return FileChooserButtonListener.filePath;
	}

	/**
	 * Sets the filePath.
	 *
	 * @param filePath
	 *            path to set
	 */
	public static void setFilePath(final String filePath) {
		FileChooserButtonListener.filePath = filePath;
	}

	/**
	 * An action listener for window dirFrame, displaying a file path on
	 * dirLabel, using fileChooser to choose a file.
	 *
	 * @param fileFrame
	 *            the main window
	 * @param fileLabel
	 *            the label for the directory path
	 * @param fileChooser
	 *            the file chooser to use
	 * @param textArea
	 *            the text area to use
	 */
	public FileChooserButtonListener(
			final JFrame fileFrame, final JLabel fileLabel,
			final JTextArea textArea, final JFileChooser fileChooser) {
		this.fileFrame = fileFrame;
		this.fileLabel = fileLabel;
		this.textArea = textArea;
		this.fileChooser = fileChooser;
	}

	/**
	 * Handles the user clicking on the open button.
	 *
	 * @param e
	 *            the event object
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		int returnVal =
				fileChooser.showOpenDialog(
						fileFrame.getContentPane().getParent());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			// Retrieves and sets the name of the file path
			if (file.exists()) {
				if (file.isFile()) {
					String fileName = file.getPath();
					setFilePath(fileName);
				}

				fileLabel.setText("Selected File" + file.getAbsolutePath());
				// Display temporary message while directory is traversed.
				this.textArea.setText("Building file tree �");

				// Make the root.
				FileNode fileTree =
						new FileNode(file.getName(), null, FileType.DIRECTORY);
				ChooserButtonListenerManager.buildTree(file, fileTree);

				// Build string representation and put it into the text area.
				StringBuffer contents = new StringBuffer();
				ChooserButtonListenerManager.buildDirectoryContents(
						fileTree, contents, "");
				this.textArea.setText(contents.toString());
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(fileChooser.getCurrentDirectory());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			fileLabel.setText("No Path Selected");
		}
	}
}
