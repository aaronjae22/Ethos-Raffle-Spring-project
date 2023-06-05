package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.view.model.BundleValidationInfo;

public class BundleValidationInfoMapper implements RowMapper<BundleValidationInfo>
{
	@Override
	public BundleValidationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BundleValidationInfo info = new BundleValidationInfo();
		
		info.setBundleNumber(rs.getString("bundleNumber"));
		info.setBrokenOn( rs.getDate("brokenOn") );
		info.setBrokenByUserId(rs.getInt("brokenByUserId"));
		info.setBroken(rs.getBoolean("isBroken"));
		info.setSold(rs.getBoolean("isSold"));
		
		
		return info;
	}
}
