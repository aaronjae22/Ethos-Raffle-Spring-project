package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.Period;

public class PeriodMapper implements RowMapper<Period> {

	@Override
	public Period mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Period period = new Period();
		
		period.setIdPeriod( rs.getInt("idPeriod"));
		period.setName(rs.getString("name"));
		period.setBeginDate(rs.getDate("beginDate"));
		period.setEndDate(rs.getDate("endDate"));
		period.setActive(rs.getBoolean("active"));
		
		return period;
	}

}
