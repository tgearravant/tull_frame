package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.interfaces.Filter;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class RowManipulationTest {

	TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(ColumnAdderTest.class, "csv/testSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		frame = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).build();
	}

	@Test
	public void rowAddTest() {
		assertEquals(3, frame.rowCount());
		String[] row = {"4","Luke","Skywalker"};
		frame.addRow(row);
		assertEquals(4, frame.rowCount());
		Row r = frame.getRow(3);
		assertEquals(4, r.getInt("id").intValue());
		assertEquals("Luke", r.getString("first_name"));
		assertEquals("Skywalker", r.getString("last_name"));
		assertEquals(3, r.getIndex());
		assertEquals(3, r.getRowNumber());
	}
	@Test
	public void rowRemoveTest() {
		assertEquals(3, frame.rowCount());
		Row r = frame.getRow(2);
		assertEquals(3, r.getInt("id").intValue());
		assertEquals("Kenaii", r.getString("first_name"));
		assertEquals("Kruda", r.getString("last_name"));
		assertEquals(2, r.getIndex());
		assertEquals(2, r.getRowNumber());
		frame.removeRow(frame.getRow(1));
		assertEquals(2, frame.rowCount());
		assertEquals(3, r.getInt("id").intValue());
		assertEquals("Kenaii", r.getString("first_name"));
		assertEquals("Kruda", r.getString("last_name"));
		assertEquals(2, r.getIndex());
		assertEquals(1, r.getRowNumber());
		r = frame.getRow(1);
		assertEquals(3, r.getInt("id").intValue());
		assertEquals("Kenaii", r.getString("first_name"));
		assertEquals("Kruda", r.getString("last_name"));
		assertEquals(2, r.getIndex());
		assertEquals(1, r.getRowNumber());
		frame.removeRow(0);
		assertEquals(1, frame.rowCount());
		assertEquals(3, r.getInt("id").intValue());
		assertEquals("Kenaii", r.getString("first_name"));
		assertEquals("Kruda", r.getString("last_name"));
		assertEquals(2, r.getIndex());
		assertEquals(0, r.getRowNumber());	
	}
	@Test
	public void filterTest(){
		TullFrame filterFrame = new TullFrameFactory().fromTullFrame(frame).build(); 
		assertEquals(3, filterFrame.rowCount());
		Filter f = (Row r) -> {return true;};
		filterFrame.filterRows(f);
		assertEquals(3, filterFrame.rowCount());
		Filter f2 = (Row r) -> {return (r.getInt("id")!=1?true:false);};
		filterFrame = new TullFrameFactory().fromTullFrame(frame).build();
		filterFrame.filterRows(f2);
		assertEquals(2, filterFrame.rowCount());
		Filter f3 = (Row r) -> {return (r.getInt("id")!=2?true:false);};
		filterFrame = new TullFrameFactory().fromTullFrame(frame).build();
		filterFrame.filterRows(f3);
		assertEquals(2, filterFrame.rowCount());
		filterFrame = new TullFrameFactory().fromTullFrame(frame).build();
		filterFrame.filterRows(f2, f3);
		assertEquals(1, filterFrame.rowCount());
		
	}

}
