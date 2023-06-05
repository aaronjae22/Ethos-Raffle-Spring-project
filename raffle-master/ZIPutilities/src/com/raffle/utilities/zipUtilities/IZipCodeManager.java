package com.raffle.utilities.zipUtilities;

import com.raffle.utilities.zipUtilities.entities.ZipCodeResponse;

public interface IZipCodeManager 
{

		ZipCodeResponse requestZipInformation(String zipCode);
}
