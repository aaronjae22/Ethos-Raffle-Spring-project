package com.raffle.utilities.zipUtilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raffle.utilities.zipUtilities.entities.ZipCodeResponse;

@Component
public class ZipCodeManager implements IZipCodeManager
{
	
	private final static String REST_URI = "http://ziptasticapi.com/";
	
	public ZipCodeManager()
	{
			
	}

	@Override
	public ZipCodeResponse requestZipInformation(String zipCode) {
	
		try
		{
			
		URL url = new URL( REST_URI + "" + zipCode );
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		
		
		con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		  new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();
		
		ObjectMapper mapper = new ObjectMapper();
					
		ZipCodeResponse zipFromJSON = mapper.readValue(content.toString(), ZipCodeResponse.class);
		return zipFromJSON;
		
		}catch(Exception ex)
		{
			
			ex.printStackTrace();
			return null;
		}
		
	}
	
	
	
	
}
