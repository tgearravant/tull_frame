package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.exceptions.IndexException;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class IndexTest {

	private TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(IndexTest.class, "csv/testSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		frame = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).build();
	}

	@Test
	public void testAddUniqueIndex() {
		frame.setUniqueIndex("id");
		Row r = frame.lookupRowByUniqueKey(1, "id");

		assertEquals("1",r.getString("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));

		r = frame.lookupRowByUniqueKey(3, "id");
		assertEquals("3",r.getString("id"));
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		
		Set<Row> rows = frame.lookupRows(1, "id");
		assertEquals(1, rows.size());
		r = rows.iterator().next();

		assertEquals("1",r.getString("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		
	}
	
	@Test
	public void testLookupIndex() {
		frame.setLookupIndex("id");
		Set<Row> rows = frame.lookupRows(1, "id");
		assertEquals(1, rows.size());
		Row r = rows.iterator().next();

		assertEquals("1",r.getString("id"));
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
	}
	
	@Test
	public void testRawLookups() {
		Set<Row> rows;
		try{
			rows = frame.lookupRows(3, "id");
			fail("An unindexed lookup without force should fail.");
		}catch(IndexException e){}
		rows = frame.lookupRows(3, "id", true);
		assertEquals(1, rows.size());
		Row r = rows.iterator().next();
		assertEquals("3",r.getString("id"));
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));

		try{
			r = frame.lookupRowByUniqueKey(3, "id");
			fail("An unindexed lookup without force should fail.");
		}catch(IndexException e){}
		r = frame.lookupRowByUniqueKey(3, "id", true);
		assertEquals("3",r.getString("id"));
		assertEquals("Kenaii",r.getString("first_name"));
		assertEquals("Kruda",r.getString("last_name"));
		
	}
}
