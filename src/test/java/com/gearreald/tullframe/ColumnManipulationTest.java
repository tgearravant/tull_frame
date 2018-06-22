package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class ColumnManipulationTest {

	TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(ColumnAdderTest.class, "csv/testSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		frame = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).build();
	}

	@Test
	public void testRemoveColumnsListOfString() {
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertTrue(frame.hasColumn("last_name"));
		List<String> columnsToRemove = new ArrayList<String>();
		columnsToRemove.add("first_name");
		columnsToRemove.add("last_name");
		List<Column> removedColumns= frame.removeColumns(columnsToRemove);
		assertEquals(1, frame.countColumns());
		assertTrue(frame.hasColumn("id"));
		assertFalse(frame.hasColumn("first_name"));
		assertFalse(frame.hasColumn("last_name"));
		assertEquals(2, removedColumns.size());
		boolean hasFN = false;
		boolean hasLN = false;
		for(Column c: removedColumns){
			assertEquals(c.getColumnType(), ColumnType.STRING);
			for(Object values: c){
				if(values.equals("Rondi"))
					hasFN = true;
				if(values.equals("Hargi"))
					hasLN = true;
			}
		}
		assertTrue(hasLN);
		assertTrue(hasFN);
	}

	@Test
	public void testRemoveColumnsStringArray() {
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertTrue(frame.hasColumn("last_name"));
		List<Column> removedColumns= frame.removeColumns("first_name", "last_name");
		assertEquals(1, frame.countColumns());
		assertTrue(frame.hasColumn("id"));
		assertFalse(frame.hasColumn("first_name"));
		assertFalse(frame.hasColumn("last_name"));
		assertEquals(2, removedColumns.size());
		boolean hasFN = false;
		boolean hasLN = false;
		for(Column c: removedColumns){
			assertEquals(c.getColumnType(), ColumnType.STRING);
			for(Object values: c){
				if(values.equals("Rondi"))
					hasFN = true;
				if(values.equals("Hargi"))
					hasLN = true;
			}
		}
		assertTrue(hasLN);
		assertTrue(hasFN);
	}

	@Test
	public void testSubsetToColumnsListOfString() {
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertTrue(frame.hasColumn("last_name"));
		List<String> columnsToKeep = new ArrayList<String>();
		columnsToKeep.add("id");
		List<Column> removedColumns= frame.subsetToColumns(columnsToKeep);
		assertEquals(1, frame.countColumns());
		assertTrue(frame.hasColumn("id"));
		assertFalse(frame.hasColumn("first_name"));
		assertFalse(frame.hasColumn("last_name"));
		assertEquals(2, removedColumns.size());
		boolean hasFN = false;
		boolean hasLN = false;
		for(Column c: removedColumns){
			assertEquals(c.getColumnType(), ColumnType.STRING);
			for(Object values: c){
				if(values.equals("Rondi"))
					hasFN = true;
				if(values.equals("Hargi"))
					hasLN = true;
			}
		}
		assertTrue(hasLN);
		assertTrue(hasFN);
	}

	@Test
	public void testSubsetToColumnsStringArray() {
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertTrue(frame.hasColumn("last_name"));
		List<Column> removedColumns= frame.subsetToColumns("id");
		assertEquals(1, frame.countColumns());
		assertTrue(frame.hasColumn("id"));
		assertFalse(frame.hasColumn("first_name"));
		assertFalse(frame.hasColumn("last_name"));
		assertEquals(2, removedColumns.size());
		boolean hasFN = false;
		boolean hasLN = false;
		for(Column c: removedColumns){
			assertEquals(c.getColumnType(), ColumnType.STRING);
			for(Object values: c){
				if(values.equals("Rondi"))
					hasFN = true;
				if(values.equals("Hargi"))
					hasLN = true;
			}
		}
		assertTrue(hasLN);
		assertTrue(hasFN);
	}

	@Test
	public void testRemoveColumn() {
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertTrue(frame.hasColumn("last_name"));
		Column c = frame.removeColumn("last_name");
		assertTrue(frame.hasColumn("id"));
		assertTrue(frame.hasColumn("first_name"));
		assertFalse(frame.hasColumn("last_name"));
		assertEquals(2, frame.countColumns());
		boolean hasLN = false;
		assertEquals(c.getColumnType(), ColumnType.STRING);
		for(Object values: c){
			if(values.equals("Hargi"))
				hasLN = true;
		}
		assertTrue(hasLN);
	}

}
