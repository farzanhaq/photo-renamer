package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * FileHistory contains methods to serialize files and tags.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class FileHistory implements Serializable {

	private static final long serialVersionUID = 5691783525487984403L;
	/** New content to be serialized. */
	private static HashMap<String, ArrayList<File>> newContent =
			new HashMap<String, ArrayList<File>>();
	/** New history to be serialized. */
	private static HashMap<File, ArrayList<String>> newHistory =
			new HashMap<File, ArrayList<String>>();
	/** New tags to be serialized. */
	private static ArrayList<String> newTags = new ArrayList<String>();

	/**
	 * Prevents instantiation of utility class.
	 */
	private FileHistory() {
	}

	/**
	 * Gets the content from the static map.
	 *
	 * @return content from the static map
	 */
	public static HashMap<String, ArrayList<File>> getNewContent() {
		return newContent;
	}

	/**
	 * Sets the newContent.
	 *
	 * @param newContent
	 *            content to set
	 */
	public static void setNewContent(
			final HashMap<String, ArrayList<File>> newContent) {
		FileHistory.newContent = newContent;
	}

	/**
	 * Gets the new history from the static map.
	 *
	 * @return new history from the static map
	 */
	public static HashMap<File, ArrayList<String>> getNewHistory() {
		return newHistory;
	}

	/**
	 * Sets the newHistory.
	 *
	 * @param newHistory
	 *            new history to set
	 */
	public static void setNewHistory(
			final HashMap<File, ArrayList<String>> newHistory) {
		FileHistory.newHistory = newHistory;
	}

	/**
	 * Gets the tags from the list.
	 *
	 * @return tags from the list
	 */
	public static ArrayList<String> getNewTags() {
		return newTags;
	}

	/**
	 * Sets the newTags.
	 *
	 * @param newTags
	 *            tags to set
	 */
	public static void setNewTags(final ArrayList<String> newTags) {
		FileHistory.newTags = newTags;
	}

	/**
	 * Deserialize the data from filePath.
	 *
	 * @param filePath
	 *            string representation of file path
	 * @return map containing the contents retrieved
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, ArrayList<File>> retrieveFromFile(
			final String filePath) {
		File f = new File(filePath);

		if (f.exists() && !f.isDirectory()) {
			try {
				// Setting up the file for deserialization
				FileInputStream fis = new FileInputStream(filePath);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);

				// Updating static HashMap with deserialized content
				newContent =
						(HashMap<String, ArrayList<File>>) ois.readObject();
				ois.close();

				return newContent;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Serialize the data to filePath.
	 *
	 * @param filePath
	 *            string representation of the file path
	 * @param tagMap
	 *            map that is to be serialized
	 * @throws FileNotFoundException
	 *             handles the exception for file not found
	 */
	public static void saveToFile(
			final String filePath, final HashMap<String,
			ArrayList<File>> tagMap)
			throws FileNotFoundException {
		try {
			// Setting up the file for serialization
			FileOutputStream fos = new FileOutputStream(filePath);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			if (TagManager.getInstance().getTagMap() == newContent) {
				oos.writeObject(tagMap);
			} else {
				// If static HashMap has been updated with deserialized content
				HashMap<String, ArrayList<File>> finalContent =
						new HashMap<String, ArrayList<File>>();

				finalContent.putAll(newContent);
				finalContent.putAll(tagMap);
				oos.writeObject(finalContent);
			}
			oos.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Deserialize the data to obtain tags from filePathTags.
	 *
	 * @param filePathTags
	 *            string representation of file path with tags
	 * @return list containing the contents retrieved
	 */

	@SuppressWarnings("unchecked")
	public static ArrayList<String> retrieveTagsFromFile(
			final String filePathTags) {
		File f = new File(filePathTags);

		if (f.exists() && !f.isDirectory()) {
			try {
				// Setting up the file for deserialization
				FileInputStream fis = new FileInputStream(filePathTags);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);

				// Updating static ArrayList with deserialized content from file
				newTags = (ArrayList<String>) ois.readObject();
				ois.close();

				return newTags;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Serialize the data of the tags including masterTags to filePathTags.
	 *
	 * @param filePathTags
	 *            string representation of the file path
	 * @param masterTags
	 *            comprehensive list of tags
	 * @throws FileNotFoundException
	 *             handles the exception for file not found
	 */
	public static void saveTagsToFile(
			final String filePathTags, final ArrayList<String> masterTags)
			throws FileNotFoundException {
		try {
			// Setting up the file for serialization
			FileOutputStream fos = new FileOutputStream(filePathTags);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			newTags = new ArrayList<String>();

			if (masterTags == newTags) {
				oos.writeObject(masterTags);
			} else {
				// If static ArrayList has been updated with content from file
				ArrayList<String> finalContent = new ArrayList<String>();
				finalContent.addAll(newTags);
				finalContent.addAll(masterTags);
				oos.writeObject(finalContent);
			}
			oos.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Deserialize the data to obtain history from filePathHistory.
	 *
	 * @param filePathHistory
	 *            string representation of file path with the history
	 * @return map containing the contents retrieved
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<File, ArrayList<String>> retrieveHistoryFromFile(
			final String filePathHistory) {
		File f = new File(filePathHistory);

		if (f.exists() && !f.isDirectory()) {
			try {
				// Setting up the file for deserialization
				FileInputStream fis = new FileInputStream(filePathHistory);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);

				// Updating static HashMap with deserialized content from file
				newHistory =
						(HashMap<File, ArrayList<String>>) ois.readObject();
				ois.close();

				return newHistory;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Serialize the data of the history including allPaths to filePathHistory.
	 *
	 * @param filePathHistory
	 *            string representation of the file path
	 * @param allPaths
	 *            list of historical tags
	 * @throws FileNotFoundException
	 *             handles the exception for file not found
	 */
	public static void saveHistoryToFile(
			final String filePathHistory, final HashMap<File,
			ArrayList<String>> allPaths)
			throws FileNotFoundException {
		try {
			// Setting up the file for serialization
			FileOutputStream fos = new FileOutputStream(filePathHistory);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			if (allPaths == newHistory) {
				oos.writeObject(allPaths);
			} else {
				// If static HashMap has has been updated with content from file
				HashMap<File, ArrayList<String>> finalContent =
						new HashMap<File, ArrayList<String>>();
				finalContent.putAll(newHistory);
				finalContent.putAll(allPaths);
				oos.writeObject(finalContent);
			}
			oos.close();
		} catch (Exception e) {
		}
	}
}
