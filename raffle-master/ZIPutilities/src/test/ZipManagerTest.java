package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.raffle.utilities.zipUtilities.ZipCodeManager;
import com.raffle.utilities.zipUtilities.entities.ZipCodeResponse;



public class ZipManagerTest {

	@Test
	public void testRequestZipInformation() 
	{
		ZipCodeManager manager = new ZipCodeManager();
		
		ZipCodeResponse response =  manager.requestZipInformation("99501");
		
		
		assertEquals(response.getCity(), "ANCHORAGE");

		//fail("Not yet implemented");
		
	}
	 
	
	@Test
	public void testBadRequestZipInformation() 
	{
		ZipCodeManager manager = new ZipCodeManager();
		
		ZipCodeResponse response =  manager.requestZipInformation("badzipcode");
		
		System.out.println(response.getError());
		
		assertNotEquals(response.getError(), null);

		//fail("Not yet implemented");
		
	}

}
