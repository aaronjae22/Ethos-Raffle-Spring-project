package com.raffle.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.UserMapper;
import com.raffle.pojo.User;

@Component
public class UserDataAccess  extends BaseDataAccess {

	
	public List<User> loadUsers()
	{
		String userlistSql=" select idUser, userName, password, creationDate, active, administrator, fullName from user ";		
		HashMap<String,Object> params=  new HashMap<String,Object>();
				
		List<User> vals = namedJdbcTemplate.query(userlistSql,params,new UserMapper());

		return vals;
	}
	
	  public User getUserById(int idUser)
    {
    	String sql = " select idUser, userName, creationDate, active, administrator, fullName from user where idUser = :idUser ";
    	
    	HashMap<String,Object> params=  new HashMap<String,Object>();
    	
    	params.put("idUser", idUser);
    	
    	List<User> vals = namedJdbcTemplate.query(sql,params,new UserMapper());
    	
    	return vals.get(0);    	
    }

	  @Autowired 
	  private PasswordEncoder passwordEncoder;
	  
	public void saveUser(User user) {
		
		String sql = " insert into `user` (fullName, userName, password, creationDate, active, administrator, modifiedDate ) " +
					 " values (:fullName, :userName, :password, current_timestamp(), :active, :administrator, current_timestamp());  ";
		
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("fullName", user.getFullName());
		parameters.put("userName", user.getUserName());
		parameters.put("active", user.isActive());
		parameters.put("administrator", user.isAdministrator());
		parameters.put("password", passwordEncoder.encode( user.getUserName()));
		
		
		this.namedJdbcTemplate.update(sql, parameters);
	}

	public void updateUser(User user, int idUser) 
	{
		
		String sql = "update `user` set userName=:userName, active=:active, administrator=:administrator,fullName=:fullName, modifiedDate=CURRENT_TIMESTAMP() where idUser=:idUser; ";
		
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("fullName", user.getFullName());
		parameters.put("userName", user.getUserName());
		parameters.put("active", user.isActive());
		parameters.put("administrator", user.isAdministrator());
		
		parameters.put("idUser", user.getIdUser());
		
		
		this.namedJdbcTemplate.update(sql, parameters);
		
	}

	public void changeUserPassword(int idUser, String password) {
		String sql = " update user set password = :password where idUser = :idUser ";
	
	
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("idUser", idUser);
		parameters.put("password", this.passwordEncoder.encode(password));
		
		this.namedJdbcTemplate.update(sql, parameters);
	}

}
