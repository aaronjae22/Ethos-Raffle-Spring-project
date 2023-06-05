package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.BundleDetails;
import com.raffle.view.model.contact.TicketPurchaseItem;

public class BundleDetailsMapper extends SqlMapperBase implements RowMapper<BundleDetails>
{

	@Override
	public BundleDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BundleDetails  detail = new BundleDetails();
		
		
		detail.setIdBundle_details(rs.getInt("idBundle_details"));
		detail.setIdPeriod(rs.getInt("idPeriod"));
		detail.setIdProduct(rs.getInt("idProduct"));

		detail.setBroken(rs.getBoolean("isBroken"));		
		detail.setBundleNumber(rs.getString("bundleNumber"));		
		detail.setTicketNumber(rs.getString("ticketNumber"));
		
		
		return detail;
	}

}
