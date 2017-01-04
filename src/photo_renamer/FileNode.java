package photo_renamer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * FileNode acts as the root of tree representing a directory structure.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class FileNode {
	/** The name of the file or directory this node represents. */
	private String name;
	/** Whether this node represents a file or a directory. */
	private FileType type;
	/** This node's parent. */
	private FileNode parent;
	/**
	 * This node's children, mapped from the file names to the nodes. If type is
	 * FileType.FILE, this is null.
	 */
	private Map<String, FileNode> children;

	/**
	 * A node in this tree.
	 *
	 * @param name
	 *            the file
	 * @param parent
	 *            the parent node
	 * @param type
	 *            file or directory
	 * @see buildFileTree
	 */
	public FileNode(
			final String name, final FileNode parent, final FileType type) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.children = new HashMap<String, FileNode>();
	}

	/**
	 * Find and return a child node named name in this directory tree, or null
	 * if there is no such child node.
	 *
	 * @param name
	 *            the file name to search for
	 * @return the node named name
	 */
	public final FileNode findChild(final String name) {
		FileNode result = null;

		// In the event of file node having no children
		if (children == null) {
			return result;
		}

		// In the event of FileNode having children
		FileNode nextChild = children.get(name);
		if (nextChild != null) {
			return children.get(name);
		} else {
			// Iterate through all the children to find the child
			for (FileNode f : getChildren()) {
				if (result != null) {
					// Skips if no children are found
					break;
				}
				// Stores the name of desired child if found
				result = f.children.get(name);
			}
			return result;
		}
	}

	/**
	 * Return the name of the file or directory represented by this node.
	 *
	 * @return name of this Node
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Set the name of the current node.
	 *
	 * @param name
	 *            of the file/directory
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Return the child nodes of this node.
	 *
	 * @return the child nodes directly underneath this node.
	 */
	public final Collection<FileNode> getChildren() {
		return this.children.values();
	}

	/**
	 * Return this node's parent.
	 *
	 * @return the parent
	 */
	public final FileNode getParent() {
		return parent;
	}

	/**
	 * Set this node's parent to p.
	 *
	 * @param p
	 *            the parent to set
	 */
	public final void setParent(final FileNode p) {
		this.parent = p;
	}

	/**
	 * Add childNode, representing a file or directory named name, as a child of
	 * this node.
	 *
	 * @param name
	 *            the name of the file or directory
	 * @param childNode
	 *            the node to add as a child
	 */
	public final void addChild(final String name, final FileNode childNode) {
		this.children.put(name, childNode);
	}

	/**
	 * Returns whether this node represents a directory.
	 *
	 * @return whether this node represents a directory.
	 */
	public final boolean isDirectory() {
		return this.type == FileType.DIRECTORY;
	}
}
