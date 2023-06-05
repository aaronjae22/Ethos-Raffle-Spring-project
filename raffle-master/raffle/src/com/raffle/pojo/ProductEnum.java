package com.raffle.pojo;

public class ProductEnum {
	public static final int Car = 1; 
	public static final int Vacation = 2;
	public static final int Bundle = 3;
	
	
	public static String getProductName(int productId)
	{
		if(productId == Car)
		{
			return "Car";
		}
		
		if(productId == Vacation)
		{
			return "Vacation";
		}
		
		if(productId == Bundle)
		{
			return "Bundle";
		}
		return null;
	}
	
}
