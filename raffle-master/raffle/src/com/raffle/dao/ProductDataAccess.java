package com.raffle.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.ProductMapper;
import com.raffle.pojo.Product;

@Component
public class ProductDataAccess extends BaseDataAccess{

	
	public List<Product> getProductList()
	{
		String sql = "select * from product ";
		
		
		List<Product> products = jdbcTemplate.query(sql, new ProductMapper());
		
		
		return products;
		
	}
	
}
