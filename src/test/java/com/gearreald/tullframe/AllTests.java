package com.gearreald.tullframe;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TullFrameFactoryTest.class, ColumnAdderTest.class, RowManipulationTest.class, IndexTest.class, MergeTest.class })
public class AllTests {

}
