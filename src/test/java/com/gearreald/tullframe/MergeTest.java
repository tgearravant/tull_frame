package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.exceptions.IndexException;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class MergeTest {

	TullFrame base;
	TullFrame merge;
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(ColumnAdderTest.class, "csv/testSheet.csv"));
		File f2 = (ResourceUtils.getResourceFile(ColumnAdderTest.class, "csv/mergeSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		base = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).build();
		merge = new TullFrameFactory().fromCSV(f2).setColumnTypes(columnTypes).build();
	}

	@Test
	public void testMerge() {
		base.setUniqueIndex("id");
		merge.setUniqueIndex("id");
		assertEquals(3, base.countColumns());
		assertEquals(3, merge.countColumns());
		assertEquals(3, base.rowCount());
		assertEquals(3, merge.rowCount());
		TullFrame output = TullFrame.merge(base, merge, "id");
		assertEquals(5, output.countColumns());
		assertEquals(3, output.rowCount());
		List<String> mergedColumns = output.getColumnNames();
		assertEquals("id",mergedColumns.get(0));
		assertEquals(output.getTypeOfColumn("id"), ColumnType.INTEGER);
		assertEquals("first_name",mergedColumns.get(1));
		assertEquals("last_name",mergedColumns.get(2));
		assertEquals("occupation",mergedColumns.get(3));
		assertEquals("last_name_1",mergedColumns.get(4));
		Row r = output.getRow(0);
		assertEquals(1,r.getInt("id").intValue());
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		assertEquals("Miner",r.getString("occupation"));
		assertEquals("Hargi",r.getString("last_name_1"));
		r = output.getRow(1);
		assertEquals(2,r.getInt("id").intValue());
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		assertNull(r.getString("occupation"));
		assertNull(r.getString("last_name_1"));
	}
	
	@Test
	public void testForceMergeNoIndex() {
		assertEquals(3, base.countColumns());
		assertEquals(3, merge.countColumns());
		assertEquals(3, base.rowCount());
		assertEquals(3, merge.rowCount());
		
		try{
			TullFrame.merge(base, merge, "id");
			fail("The merge didn't throw an index exception on an unindexed merge.");
		}catch(IndexException e){}
		TullFrame output = TullFrame.merge(base, merge, "id", true);
		assertEquals(5, output.countColumns());
		assertEquals(3, output.rowCount());
		List<String> mergedColumns = output.getColumnNames();
		assertEquals("id",mergedColumns.get(0));
		assertEquals(output.getTypeOfColumn("id"), ColumnType.INTEGER);
		assertEquals("first_name",mergedColumns.get(1));
		assertEquals("last_name",mergedColumns.get(2));
		assertEquals("occupation",mergedColumns.get(3));
		assertEquals("last_name_1",mergedColumns.get(4));
		Row r = output.getRow(0);
		assertEquals(1,r.getInt("id").intValue());
		assertEquals("Rondi",r.getString("first_name"));
		assertEquals("Hargi",r.getString("last_name"));
		assertEquals("Miner",r.getString("occupation"));
		assertEquals("Hargi",r.getString("last_name_1"));
		r = output.getRow(1);
		assertEquals(2,r.getInt("id").intValue());
		assertEquals("Kroni",r.getString("first_name"));
		assertEquals("Banthua",r.getString("last_name"));
		assertNull(r.getString("occupation"));
		assertNull(r.getString("last_name_1"));
		
	}

}
