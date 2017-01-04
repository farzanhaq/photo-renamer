package photo_renamer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * FileHistoryTags contains methods pertaining to subsets of tags.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class FileHistoryTags {

	/**
	 * Prevents instantiation of utility class.
	 */
	private FileHistoryTags() {
	}

	/**
	 * Returns a list of tags from the newTags.
	 *
	 * @param newTags
	 *            new tags which are being iterated over
	 * @return list of tags from the newTags
	 */
	public static ArrayList<String> setOfTags(final ArrayList<String> newTags) {
		ArrayList<String> list = new ArrayList<String>();

		for (String tag : newTags) {
			// Avoid repetition of tags in the final list
			if (!list.contains(tag)) {
				list.add(tag);
			}
		}
		return list;
	}

	/**
	 * Returns a list of tags from the newContent.
	 *
	 * @param newContent
	 *            new content obtained from the FileHistory class
	 * @return list of tags from newContent
	 */
	public static ArrayList<String> setOfTagsFromAddTag(
			final HashMap<String, ArrayList<File>> newContent) {
		ArrayList<String> list = new ArrayList<String>();

		for (String tag : newContent.keySet()) {
			list.add(tag);
		}
		return list;
	}

	/**
	 * Returns a list of tags from the newContent with the newTags.
	 *
	 * @param newTags
	 *            new tags for the set of tags
	 * @param newContent
	 *            new content for the set of tags from add tag
	 * @return list of tags from newContent with newTags
	 */
	public static ArrayList<String> setOfMasterTag(
			final ArrayList<String> newTags,
			final HashMap<String, ArrayList<File>> newContent) {
		ArrayList<String> setOfTagsList = setOfTags(newTags);
		ArrayList<String> setOfTagsFromAddTagList =
				setOfTagsFromAddTag(newContent);
		ArrayList<String> freshList = new ArrayList<String>();

		for (String tag : setOfTagsList) {
			// Combining contents of both sets of tag to make master tag list
			if (!setOfTagsFromAddTagList.contains(tag)) {
				freshList.add(tag);
			}
		}
		freshList.addAll(setOfTagsFromAddTagList);
		return freshList;
	}

	/**
	 * Generates historical names of a filePath from its newHistory.
	 *
	 * @param filePath
	 *            string representation of a file path
	 * @param newHistory
	 *            list containing the updated history
	 * @return list of historical file paths
	 */
	public static ArrayList<String> historicalNames(final String filePath,
			final HashMap<File, ArrayList<String>> newHistory) {
		File f = new File(filePath);
		return newHistory.get(f);
	}
}
