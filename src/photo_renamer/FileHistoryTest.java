package photo_renamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FileHistoryTest tests functionality of methods involving historical files.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class FileHistoryTest {

	/** List to be tested simulating the master tags. */
	private static ArrayList<String> masterTagsTest = new ArrayList<String>();
	/** Map to be tested simulating all of the paths. */
	private static HashMap<File, ArrayList<String>> allPathTest =
			new HashMap<File, ArrayList<String>>();

	/**
	 * Gets the masterTagsTest.
	 *
	 * @return list of masterTagsTest
	 */
	public static ArrayList<String> getMasterTagsTest() {
		return masterTagsTest;
	}

	/**
	 * Sets the masterTagsTest.
	 *
	 * @param masterTagsTest
	 *            masterTagsTest to be set
	 */
	public static void setMasterTagsTest(
			final ArrayList<String> masterTagsTest) {
		FileHistoryTest.masterTagsTest = masterTagsTest;
	}

	/**
	 * Gets the allPathTest.
	 *
	 * @return map of allPathTest
	 */
	public static HashMap<File, ArrayList<String>> getAllPathTest() {
		return allPathTest;
	}

	/**
	 * Sets the allPathTest.
	 *
	 * @param allPathTest
	 *            allPathTest to set
	 */
	public static void setAllPathTest(
			final HashMap<File, ArrayList<String>> allPathTest) {
		FileHistoryTest.allPathTest = allPathTest;
	}

	/**
	 * Sets up master tags test and all path test to be stimulated before class.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		masterTagsTest.add("TestTag1");
		masterTagsTest.add("TestTag2");
		masterTagsTest.add("TestTag3");
		masterTagsTest.add("TestTag4");
		masterTagsTest.add("TestTag5");
		masterTagsTest.add("TestTag6");

		File fTest1 = new File("group_0613/a2/src/a2_photosRenamer/test.jpg");
		allPathTest.put(fTest1, masterTagsTest);
		File fTest2 = new File("group_0613/a2/src/a2_photosRenamer/test2.jpg");
		allPathTest.put(fTest2, masterTagsTest);
	}

	/**
	 * Tears down and delete the temporarily created serialized file after
	 * class.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		String filepathTest = "History_Test.ser";

		File f = new File(filepathTest);
		f.delete();
	}

	/**
	 * Sets up.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@Before
	public final void setUp() throws Exception {
	}

	/**
	 * Tears down and deletes the temporary serialized file after the test is
	 * run.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@After
	public final void tearDown() throws Exception {
		String filepathTest = "History_Test.ser";

		File f = new File(filepathTest);
		f.delete();
	}

	/**
	 * Tests the functionality of deserialization.
	 * {@link a2_photosRenamer.FileHistory#retrieveTagsFromFile( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testRetrieveTagsFromFile() throws FileNotFoundException {
		String filepathTest = "History_Test.ser";

		FileHistory.retrieveTagsFromFile(filepathTest);
		FileHistory.saveTagsToFile(filepathTest, masterTagsTest);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("TestTag1");
				add("TestTag2");
				add("TestTag3");
				add("TestTag4");
				add("TestTag5");
				add("TestTag6");
			}
		};

		assertEquals(listTest, FileHistory.retrieveTagsFromFile(filepathTest));
	}

	/**
	 * Tests the functionality of serialization.
	 * {@link a2_photosRenamer.FileHistory#saveTagsToFile( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testSaveTagsToFile() throws FileNotFoundException {
		String filepathTest = "History_Test.ser";

		FileHistory.retrieveTagsFromFile(filepathTest);
		FileHistory.saveTagsToFile(filepathTest, masterTagsTest);

		assertNotNull(FileHistory.retrieveTagsFromFile(filepathTest));
	}

	/**
	 * Tests the functionality of deserialization of TagManager.tagMap
	 * {@link a2_photosRenamer.FileHistory# retrieveHistoryFromFile( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testRetrieveHistoryFromFile()
			throws FileNotFoundException {
		String filepathTest = "History_Test.ser";

		FileHistory.retrieveHistoryFromFile(filepathTest);
		FileHistory.saveHistoryToFile(filepathTest, allPathTest);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("TestTag1");
				add("TestTag2");
				add("TestTag3");
				add("TestTag4");
				add("TestTag5");
				add("TestTag6");
			}
		};

		HashMap<File, ArrayList<String>> hashMapTest =
				new HashMap<File, ArrayList<String>>();

		File fTest3 = new File("group_0613/a2/src/a2_photosRenamer/test.jpg");
		hashMapTest.put(fTest3, listTest);
		File fTest4 = new File("group_0613/a2/src/a2_photosRenamer/test2.jpg");
		hashMapTest.put(fTest4, listTest);

		assertEquals(
				hashMapTest, FileHistory.retrieveHistoryFromFile(filepathTest));
	}

	/**
	 * Tests functionality of serialization of TagManager.tagMap
	 * {@link a2_photosRenamer.FileHistory#saveHistoryToFile( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testSaveHistoryToFile() throws FileNotFoundException {
		String filepathTest = "History_Test.ser";

		FileHistory.retrieveHistoryFromFile(filepathTest);
		FileHistory.saveHistoryToFile(filepathTest, allPathTest);

		assertNotNull(FileHistory.retrieveHistoryFromFile(filepathTest));
	}
}
