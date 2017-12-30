package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.exceptions.TullFrameException;

import net.tullco.tullutils.ResourceUtils;

public class TullFrameTest {

	TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(TullFrameTest.class, "csv/testSheet.csv"));
		frame = new TullFrameFactory().fromCSV(f).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCSVLoad() {
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
	}
	
	@Test
	public void testSQLLoad() throws SQLException, ClassNotFoundException{
		String jdbcURL = "jdbc:sqlite:" + ResourceUtils.getAbsoluteResourcePath(TullFrameTest.class, "db/test.db");
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(jdbcURL);
		frame = new TullFrameFactory().fromSQL(c, "SELECT * FROM people").build();
		assertEquals("id", frame.getColumnNames().get(0));
		assertEquals("first_name", frame.getColumnNames().get(1));
		assertEquals("last_name", frame.getColumnNames().get(2));
		Row r = frame.getRow(0);
		assertEquals(1,r.getInt("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		r = frame.getRow(1);
		assertEquals(2,r.getInt("id"));
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		r = frame.getRow(2);
		assertEquals(3,r.getInt("id"));
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		try{
			r = frame.getRow(3);
			fail();
		}catch(TullFrameException e){}
	}
	public static File getResource(String path){
		return new File(TullFrameTest.class.getClassLoader().getResource(path).getFile());
	}
}
