package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.CurrentTotalAmounts;

public class CurrentTotalAmountsMapper implements RowMapper<CurrentTotalAmounts>  
{

	@Override
	public CurrentTotalAmounts mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		CurrentTotalAmounts item = new CurrentTotalAmounts();
	
		item.setTotalTickets(rs.getInt("totalTickets"));
		item.setTotalAmount(rs.getBigDecimal("totalAmount"));
		item.setTotalBundle(rs.getBigDecimal("totalBundle"));
		item.setTotalCar(rs.getBigDecimal("totalCar"));		
		item.setTotalVac(rs.getBigDecimal("totalVac"));
		item.setPeriod(rs.getString("Period"));
		
		
		
		
		item.setTotalCount(rs.getInt("TotalCount"));
		item.setTotalCountBundle(rs.getInt("TotalCountBundle"));
		item.setTotalCountCar(rs.getInt("TotalCountCar"));
		item.setTotalCountVac(rs.getInt("TotalCountVac"));
		
		
		return item;
	}

}
