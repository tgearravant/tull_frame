package com.gearreald.tullframe;

import java.io.File;
import java.sql.ResultSet;

public class TullFrameFactory {
	
	File csvFile;
	ResultSet sqlResult;

	public TullFrameFactory(){
		
	}
	public TullFrame build(){
		return new TullFrame();
	}
}
