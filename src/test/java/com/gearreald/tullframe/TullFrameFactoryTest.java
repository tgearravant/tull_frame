package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.exceptions.ColumnNameException;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class TullFrameFactoryTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCSVLoad() {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		TullFrame frame = new TullFrameFactory().fromCSV(f).build();
		assertEquals(3, frame.size());
		assertEquals("id", frame.getColumnNames().get(0));
		assertEquals("first_name", frame.getColumnNames().get(1));
		assertEquals("last_name", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals("1",r.getString("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		r = frame.getRow(1);
		assertEquals("2",r.getString("id"));
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		r = frame.getRow(2);
		assertEquals("3",r.getString("id"));
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		try{
			r.getInt("id");
			fail("Didn't throw a type exception trying to get an integer from a CSV column!");
		}catch(ColumnTypeMismatchException e){}
	}
	
	@Test
	public void testCSVLoadWithHeadersAndTypes() {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		String[] headers = {"person_id", "person_first","person_last"};
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		TullFrame frame = new TullFrameFactory().fromCSV(f).setColumnHeaders(headers).setColumnTypes(columnTypes).build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("person_id").intValue());
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("person_id").intValue());
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("person_id").intValue());
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			assertEquals(3,r.getInt("id").intValue());
			fail("Didn't throw an exception on the default column name");
		}catch(ColumnNameException e){}
	}
	
	@Test
	public void testCSVLoadWithHeadersAndTypeMap() {
		HashMap<String,ColumnType> headerMap = new HashMap<String, ColumnType>();
		headerMap.put("person_id", ColumnType.INTEGER);
		headerMap.put("person_first", ColumnType.STRING);
		headerMap.put("a;sldkfja;lskdjfa", ColumnType.STRING);
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		String[] headers = {"person_id", "person_first","person_last"};
		TullFrame frame = new TullFrameFactory().fromCSV(f).setColumnHeaders(headers).setColumnTypes(headerMap).build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("person_id").intValue());
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("person_id").intValue());
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("person_id").intValue());
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			assertEquals(3,r.getInt("id").intValue());
			fail("Didn't throw an exception on the default column name");
		}catch(ColumnNameException e){}
	}
	
	@Test
	public void testCSVLoadWithHeadersAndInferredTypes() {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		String[] headers = {"person_id", "person_first","person_last"};
		TullFrame frame = new TullFrameFactory().fromCSV(f).setColumnHeaders(headers).inferringTypes().build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("person_id").intValue());
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("person_id").intValue());
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("person_id").intValue());
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			assertEquals(3,r.getInt("id").intValue());
			fail("Didn't throw an exception on the default column name");
		}catch(ColumnNameException e){}
	}
	
	@Test
	public void testSerialize() throws IOException {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		String[] headers = {"person_id", "person_first","person_last"};
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		TullFrame originalFrame = new TullFrameFactory().fromCSV(f).setColumnHeaders(headers).setColumnTypes(columnTypes).build();
		File serialized = File.createTempFile("tull_frame_test",".jobject");
		originalFrame.serializeToFile(serialized);
		TullFrame frame = new TullFrameFactory().fromSerializedFile(serialized).build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("person_id").intValue());
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("person_id").intValue());
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("person_id").intValue());
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			assertEquals(3,r.getInt("id").intValue());
			fail("Didn't throw an exception on the default column name");
		}catch(ColumnNameException e){}
		
	}
	
	@Test
	public void testCopy() {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		String[] headers = {"person_id", "person_first","person_last"};
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		TullFrame originalFrame = new TullFrameFactory().fromCSV(f).setColumnHeaders(headers).setColumnTypes(columnTypes).build();
		TullFrame frame = new TullFrameFactory().fromTullFrame(originalFrame).build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("person_id").intValue());
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("person_id").intValue());
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("person_id").intValue());
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			assertEquals(3,r.getInt("id").intValue());
			fail("Didn't throw an exception on the default column name");
		}catch(ColumnNameException e){}
	}
	
	@Test
	public void testSQLLoad() throws SQLException, ClassNotFoundException{
		String jdbcURL = "jdbc:sqlite:" + ResourceUtils.getAbsoluteResourcePath(TullFrameFactoryTest.class, "db/test.db");
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(jdbcURL);
		TullFrame frame = new TullFrameFactory().fromSQL(c, "SELECT * FROM people").build();
		assertEquals("id", frame.getColumnNames().get(0));
		assertEquals("first_name", frame.getColumnNames().get(1));
		assertEquals("last_name", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("id").intValue());
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("id").intValue());
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("id").intValue());
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		try{
			r = frame.getRow(3);
			fail("Getting a non-existant row didn't throw an exception!");
		}catch(IndexOutOfBoundsException e){}
	}
	@Test
	public void testSQLLoadWithHeadersAndTypes() throws SQLException, ClassNotFoundException {
		String[] headers = {"person_id", "person_first","person_last"};
		String jdbcURL = "jdbc:sqlite:" + ResourceUtils.getAbsoluteResourcePath(TullFrameFactoryTest.class, "db/test.db");
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(jdbcURL);
		ColumnType[] columnTypes = {ColumnType.STRING, ColumnType.STRING, ColumnType.STRING};
		String sqlQuery = "SELECT * FROM people";
		TullFrame frame = new TullFrameFactory().fromSQL(c, sqlQuery).setColumnHeaders(headers).setColumnTypes(columnTypes).build();
		assertEquals(3, frame.size());
		assertEquals("person_id", frame.getColumnNames().get(0));
		assertEquals("person_first", frame.getColumnNames().get(1));
		assertEquals("person_last", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals("1",r.getString("person_id"));
		assertEquals("Rondi",r.getString("person_first"));
		assertEquals("Hargi",r.getString("person_last"));
		r = frame.getRow(1);
		assertEquals("2",r.getString("person_id"));
		assertEquals("Kroni",r.getString("person_first"));
		assertEquals("Banthua",r.getString("person_last"));
		r = frame.getRow(2);
		assertEquals("3",r.getString("person_id"));
		assertEquals("Kenaii",r.getString("person_first"));
		assertEquals("Kruda",r.getString("person_last"));
		try{
			frame.getRow(3);
			fail("Getting a non-existant row didn't throw an exception!");
		}catch(IndexOutOfBoundsException e){}
		try{
			frame.getRow(2).getInt("id");
			fail("The wrong data type didn't throw an exception!");
		}catch(ColumnNameException e){}
	}
	@Test
	public void testLoadWithIndices() {
		File f = (ResourceUtils.getResourceFile(TullFrameFactoryTest.class, "csv/testSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		TullFrame originalFrame = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).setUniqueIndex("id").build();
		Row r = originalFrame.lookupRowByUniqueKey(new Integer("1"), "id");
		assertEquals(new Integer(1),r.getInt("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
	}
	@Test
	public void testLoadWithCache() throws ClassNotFoundException, SQLException, IOException{
		String jdbcURL = "jdbc:sqlite:" + ResourceUtils.getAbsoluteResourcePath(TullFrameFactoryTest.class, "db/test.db");
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(jdbcURL);
		File cacheDirectory = Files.createTempDirectory("tull_frame_test").toFile();
		TullFrame frame = new TullFrameFactory().fromSQL(c, "SELECT * FROM people").cachingDataAt(cacheDirectory, "test").build();
		assertEquals("id", frame.getColumnNames().get(0));
		assertEquals("first_name", frame.getColumnNames().get(1));
		assertEquals("last_name", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("id").intValue());
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("id").intValue());
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("id").intValue());
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		
		frame = new TullFrameFactory().fromSQL(c, "blah garbage").cachingDataAt(cacheDirectory, "test").build();
		assertEquals("id", frame.getColumnNames().get(0));
		assertEquals("first_name", frame.getColumnNames().get(1));
		assertEquals("last_name", frame.getColumnNames().get(2));
		r = frame.getRow(0);
		assertEquals(1,r.getInt("id").intValue());
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("id").intValue());
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("id").intValue());
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		
		try{
			r = frame.getRow(3);
			fail("Getting a non-existant row didn't throw an exception!");
		}catch(IndexOutOfBoundsException e){}
	}
}
