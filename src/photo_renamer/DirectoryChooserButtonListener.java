package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * DirectoryChooserButtonListener for the button to choose a directory.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class DirectoryChooserButtonListener implements ActionListener {
	/** The window the button is in. */
	private static JFrame dirFrame;
	/** The label for the full path to the chosen directory. */
	private JLabel dirLabel;
	/** The file chooser to use when the user clicks. */
	private static JFileChooser dirChooser;
	/** The area to use to display the nested directory contents. */
	private JTextArea textArea;
	/** The selected filePath. */
	private static String filePath;

	/**
	 * An action listener for window dirFrame, displaying a file path on
	 * dirLabel, using fileChooser to choose a file.
	 *
	 * @param dirFrame
	 *            the main window
	 * @param dirLabel
	 *            the label for the directory path
	 * @param dirChooser
	 *            the file chooser to use
	 * @param textArea
	 *            the text area to use
	 *
	 */
	public DirectoryChooserButtonListener(final JFrame dirFrame,
			final JLabel dirLabel, final JTextArea textArea,
			JFileChooser dirChooser) {
		this.dirFrame = dirFrame;
		this.dirLabel = dirLabel;
		this.textArea = textArea;
		this.dirChooser = dirChooser;
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
				dirChooser.showOpenDialog(
						dirFrame.getContentPane().getParent());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = dirChooser.getSelectedFile();

			// Checks if file exists
			if (file.exists()) {
				dirLabel.setText("Selected File" + file.getAbsolutePath());
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
			}
		} else {
			dirLabel.setText("No Path Selected");
		}
	}
}
