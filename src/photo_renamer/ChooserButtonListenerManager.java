package photo_renamer;

import java.io.File;

/**
 * ChooserButtonListenerManager for DirectoryChooserButtonListener and
 * FileChooserButtonListener.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public final class ChooserButtonListenerManager {

	/**
	 * Prevents instantiation of utility class.
	 */
	private ChooserButtonListenerManager() {
	}

	/**
	 * Builds the tree of nodes rooted at file in the file system; note curr is
	 * the FileNode corresponding to file, so this only adds nodes for children
	 * of file to the tree. Precondition: file represents a directory.
	 *
	 * @param file
	 *            the file or directory we are building
	 * @param curr
	 *            the node representing file
	 */
	static void buildTree(final File file, final FileNode curr) {
		File[] allFiles = file.listFiles();

		if (file.isDirectory()) {
			for (int i = 0; i < allFiles.length; i++) {
				File childFile = allFiles[i];

				// Traverse files or directories recursively by building nodes
				if (childFile.isDirectory()) {
					FileNode childNode = new FileNode(childFile.getName(),
							curr, FileType.DIRECTORY);
					curr.addChild(childFile.getName(), childNode);
					buildTree(childFile, childNode);
				} else {
					// Checks for image type if there are no sub-directories
					String[] ext = { "jpg", "jpeg", "png", "tif", "gif", "bmp",
							"wbmp" };

					for (int j = 0; j < ext.length; j++) {
					  if (childFile.getName().toLowerCase().endsWith(ext[j])) {
							FileNode childNode = new
									FileNode(childFile.getName(), curr,
											FileType.FILE);
							curr.addChild(childFile.getName(), childNode);
						}
					}
				}
			}
		}
	}

	/**
	 * Builds a string buffer representation of the contents of the tree rooted
	 * at n, prepending each file name with prefix, and adding and additional
	 * DirectoryExplorer.PREFIX for subdirectory contents.
	 *
	 * @param fileNode
	 *            the root of the subtree
	 * @param contents
	 *            the string to display
	 * @param prefix
	 *            the prefix to prepend
	 */
	static void buildDirectoryContents(
			final FileNode fileNode, final StringBuffer contents,
			String prefix) {
		contents.append(prefix);
		contents.append(fileNode.getName() + "\n");

		// Builds the prefix
		prefix += ExplorerManager.PREFIX;

		// Iterates through the nodes and recursively builds the contents
		for (FileNode n : fileNode.getChildren()) {
			buildDirectoryContents(n, contents, prefix);
		}
	}
}
