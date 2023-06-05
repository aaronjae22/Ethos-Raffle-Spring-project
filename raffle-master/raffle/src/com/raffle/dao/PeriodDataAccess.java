package com.raffle.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.PeriodMapper;
import com.raffle.pojo.Period;

@Component
public class PeriodDataAccess extends BaseDataAccess{

	//public static final int currentPeriodId  = 4;
	
	
	public List<Period> getPeriodList()
	{
		String sql = "select idPeriod, name, beginDate, endDate, active from period order by endDate desc ";
		
		
		List<Period> products = jdbcTemplate.query(sql, new PeriodMapper()); 
		
		
		return products;
		
	}
	
	public int getCurrentPeriod()
	{
		String sql = "select idPeriod, name, beginDate, endDate, active from period where active = 1";
			
		List<Period> products = jdbcTemplate.query (sql, new PeriodMapper()); 
		
		return products.get(0).getIdPeriod();
	}

	public void setActivePeriod(int idPeriod) {
		
		String sql = "update period set active =0;";		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		//parameters.put("idPeriod",  idPeriod );		
		this.namedJdbcTemplate.update(sql, parameters);
		
		
		
		sql ="update period set active = 1 where idPeriod = :idPeriod" ;
		parameters.put("idPeriod",  idPeriod );		
		this.namedJdbcTemplate.update(sql, parameters);
		
		
		
				
		
	}
}
