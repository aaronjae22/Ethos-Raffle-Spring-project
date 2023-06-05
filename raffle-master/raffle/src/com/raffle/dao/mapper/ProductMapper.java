package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.Product;

public class ProductMapper implements RowMapper<Product>  {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		Product product = new Product();
		
		product.setCurrentPrice(rs.getBigDecimal( "currentPrice" ));
		product.setActive(rs.getBoolean("active"));
		product.setName(rs.getString("name"));
		product.setIdProduct(rs.getInt("idProduct"));
		product.setName(rs.getString("description"));
		
		
		return product;
	}

}
