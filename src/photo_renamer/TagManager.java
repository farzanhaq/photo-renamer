package photo_renamer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TagManager manages tags and their relationship with files.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class TagManager {

	/** The tag manager. */
	private static TagManager tagManager = new TagManager();
	/** Maps a tag with its associated files. */
	private static HashMap<String, ArrayList<File>> tagMap;
	/** Maps a file with its associated tags. */
	private static Map<File, ArrayList<String>> fileMap;
	/** Contains a comprehensive list of tags. */
	private static ArrayList<String> masterTags;

	/**
	 * Instantiates a new tag manager.
	 */
	private TagManager() {
		TagManager.tagMap = new HashMap<String, ArrayList<File>>();
		TagManager.fileMap = new HashMap<File, ArrayList<String>>();
		TagManager.masterTags = new ArrayList<String>();
	}

	/**
	 * Gets the tag map.
	 *
	 * @return the tagMap
	 */
	public HashMap<String, ArrayList<File>> getTagMap() {
		return tagMap;
	}

	/**
	 * Sets the tagMap.
	 *
	 * @param tagMap
	 *            the tagMap to set
	 */
	public void setTagMap(final HashMap<String, ArrayList<File>> tagMap) {
		TagManager.tagMap = tagMap;
	}

	/**
	 * Gets the file map.
	 *
	 * @return the fileMap
	 */
	public Map<File, ArrayList<String>> getFileMap() {
		return fileMap;
	}

	/**
	 * Sets the fileMap.
	 *
	 * @param fileMap
	 *            the fileMap to set
	 */
	public void setFileMap(final Map<File, ArrayList<String>> fileMap) {
		TagManager.fileMap = fileMap;
	}

	/**
	 * Gets the master tags.
	 *
	 * @return the masterTags
	 */
	public ArrayList<String> getMasterTags() {
		return masterTags;
	}

	/**
	 * Sets the masterTags.
	 *
	 * @param masterTags
	 *            the masterTags to set
	 */
	public void setMasterTags(final ArrayList<String> masterTags) {
		TagManager.masterTags = masterTags;
	}

	/**
	 * Provides a global point of access to the Singleton instance. Interacts
	 * with the Tag, FileHistory and DirectoryExplorer classes. TagManager is
	 * shared throughout PhotoRenamer. By implementing this design pattern, it
	 * is possible to avoid multiple instantiation of this class.
	 *
	 * @return single instance of the class
	 */
	public static TagManager getInstance() {
		return tagManager;
	}
}
