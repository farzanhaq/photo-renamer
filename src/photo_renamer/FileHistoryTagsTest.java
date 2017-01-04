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
 * FileHistoryTagsTest tests functionality of methods involving historical tags.
 *
 * @author Farzan Haq
 * @author Pratiman Shahi
 * @version %I%, %G%
 */
public class FileHistoryTagsTest {

	/** List to be tested simulating the tag list. */
	private static ArrayList<String> tagsList = new ArrayList<String>();
	/** Map to be tested simulating the history list. */
	private static HashMap<File, ArrayList<String>> historyMap =
			new HashMap<File, ArrayList<String>>();
	/** List to be tested simulating the history list. */
	private static ArrayList<String> historyList = new ArrayList<String>();
	/** Map to be tested simulating the tag map. */
	private static HashMap<String, ArrayList<File>> tagsMap =
			new HashMap<String, ArrayList<File>>();

	/**
	 * Sets up tag and file history list to be simulated before class.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tagsList.add("TestTag1");
		tagsList.add("TestTag2");
		tagsList.add("TestTag3");
		tagsList.add("TestTag4");
		tagsList.add("TestTag5");
		tagsList.add("TestTag5");

		historyList.add("group_0613/a3/src/photo_renamer/test.jpg");
		historyList.add("group_0613/a3/src/photo_renamer/test@2.jpg");
		historyList.add("group_0613/a3/src/photo_renamer/test@3.jpg");
		historyList.add("group_0613/a3/src/photo_renamer/test@4.jpg");

		File fTest1 = new File("group_0613/a3/src/photo_renamer/test.jpg");
		historyMap.put(fTest1, historyList);

		File fTest2 = new File("group_0613/a3/src/photo_renamer/test@2.jpg");
		@SuppressWarnings("serial")
		ArrayList<File> fList2 = new ArrayList<File>() {
			{
				add(fTest2);
			}
		};

		File fTest3 = new File("group_0613/a3/src/photo_renamer/test@3.jpg");
		@SuppressWarnings("serial")
		ArrayList<File> fList3 = new ArrayList<File>() {
			{
				add(fTest3);
			}
		};

		File fTest4 = new File("group_0613/a3/src/photo_renamer/test@4.jpg");
		@SuppressWarnings("serial")
		ArrayList<File> fList4 = new ArrayList<File>() {
			{
				add(fTest4);
			}
		};

		tagsMap.put("2", fList2);
		tagsMap.put("3", fList3);
		tagsMap.put("4", fList4);
	}

	/**
	 * Tears down after class & delete the temporarily created serialized file.
	 *
	 * @throws Exception
	 *             throws an exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		String fTest1 = "History_Test.ser";
		File f1 = new File(fTest1);
		f1.delete();

		String fTest2 = "History_Test2.ser";
		File f2 = new File(fTest2);
		f2.delete();
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
	 * Tears down after each test run and delete the temporary serialized file.
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
	 * Tests whether setOfTags returns the desired output
	 * {@link a2_photosRenamer.FileHistoryTags#setOfTags( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testSetOfTags() throws FileNotFoundException {
		String filepathTest = "History_Test.ser";

		FileHistory.retrieveTagsFromFile(filepathTest);
		FileHistory.saveTagsToFile(filepathTest, tagsList);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 6447146404395707449L;
			{
				add("TestTag1");
				add("TestTag2");
				add("TestTag3");
				add("TestTag4");
				add("TestTag5");
			}
		};

		assertEquals(listTest, FileHistoryTags.setOfTags(tagsList));
		assertNotNull(FileHistoryTags.setOfTags(tagsList));
	}

	/**
	 * Tests whether setOfTagsFromAddTags returns the desired output
	 * {@link a2_photosRenamer.FileHistoryTags#setOfTags( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testSetOfTagsFromAddTags() throws FileNotFoundException {
		String filePathTest = "History_Test.ser";

		FileHistory.retrieveFromFile(filePathTest);
		FileHistory.saveToFile(filePathTest, tagsMap);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("2");
				add("3");
				add("4");
			}
		};

		assertEquals(listTest, FileHistoryTags.setOfTagsFromAddTag(tagsMap));
		assertNotNull(FileHistoryTags.setOfTagsFromAddTag(tagsMap));
	}

	/**
	 * Tests whether setOfMasterTag returns the desired output
	 * {@link a2_photosRenamer.FileHistoryTags#setOfTags( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testSetOfMasterTag() throws FileNotFoundException {
		String fTest1 = "History_Test.ser";
		String fTest2 = "History_Test2.ser";

		FileHistory.retrieveFromFile(fTest2);
		FileHistory.retrieveTagsFromFile(fTest1);
		FileHistory.saveToFile(fTest2, tagsMap);
		FileHistory.saveTagsToFile(fTest1, tagsList);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("TestTag1");
				add("TestTag2");
				add("TestTag3");
				add("TestTag4");
				add("TestTag5");
				add("2");
				add("3");
				add("4");
			}
		};

		assertEquals(
				listTest, FileHistoryTags.setOfMasterTag(tagsList, tagsMap));
		assertNotNull(FileHistoryTags.setOfMasterTag(tagsList, tagsMap));
	}

	/**
	 * Tests whether histroricalNames returns the desired output
	 * {@link a2_photosRenamer.FileHistoryTags#historicalNames( java.lang.String)}.
	 *
	 * @throws FileNotFoundException
	 *             throws a file not found exception
	 */
	@Test
	public final void testHistoricalNames() throws FileNotFoundException {
		String filepathTest = "group_0613/a3/src/photo_renamer/test.jpg";

		FileHistory.retrieveHistoryFromFile("History_Test.ser");
		FileHistory.saveHistoryToFile("History_Test.ser", historyMap);

		ArrayList<String> listTest = new ArrayList<String>() {
			private static final long serialVersionUID = 2770112645536014437L;
			{
				add("group_0613/a3/src/photo_renamer/test.jpg");
				add("group_0613/a3/src/photo_renamer/test@2.jpg");
				add("group_0613/a3/src/photo_renamer/test@3.jpg");
				add("group_0613/a3/src/photo_renamer/test@4.jpg");
			}
		};

		assertEquals(
				listTest,
				FileHistoryTags.historicalNames(filepathTest, historyMap));
		assertNotNull(
				FileHistoryTags.historicalNames(filepathTest, historyMap));
	}
}
