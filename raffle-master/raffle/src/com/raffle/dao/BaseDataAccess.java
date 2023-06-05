package com.raffle.dao;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.raffle.dao.mapper.UserMapper;
import com.raffle.pojo.User;

public class BaseDataAccess 
{
	
	
	
	protected JdbcTemplate jdbcTemplate;
	
	protected NamedParameterJdbcTemplate namedJdbcTemplate;
	

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    
    public User getUserByUserName(String loggedUser)
    {
    	String sql = " select * from user where userName = :userName ";
    	
    	HashMap<String,Object> params=  new HashMap<String,Object>();
    	
    	params.put("userName", loggedUser);
    	
    	List<User> vals = namedJdbcTemplate.query(sql,params,new UserMapper());
    	if(vals.size() != 0)
    	return vals.get(0);
    	else
    		return new User();
    }
    
  
    
} 
