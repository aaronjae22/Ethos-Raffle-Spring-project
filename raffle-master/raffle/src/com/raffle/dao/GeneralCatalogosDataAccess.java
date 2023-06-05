package com.raffle.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.FollowUpCallMapper;
import com.raffle.pojo.FollowUpCall;

@Component
public class GeneralCatalogosDataAccess extends BaseDataAccess {

	
	public List<FollowUpCall> getFowllowUpCallCatalog()
	{
		
		List<FollowUpCall> vals = jdbcTemplate.query("select idFollowUp_Call, Description, createdOn, createdByUserId from followup_call order by idFollowUp_Call", 
				new Object[] {},
				new FollowUpCallMapper());

		return vals;
	}
	
}
