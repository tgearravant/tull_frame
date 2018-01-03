package com.gearreald.tullframe;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.gearreald.tullframe.interfaces.column_adders.BooleanColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.DateColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.DoubleColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.IntColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.LongColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.StringColumnAdder;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.ResourceUtils;

public class ColumnAdderTest {

	private TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		File f = (ResourceUtils.getResourceFile(ColumnAdderTest.class, "csv/testSheet.csv"));
		ColumnType[] columnTypes = {ColumnType.INTEGER, ColumnType.STRING, ColumnType.STRING};
		frame = new TullFrameFactory().fromCSV(f).setColumnTypes(columnTypes).build();
	}

	@Test
	public void testAddBooleanColumn() {
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		BooleanColumnAdder boolAdder = (Row r) -> {
			if(r.getString("first_name").toLowerCase().equals("rondi"))
				return true;
			return false;
		};
		frame.addNewBooleanColumn("named_rondi", boolAdder);
		assertEquals(frame.countColumns(), 4);
		assertTrue(frame.getRow(0).getBoolean("named_rondi"));
		assertFalse(frame.getRow(1).getBoolean("named_rondi"));
	}
	@Test
	public void testAddLongColumn(){
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		LongColumnAdder longAdder = (Row r) -> {
			return r.getInt("id") * r.getInt("id");
		};
		frame.addNewLongColumn("id_squared", longAdder);
		assertEquals(frame.countColumns(), 4);
		assertEquals(frame.getRow(0).getLong("id_squared"), 1);
		assertEquals(frame.getRow(1).getLong("id_squared"), 4);
		assertEquals(frame.getRow(2).getLong("id_squared"), 9);
	}
	@Test
	public void testAddIntegerColumn(){
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		IntColumnAdder intAdder = (Row r) -> {
			return r.getInt("id") * r.getInt("id");
		};
		frame.addNewIntegerColumn("id_squared", intAdder);
		assertEquals(frame.countColumns(), 4);
		assertEquals(frame.getRow(0).getInt("id_squared"), 1);
		assertEquals(frame.getRow(1).getInt("id_squared"), 4);
		assertEquals(frame.getRow(2).getInt("id_squared"), 9);
	}
	@Test
	public void testAddDateColumn(){
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		DateColumnAdder dateAdder = (Row r) -> {
			if(r.getInt("id") == 1)
				return LocalDate.parse("2017-01-10");
			if(r.getInt("id") == 2)
				return LocalDate.parse("2017-01-11");
			return null;
		};
		frame.addNewDateColumn("dates", dateAdder);
		assertEquals(frame.countColumns(), 4);
		assertEquals(frame.getRow(0).getDate("dates"), LocalDate.parse("2017-01-10"));
		assertEquals(frame.getRow(1).getDate("dates"), LocalDate.parse("2017-01-11"));
		assertNull(frame.getRow(2).getDate("dates"));
	}
	@Test
	public void testAddDoubleColumn(){
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		DoubleColumnAdder doubleAdder = (Row r) -> {
			return r.getInt("id") / 2.0d;
		};
		frame.addNewDoubleColumn("half_id", doubleAdder);
		assertEquals(frame.countColumns(), 4);
		assertEquals(frame.getRow(0).getDouble("half_id"), 0.5d, 0.0001d);
		assertEquals(frame.getRow(1).getDouble("half_id"), 1, 0.0001d);
	}
	@Test
	public void testAddStringColumn(){
		assertEquals(frame.size(), 3);
		assertEquals(frame.countColumns(), 3);
		StringColumnAdder doubleAdder = (Row r) -> {
			return String.format("%s %s",r.getString("first_name"), r.getString("last_name"));
		};
		frame.addNewStringColumn("full_name", doubleAdder);
		assertEquals(frame.countColumns(), 4);
		assertEquals(frame.getRow(0).getString("full_name"), "Rondi Hargi");
		assertEquals(frame.getRow(1).getString("full_name"), "Kroni Banthua");
	}
}
