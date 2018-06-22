package com.gearreald.tullframe.serializers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.serializers.FSTMapSerializer;

import com.gearreald.tullframe.TullFrame;
import com.gearreald.tullframe.columns.BooleanColumn;
import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.columns.DateColumn;
import com.gearreald.tullframe.columns.DoubleColumn;
import com.gearreald.tullframe.columns.IntegerColumn;
import com.gearreald.tullframe.columns.LongColumn;
import com.gearreald.tullframe.columns.LookupIndex;
import com.gearreald.tullframe.columns.StringColumn;
import com.gearreald.tullframe.columns.TimeColumn;
import com.gearreald.tullframe.columns.UniqueIndex;
import com.gearreald.tullframe.utils.ColumnType;

public class SerializerConfiguration {
	private static final FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();
	static {
		configuration.registerClass(HashMap.class
				,Integer.class
				,Boolean.class
				,LocalDate.class
				,LocalDateTime.class
				,Double.class
				,String.class
				,Long.class
				,BooleanColumn.class
				,DateColumn.class
				,DoubleColumn.class
				,IntegerColumn.class
				,LongColumn.class
				,StringColumn.class
				,TimeColumn.class
				,LookupIndex.class
				,UniqueIndex.class
				,ArrayList.class
				,HashSet.class
				,TullFrame.class
				,Column.class
				,ColumnType.class
				);
		configuration.registerSerializer(HashMap.class, new FSTMapSerializer(), false);
	}
	public static FSTConfiguration getInstance() {
        return configuration;
    }
}
