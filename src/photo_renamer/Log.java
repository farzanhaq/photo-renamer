package photo_renamer;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Log is the class which keeps a log of file name changes.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class Log extends Formatter {

	// Use various facilities of Logger Class without hard-coding class name
	/** Gets to Logger object. */
	public static final Logger LOGGER =
			Logger.getLogger(Thread.currentThread().getStackTrace()[0]
					.getClassName());

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public final String format(final LogRecord arg0) {
		return null;
	}

	/**
	 * Logs the old and new name of every file change into a config file.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void systemLogger() throws IOException {
		try {
			// Add the config file to the handler for logging
			FileHandler handler = new FileHandler("system_log.xml", true);
			Log.LOGGER.addHandler(handler);
			// Override global logging level
			Log.LOGGER.setLevel(Level.INFO);

			for (File tag : FileHistory.getNewHistory().keySet()) {
				for (String path : FileHistory.getNewHistory().get(tag)) {
					Log.LOGGER.info("-[OLD NAME]-" + tag + " -[NEW NAME]-" + path);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
