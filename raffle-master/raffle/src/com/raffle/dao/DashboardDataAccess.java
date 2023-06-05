package com.raffle.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.CurrentTotalAmountsMapper;
import com.raffle.pojo.CurrentTotalAmounts;

@Component
public class DashboardDataAccess extends BaseDataAccess {

	
	
	public CurrentTotalAmounts getTotalAmounts(int idPeriod) {

		List<CurrentTotalAmounts> vals = jdbcTemplate.query("select TotalTickets, TotalAmount, TotalBundle, TotalCar, TotalVac, Period,  "
				+ " TotalCount, TotalCountBundle, TotalCountCar, TotalCountVac "
				+ " from current_total_amounts where idPeriod = "  + idPeriod + " Limit 1", new Object[] {},
				new CurrentTotalAmountsMapper());

		return vals.get(0);

	}
}
	