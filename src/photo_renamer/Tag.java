package photo_renamer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tag contains the common functionality for tags.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class Tag implements Serializable {

	/**
	 * Prevents instantiation of utility class.
	 */
	private Tag() {
	}

	private static final long serialVersionUID = -2940748953736911217L;
	/** PREFIX to distinguish a tag. */
	private static final String PREFIX = "@";
	/** List of file paths. */
	private static ArrayList<String> storePaths = new ArrayList<String>();
	/** Maps a file to its paths. */
	private static HashMap<File, ArrayList<String>> allPaths =
			new HashMap<File, ArrayList<String>>();

	/**
	 * Gets the paths from the static map.
	 *
	 * @return paths from the static map.
	 */
	public static HashMap<File, ArrayList<String>> getAllPaths() {
		return allPaths;
	}

	/**
	 * Sets the paths.
	 *
	 * @param allPaths
	 *            paths to be set.
	 */
	public static void setAllPaths(
			final HashMap<File, ArrayList<String>> allPaths) {
		Tag.allPaths = allPaths;
	}

	/**
	 * Adds or removes tags from masterTags depending on the given argument.
	 *
	 * @param tags
	 *            tag to be added or removed
	 * @param argument
	 *            condition determining if tag should be added or removed.
	 * @return list of tags after the addition or removal
	 */
	public static ArrayList<String> manipulateTags(
			final ArrayList<String> tags, final String argument) {
		if (argument == "add") {
			// Iterates over tags after checking if add condition is met
			for (String t : tags) {
				// Updates master tags by adding the string
				if (TagManager.getInstance().getMasterTags() == null
						|| !TagManager.getInstance().getMasterTags()
						.contains(t)) {
					TagManager.getInstance().getMasterTags().add(t);
				}
				// Returns the updated master tags
				return TagManager.getInstance().getMasterTags();
			}
		} else if (argument == "delete") {
			// Iterates over tags after checking if delete condition is met
			for (String t : tags) {
				// Removes the tag from the master tags
				TagManager.getInstance().getMasterTags().remove(t);
				// Updates master tags by removing the string
				if (FileHistory.getNewTags().contains(t)) {
					FileHistory.getNewTags().remove(t);
				}
			}
			// Returns the updated master tags
			return TagManager.getInstance().getMasterTags();
		}
		// If none of the conditions are met
		return null;
	}

	/**
	 * Renames a file from a specified filePath by adding a tag. Updates tagMap,
	 * fileMap, storePaths and fileList to keep track of added tags and new file
	 * paths.
	 *
	 * @param filePath
	 *            filePath to which a tag is added
	 * @param tag
	 *            tag being added
	 */
	public static void addTag(final String filePath, final String tag) {
		// Stores the paths in a list
		ArrayList<String> tempStorePaths = new ArrayList<String>();

		// Checks if the tag does not already exist in the file path
		if (!filePath.contains("@" + tag)) {
			int ext = filePath.lastIndexOf(".");
			String preTag = filePath.substring(0, ext);
			String postTag = filePath.substring(ext);
			String newFilePath = preTag + PREFIX + tag + postTag;
			ArrayList<File> fileList = new ArrayList<File>();
			ArrayList<String> tagList = new ArrayList<String>();
			File concatPath;
			File f = new File(filePath);

			// Extracts and stores the name of a file path without tags
			if (filePath.contains("@")) {
				int prefixIndex = filePath.indexOf("@");
				int extIndex = filePath.indexOf(".");
				concatPath = new File(filePath.substring(0, prefixIndex)
						+ filePath.substring(extIndex));
			} else {
				concatPath = new File(filePath);
			}

			// Adds and maps the tag to its corresponding file
			if (TagManager.getInstance().getFileMap().containsKey(concatPath)) {
				TagManager.getInstance().getFileMap().get(concatPath).add(tag);
			} else {
				TagManager.getInstance().getFileMap().put(concatPath, tagList);
				TagManager.getInstance().getFileMap().get(concatPath).add(tag);
				// Adds the original file path to the storage
				storePaths.add(filePath);
			}

			fileList.add(new File(newFilePath));
			// Adds the new file path to the storage
			tempStorePaths.add(newFilePath);
			storePaths.add(newFilePath);

			// Stores only the required tags
			ArrayList<String> reqTags =
					TagManager.getInstance().getFileMap().get(concatPath);

			// Updates tag map by associating the tag with the new file path
			if (reqTags != null) {
				for (String r : reqTags) {
					if (TagManager.getInstance().getTagMap().containsKey(r)) {
						TagManager.getInstance().getTagMap().get(r).remove(f);
						TagManager.getInstance().getTagMap()
						.get(r).add(new File(newFilePath));
					}
				}
			}

			// Updates tag map by associating the new tag with new file path
			if (TagManager.getInstance().getTagMap().containsKey(tag)) {
				TagManager.getInstance().getTagMap()
				.get(tag).add(new File(newFilePath));
			} else {
				TagManager.getInstance().getTagMap().put(tag, fileList);
			}

			// Renames the original file with the new file path
			f.renameTo(new File(newFilePath));
		}

		// Updates the file history
		tempFileHistory(filePath, tempStorePaths);
	}

	/**
	 * Renames a file from a specified filePath by deleting a tag. Updates
	 * tagMap, fileMap, storePaths and fileList to keep track of deleted tags
	 * and new file paths.
	 *
	 * @param filePath
	 *            filePath to which a tag is deleted
	 * @param tag
	 *            tag being deleted
	 */
	public static void deleteTag(final String filePath, final String tag) {
		// Stores the paths in a list
		ArrayList<String> tempStorePaths = new ArrayList<String>();

		// Checks if the tag already exists in the file path
		if (filePath.contains("@" + tag)) {
			String concatPath = null;
			String newFilePath = null;
			File f = new File(filePath);

			// Extracts and stores the name of a file path without tags
			if (filePath.contains("@")) {
				int prefixIndex = filePath.indexOf("@");
				int extIndex = filePath.indexOf(".");
				concatPath =
						(filePath.substring(0, prefixIndex)
								+ filePath.substring(extIndex));
			}

			// Removes and unmaps the tag from its corresponding file
			if (TagManager.getInstance().getFileMap()
					.containsKey(new File(concatPath))) {
				TagManager.getInstance().getFileMap()
				.get(new File(concatPath)).remove(tag);
			}

			// Stores only the required tags
			ArrayList<String> reqTags =
					TagManager.getInstance().getFileMap()
					.get(new File(concatPath));
			// Removes a tag from a file path by replacing it with empty string
			// http://stackoverflow.com/questions/24730871/replacing-only-exact-match
			newFilePath = filePath.replaceAll("@" + tag + "(?=@|$|\\.)", "");

			// Updates tag map by associating the tag with the new file path
			if (reqTags != null) {
				for (String r : reqTags) {
					if (TagManager.getInstance().getTagMap().containsKey(r)) {
						TagManager.getInstance().getTagMap().get(r).remove(f);
						TagManager.getInstance().getTagMap()
						.get(r).add(new File(newFilePath));
					}
				}
			}

			// Adds the new file path to the storage
			tempStorePaths.add(newFilePath);
			storePaths.add(newFilePath);

			// Updates tag map by dissociating the tag from the file
			if (TagManager.getInstance().getTagMap().containsKey(tag)) {
				TagManager.getInstance().getTagMap().get(tag).remove(f);
			}

			// Renames the original file with the new file path
			f.renameTo(new File(newFilePath));
		}

		// Updates the file history
		tempFileHistory(filePath, tempStorePaths);
	}

	/**
	 * Updates file history representation with tempStorePaths of a filePath.
	 *
	 * @param filePath
	 *            path to which stored paths are being added
	 * @param tempStorePaths
	 *            list of stored paths being added
	 */
	public static void tempFileHistory(final String filePath, final
			ArrayList<String> tempStorePaths) {
		ArrayList<String> list;
		String concatPath = new String();

		if (filePath.contains("@")) {
			int prefixIndex = filePath.indexOf("@");
			int extIndex = filePath.indexOf(".");
			concatPath = (filePath.substring(0, prefixIndex)
					+ filePath.substring(extIndex));
		File f = new File(concatPath);

		if (allPaths.containsKey(f)) {
			// Add the stored paths as the value of an existing file
			list = allPaths.get(f);
			list.addAll(tempStorePaths);
			allPaths.put(f, list);
		} else {
			// Creates a file as a key and the stored paths as its value
			list = new ArrayList<String>();
			list.addAll(tempStorePaths);
			allPaths.put(f, list);
		}

		}
		File f = new File(filePath);
		if (allPaths.containsKey(f)) {
			// Add the stored paths as the value of an existing file
			list = allPaths.get(f);
			list.addAll(tempStorePaths);
			allPaths.put(f, list);
		} else {
			// Creates a file as a key and the stored paths as its value
			list = new ArrayList<String>();
			list.addAll(tempStorePaths);
			allPaths.put(f, list);
		}
	}

	/**
	 * Returns the historical names of filePath.
	 *
	 * @param filePath
	 *            path to which stored paths are being added
	 * @return array list of historical paths
	 */
	public static ArrayList<String> fileHistory(final String filePath) {
		// Puts the stored paths as the value of the file with file path
		allPaths.put(new File(filePath), storePaths);
		return allPaths.get(new File(filePath));
	}

	/**
	 * Reverts a filePath to a previous name in storePaths being revertTo.
	 *
	 * @param filePath
	 *            name of the file path reverting from
	 * @param revertTo
	 *            name of the file path reverted to
	 * @param tempStorePaths
	 *            list of stored paths to revert to
	 */
	public static void fileReversion(final String filePath,
			final String revertTo, final ArrayList<String> tempStorePaths) {
		File f = new File(filePath);
		// Renames the file path with the path being reverted to
		f.renameTo(new File(revertTo));
		// Adds the new file path to the storage
		storePaths.add(revertTo);
		tempStorePaths.add(revertTo);
		// Updates the file history
		tempFileHistory(filePath, tempStorePaths);
	}
}
