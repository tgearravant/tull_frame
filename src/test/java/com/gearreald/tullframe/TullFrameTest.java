package com.gearreald.tullframe;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TullFrameTest {

	TullFrame frame;
	
	@Before
	public void setUp() throws Exception {
		frame = new TullFrameFactory().build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
