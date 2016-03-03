package com.rest.test.coupleaccounts;

import java.math.BigDecimal;

import config.DatabaseConfiguration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	BigDecimal bd = new BigDecimal("140.825");
		bd = bd.setScale(2, BigDecimal.ROUND_UP);
		System.out.println(bd);
		
		Double a = 140.825;
		System.out.println(a/2);
    }
    
    public void testConfig() {
    	System.out.println(DatabaseConfiguration.getInstance().getProperty("user"));
    }
}
