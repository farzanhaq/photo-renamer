package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Create and display the graphical user interface of the application.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class Explorer extends FileChooserButtonListener {

	/**
	 * Instantiates a new explorer.
	 *
	 * @param frame
	 *            frame of the explorer
	 * @param label
	 *            label for the explorer
	 * @param textArea
	 *            text area of the explorer
	 * @param chooser
	 *            chooser of the explorer
	 */
	public Explorer(
			final JFrame frame, final JLabel label,
			final JTextArea textArea, final JFileChooser chooser) {
		super(frame, label, textArea, chooser);
	}

	/**
	 * Creates and returns the window for the explorer.
	 *
	 * @return window for the explorer
	 */
	public static JFrame buildWindow() {
		JFrame mainFrame = new JFrame("Photo Renamer");
		mainFrame.setResizable(false);

		JLabel mainLabel = new JLabel("Select an option");

		JFileChooser directoryChooser = new JFileChooser();
		JFileChooser fileChooser = new JFileChooser();

		// Restricts the user to select a directory or a file
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// Filters the file types to specified image extensions
		FileFilter filter =
				new FileNameExtensionFilter(
						"Image Files", ImageIO.getReaderFileSuffixes());
		directoryChooser.setFileFilter(filter);
		fileChooser.setFileFilter(filter);

		// Sets up the area for the contents and displays the instructions
		JTextArea textArea =
				new JTextArea(
						ExplorerManager.NUMBER_OF_ROWS,
						ExplorerManager.NUMBER_OF_COLUMNS);
		ExplorerManager.buildWindowText(textArea);

		// Puts it in a scroll pane in case the output is long
		JScrollPane scrollPane = new JScrollPane(textArea);

		// The directory choosing button.
		JButton openButton = new JButton("View Directory Contents");
		ExplorerManager.buttonHelper(openButton);
		ActionListener directoryListener =
				new DirectoryChooserButtonListener(
						mainFrame, mainLabel, textArea,
				directoryChooser);
		openButton.addActionListener(directoryListener);

		// The file choosing button
		JButton chooseButton = new JButton("Select Image File");
		ExplorerManager.buttonHelper(chooseButton);
		ActionListener fileListener =
				new FileChooserButtonListener(
						mainFrame, mainLabel, textArea, fileChooser);
		chooseButton.addActionListener(fileListener);

		// The perform action button
		JButton selectButton = new JButton("Perform Action");
		ExplorerManager.buildWindowButtonActionListener(
				selectButton, buildRenamer(), mainFrame);

		// The manipulate tags button
		JButton manipulateButton = new JButton("Manipulate Tags");
		ExplorerManager.buildWindowButtonActionListener(
				manipulateButton, buildManipulator(), mainFrame);

		// The view log button
		JButton logButton = new JButton("View Log");
		// buildRenamer() as a parameter doesn't affect functionality here,
		// it is only required to call the function
		ExplorerManager.buildWindowButtonActionListener(
				logButton, buildRenamer(), mainFrame);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the buttons to the panel
		buttonPanel.add(openButton);
		buttonPanel.add(chooseButton);
		buttonPanel.add(selectButton);
		buttonPanel.add(manipulateButton);
		buttonPanel.add(logButton);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the label to the panel
		topPanel.add(mainLabel);

		// Adds the panels to the container
		Container c = mainFrame.getContentPane();
		c.add(topPanel, BorderLayout.PAGE_START);
		c.add(scrollPane, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.PAGE_END);

		mainFrame.pack();
		return mainFrame;
	}

	/**
	 * Builds and returns the frame of the renamer.
	 *
	 * @return frame of the renamer
	 */
	public static JFrame buildRenamer() {
		JFrame renameFrame = new JFrame("Rename Image");
		renameFrame.setResizable(false);

		// Sets up the area for the renamer and displays the instructions
		JTextArea textArea =
				new JTextArea(
						ExplorerManager.NUMBER_OF_ROWS,
						ExplorerManager.NUMBER_OF_COLUMNS);
		ExplorerManager.buildRenamerText(textArea);

		// The field for user input
		JTextField textField = new JTextField();
		textField.setEditable(true);

		// Put it in a scroll pane in case the output is long
		JScrollPane scrollPane = new JScrollPane(textArea);

		// The insert tag(s) button
		JButton addButton = new JButton("Insert Tag(s)");
		ExplorerManager.buttonHelper(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ArrayList<String> manipulateTagList = new ArrayList<String>();
				String[] tagInput = textField.getText().split("\\s+");
				String currPath = getFilePath();

				// Checks if path is null and the input is valid
				if (currPath != null && !textField.getText().equals("")) {
					// Iterates over tags from the user input
					for (String t : tagInput) {
						// Deserialize files
						ExplorerManager.deserialize();

						// Adds the tag to the file path
						Tag.addTag(currPath, t);

						// Adds to the manipulate tag list if tag does not exist
						if (!manipulateTagList.contains(t)) {
							manipulateTagList.add(t);
						}

						// Modifies current path to most recent path in history
						if (Tag.fileHistory(currPath).size() > 0) {
							currPath =
									Tag.fileHistory(currPath).get(
											Tag.fileHistory(
													currPath).size() - 1);
							setFilePath(currPath);
						}
					}
					// Adds the tag to master tags
					Tag.manipulateTags(manipulateTagList, "add");

					// Updates the text area with the updated path
					textArea.setText(currPath);

					// Serializes files
					ExplorerManager.serialize(true);
				}
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				renameFrame.dispose();
			}
		});

		// The remove tag(s) button
		JButton deleteButton = new JButton("Remove Tag(s)");
		ExplorerManager.buttonHelper(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				String[] tagInput = textField.getText().split("\\s+");
				String currPath = getFilePath();

				// Checks if path is not null and the input is valid
				if (currPath != null && !textField.getText().equals("")) {
					// Iterates over tags from the user input
					for (String t : tagInput) {
						// Deserialize file
						ExplorerManager.deserialize();

						// Deletes the tag from the file path
						Tag.deleteTag(currPath, t);

						// Modifies current path to most recent path in history
						if (Tag.fileHistory(currPath).size() > 0) {
							currPath =
									Tag.fileHistory(currPath).get(
											Tag.fileHistory(
													currPath).size() - 1);
							setFilePath(currPath);
						}
					}
				}
				// Updates the text area with the updated path
				textArea.setText(currPath);

				// Serialize files
				ExplorerManager.serialize(true);
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				renameFrame.dispose();
			}
		});

		// The view history button
		JButton historyButton = new JButton("View History");
		ExplorerManager.buttonHelper(historyButton);
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (getFilePath() != null) {
					// Builds a new JFrame
					buildReverter().setVisible(true);
				} else {
					// Builds a new JFrame
					buildWindow().setVisible(true);
				}
				// Disposes the current JFrame
				renameFrame.dispose();
			}
		});

		// The select from master button
		JButton masterButton = new JButton("Select From Master");
		ExplorerManager.buttonHelper(masterButton);
		masterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Builds a new JFrame and disposes the current one
				buildReverter().setVisible(true);
				renameFrame.dispose();
			}
		});

		// The cancel button
		JButton cancelButton = new JButton("Cancel");
		ExplorerManager.buttonHelper(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				renameFrame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the buttons to the panel
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(historyButton);
		buttonPanel.add(cancelButton);

		// Adds the panels to the container
		Container c = renameFrame.getContentPane();
		c.add(scrollPane, BorderLayout.PAGE_START);
		c.add(textField, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.PAGE_END);

		renameFrame.pack();
		return renameFrame;
	}

	/**
	 * Builds and returns the frame of the manipulator.
	 *
	 * @return frame of the manipulator
	 */
	public static JFrame buildManipulator() {
		JFrame manipulateFrame = new JFrame("Manipulate Tags");
		manipulateFrame.setResizable(false);

		// Sets up the area for the manipulator and displays the instructions
		JTextArea textArea =
				new JTextArea(
						ExplorerManager.NUMBER_OF_ROWS,
						ExplorerManager.NUMBER_OF_COLUMNS);
		ExplorerManager.buildManipulatorText(textArea);

		// The field for user input
		JTextField textField = new JTextField();
		textField.setEditable(true);

		// Put it in a scroll pane in case the output is long
		JScrollPane scrollPane = new JScrollPane(textArea);

		// The add to master tags button
		JButton addToMasterButton = new JButton("Add To Master Tags");
		ExplorerManager.buttonHelper(addToMasterButton);
		addToMasterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ArrayList<String> manipulateTagList = new ArrayList<String>();
				String[] tagInput = textField.getText().split("\\s+");

				// Checks if path is not null and the input is valid
				if (tagInput != null && !textField.getText().equals("")) {
					// Iterates over tags from user input
					for (String t : tagInput) {
						// Deserialize files
						ExplorerManager.deserialize();
						if (FileHistory.retrieveTagsFromFile("History_Tags.ser")
								!= null) {
							TagManager.getInstance().setMasterTags(
											FileHistory.retrieveTagsFromFile(
													"History_Tags.ser"));
						}

						// Adds to the manipulate tag list if tag does not exist
						if (!manipulateTagList.contains(t)) {
							manipulateTagList.add(t);
						}
					}
					// Adds the tag to master tags
					Tag.manipulateTags(manipulateTagList, "add");
				}
				// Updates the text area with the updated master tag list
				textArea.setText(
						TagManager.getInstance().getMasterTags().toString());
				// Serialize files
				ExplorerManager.serialize(true);
			}
		});

		// The remove from master tags button
		JButton deleteFromMasterButton = new JButton("Remove From Master Tags");
		ExplorerManager.buttonHelper(deleteFromMasterButton);
		deleteFromMasterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ArrayList<String> manipulateTagList = new ArrayList<String>();
				String[] tagInput = textField.getText().split("\\s+");

				// Checks if path is not null and the input is valid
				if (tagInput != null && !textField.getText().equals("")) {
					// Iterates over tags from user input
					for (String t : tagInput) {
						// Deserialize files
						ExplorerManager.deserialize();
						if (FileHistory.retrieveTagsFromFile("History_Tags.ser")
								!= null) {
							TagManager.getInstance().setMasterTags(
											FileHistory.retrieveTagsFromFile(
													"History_Tags.ser"));
						}

						// Adds to the manipulate tag list if tag does not exist
						if (!manipulateTagList.contains(t)) {
							manipulateTagList.add(t);
						}
					}
					// Deletes the tag from master tags
					Tag.manipulateTags(manipulateTagList, "delete");
				}

				// Serialize files
				ExplorerManager.serialize(true);
				// Updates the text area with the updated master tag list
				textArea.setText(
						TagManager.getInstance().getMasterTags().toString());
			}
		});

		// The rename from master tags button
		JButton renameFromMasterButton = new JButton("Rename From Master Tags");
		ExplorerManager.buttonHelper(renameFromMasterButton);
		renameFromMasterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Checks if path is not null
				if (getFilePath() != null) {
					// Builds a new JFrame
					buildMaster().setVisible(true);
				} else {
					// Builds a new JFrame
					buildWindow().setVisible(true);
				}
				// Disposes current JFrame
				manipulateFrame.dispose();
			}
		});

		// The cancel button
		JButton cancelButton = new JButton("Cancel");
		ExplorerManager.buttonHelper(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				manipulateFrame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the buttons to the panel
		buttonPanel.add(addToMasterButton);
		buttonPanel.add(deleteFromMasterButton);
		buttonPanel.add(renameFromMasterButton);
		buttonPanel.add(cancelButton);

		Container c = manipulateFrame.getContentPane();
		// Adds the panels to the container
		c.add(scrollPane, BorderLayout.PAGE_START);
		c.add(textField, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.PAGE_END);

		manipulateFrame.pack();
		return manipulateFrame;
	}

	/**
	 * Builds and returns the frame of the master.
	 *
	 * @return frame of the master
	 */
	public static JFrame buildMaster() {
		JFrame masterFrame = new JFrame("Rename");
		masterFrame.setResizable(false);

		// Deserialize files
		ExplorerManager.deserialize();

		// Stores the object from deserialized file
		ArrayList<String> fileNames =
				FileHistoryTags.setOfMasterTag(FileHistory.getNewTags(),
				FileHistory.getNewContent());

		// Clickable list to select files
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JList list = new JList(fileNames.toArray());
		list.setVisibleRowCount(fileNames.size());
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Put it in a scroll pane in case the output is long
		JScrollPane scrollPane = new JScrollPane(list);

		// The insert tag button
		JButton insertButton = new JButton("Insert Tag");
		ExplorerManager.buttonHelper(insertButton);
		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ArrayList<Object> tagInput = new ArrayList<Object>();
				String currPath = getFilePath();

				// Retrieve the user input from selected tags
				for (Object i : list.getSelectedValuesList()) {
					tagInput.add(i);
				}

				// Checks if path is not null and the input is valid
				if (currPath != null && list.getSelectedValue() != null) {
					// Iterates over tags from selected tags
					for (Object t : tagInput) {
						// Adds the tag to the file path
						Tag.addTag(currPath, t.toString());

						// Modifies current path to most recent path in history
						if (Tag.fileHistory(currPath).size() > 0) {
							currPath
							= Tag.fileHistory(currPath).get(
									Tag.fileHistory(currPath).size() - 1);
						}
					}

					// Serialize files
					ExplorerManager.serialize(true);
				}
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				masterFrame.dispose();
			}
		});

		// The remove button.
		JButton removeButton = new JButton("Remove Tag");
		ExplorerManager.buttonHelper(removeButton);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ArrayList<Object> tagInput = new ArrayList<Object>();
				String currPath = getFilePath();

				// Retrieve the user input from selected tags
				for (Object i : list.getSelectedValuesList()) {
					tagInput.add(i);
				}

				// Checks if path is not null and the input is valid
				if (currPath != null && list.getSelectedValue() != null) {
					// Iterates over tags from selected tags
					for (Object t : tagInput) {
						// Deletes the tag from the file path
						Tag.deleteTag(currPath, t.toString());

						// Modifies current path to most recent path in history
						if (Tag.fileHistory(currPath).size() > 0) {
							currPath =
									Tag.fileHistory(currPath).get(
											Tag.fileHistory(
													currPath).size() - 1);
						}
					}

					// Serialize files
					ExplorerManager.serialize(true);
				}
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				masterFrame.dispose();
			}
		});

		// The cancel button.
		JButton cancelButton = new JButton("Cancel");
		ExplorerManager.buttonHelper(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				masterFrame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the buttons to the panel
		buttonPanel.add(insertButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(cancelButton);

		Container c = masterFrame.getContentPane();
		// Adds the panels to the container
		c.add(scrollPane, BorderLayout.PAGE_START);
		c.add(buttonPanel, BorderLayout.PAGE_END);
		masterFrame.pack();
		return masterFrame;
	}

	/**
	 * Builds and returns the frame of the reverter.
	 *
	 * @return frame of the reverter
	 */
	public static JFrame buildReverter() {
		JFrame revertFrame = new JFrame("Revert");
		revertFrame.setResizable(false);

		// Deserialize file
		FileHistory.retrieveHistoryFromFile("History_File.ser");

		// Retrieves the original file path without the tags
		int prefixIndex = getFilePath().indexOf("@");
		int ext = getFilePath().lastIndexOf(".");
		String concatPath =
				getFilePath().substring(0, prefixIndex)
				+ getFilePath().substring(ext);
		ArrayList<String> historicalNameList =
				FileHistoryTags.historicalNames(
						concatPath, FileHistory.getNewHistory());
		ArrayList<String> fileNames = new ArrayList<>();
		String newConcatPath = getFilePath().substring(0, prefixIndex);

		// Checks if historicalNameList is not null
		if (historicalNameList != null) {
			// Iterates over historicalNameList
			for (int i = 0; i < historicalNameList.size() - 1; i++) {
				// Checks if item at index not equal to the item at next index
				if (!historicalNameList.get(i).equals(
						historicalNameList.get(i + 1))) {
					// Adds item at index to list
					fileNames.add(historicalNameList.get(i));
					// Adds the final item to list
				}
				if (i == historicalNameList.size() - 2) {
					fileNames.add(historicalNameList.get(i));
				}
			}
		}

		// Stores file names for original file names
		ArrayList<String> newList = new ArrayList<String>();

		// Iterates over fileNames and avoids fileNames
		// that are not extension of original file name
		for (int f = 0; f < fileNames.size() - 1; f++) {
			if (fileNames.get(f).contains(newConcatPath)) {
				newList.add(fileNames.get(f));
			}

			if ((f == fileNames.size() - 2)) {
				newList.add(fileNames.get(f + 1));
			}
		}

		//Stores file names without duplicity from fileNames
		ArrayList<String> finalList = new ArrayList<String>();

		// Iterates over fileNames and updates newList with new file names
		for (String t : newList) {
			if (finalList.contains(t)) {
				break;
			} else {
				finalList.add(t);
			}
		}

		// Clickable list to select a file
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JList list = new JList(finalList.toArray());
		list.setVisibleRowCount(finalList.size());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Put it in a scroll pane in case the output is long.
		JScrollPane scrollPane = new JScrollPane(list);

		// The revert button
		JButton revertButton = new JButton("Revert");
		ExplorerManager.buttonHelper(revertButton);
		revertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Checks if path is not null and the input is valid
				if (getFilePath() != null && list.getSelectedValue() != null) {
					// Deserialize files
					ExplorerManager.deserialize();

					String selected = list.getSelectedValue().toString();

					// Revert to a previous file from the historicalNameList
					Tag.fileReversion(
							getFilePath(), selected, historicalNameList);

					// Serialize files
					ExplorerManager.serialize(true);
				}

				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				revertFrame.dispose();
			}
		});

		// The cancel button
		JButton cancelButton = new JButton("Cancel");
		ExplorerManager.buttonHelper(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// Builds a new JFrame and disposes the current one
				buildWindow().setVisible(true);
				revertFrame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Adds the buttons to the panel
		buttonPanel.add(revertButton);
		buttonPanel.add(cancelButton);

		Container c = revertFrame.getContentPane();
		// Adds the panel to the container
		c.add(scrollPane, BorderLayout.PAGE_START);
		c.add(buttonPanel, BorderLayout.PAGE_END);
		revertFrame.pack();
		return revertFrame;
	}
}
