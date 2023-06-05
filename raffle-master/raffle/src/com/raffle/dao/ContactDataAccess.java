package com.raffle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.ContactCallDetailMapper;
import com.raffle.dao.mapper.ContactCallMapper;
import com.raffle.dao.mapper.ContactMapper;
import com.raffle.pojo.Contact;
import com.raffle.pojo.ContactCall;
import com.raffle.pojo.ContactCallDetail;

@Component
public class ContactDataAccess extends BaseDataAccess
{

	/**
	 * 
	 * Obtiene la lista de contactos filtrados por los valores de los parametros
	 * @param limit Define cuantos registros va a devolver
	 * @param defaultIdContact En caso que se desee cargar un contacto en especifico
	 * @param firstName Buscar por primer nombre
	 * @param lastName Buscar por segundo nombre
	 * @return
	 */
	public List<Contact> searchContactsByFilters(int limit, Integer defaultIdContact, String firstName, String lastName)
	{
		String sql = "select * from contact ";
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		String whereSql;
		
		
		whereSql = " where 1 = 1 ";
		
		String filtersSql = "";
		
		boolean hasFilter =false;
		if(firstName != null && !firstName.trim().isEmpty())
		{
			filtersSql += " AND firstName like  concat('%' , :firstName , '%') ";		
			params.put("firstName", firstName);		
			hasFilter=true;
		}
		
		if(lastName != null && !lastName.trim().isEmpty())
		{
			filtersSql += " AND lastName like  concat('%' , :lastName , '%') ";
			params.put("lastName", lastName);
			hasFilter=true;
		}
		
		if(defaultIdContact != null)
		{
			filtersSql += " AND idContact = :idContact ";
			params.put("idContact", defaultIdContact.intValue());
			hasFilter=true;
		}
		
		hasFilter = false;
		
		
		sql += whereSql + (hasFilter ? "(" : "" ) + filtersSql +  (hasFilter ? ")" : "") +   " order by firstName asc, lastName asc limit :limit  ";
		params.put("limit", limit);
		
		List<Contact> vals = namedJdbcTemplate.query(sql,params,new ContactMapper());

		return vals;		
	}
	
	
	/**
	 * Obtiene el la informacion del un contacto del id especifico
	 * @param idContact
	 * @return
	 */
	public Contact getContactById(int idContact)
	{
		List<Contact> contacts = this.searchContactsByFilters(1, idContact, null, null);
		if(contacts.size() > 0)
			return contacts.get(0);
		else		
			return null;
	}
	
	
	public List<Contact> searchContactsByFirstName(String searchTerm, int limit) 
	{
		
		String sql = "select * from contact ";
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		String whereSql = "";
		
		if(searchTerm != null && !searchTerm.trim().equals("") )
		{
			whereSql = " where lastName like concat('%',:searchTerm,'%') " ;
			params.put("searchTerm", searchTerm);					
		}
		
		sql += whereSql +  " limit :limit ";
		params.put("limit", limit);
		
		List<Contact> vals = namedJdbcTemplate.query(sql,params,new ContactMapper());

		return vals;		
	}
	
	
	public java.math.BigInteger createContact(Contact contact)
	{
		String INSERT_SQL = "insert into contact (firstName, lastName, email, phone, creationDate, address, city, state, zipcode) values(?,?,?,?,?,?,?,?,?);";
	
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS );
		            
		            ps.setString(1, contact.getFirstName());
		            ps.setString(2, contact.getLastName());
		            ps.setString(3, contact.getEmail());
		            ps.setString(4, contact.getPhone());
		            ps.setDate(5, new java.sql.Date( new java.util.Date().getTime()));
		            ps.setString(6, contact.getAddress());
		            ps.setString(7, contact.getCity());
		            ps.setString(8, contact.getState());
		            ps.setString(9, contact.getZipCode());
		            
		            return ps;
		            
		        }
		    },
		    keyHolder);
		
		Object key = keyHolder.getKey();
		
		return (java.math.BigInteger)key;
	}


	public void update(Contact contact) {
		
		String sql = "update contact set firstName = :firstName, lastName = :lastName, email  = :email, phone = :phone, address = :address, city = :city, state = :state, zipcode = :zipcode where idContact=:idContact";
		
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("firstName", contact.getFirstName());
		parameters.put("lastName", contact.getLastName());
		parameters.put("email", contact.getEmail());
		parameters.put("phone", contact.getPhone());
		parameters.put("address", contact.getAddress());
		parameters.put("city", contact.getCity());
		
		parameters.put("state", contact.getState());
		parameters.put("zipcode", contact.getZipCode());
		parameters.put("idContact", contact.getIdContact());
		
		
		this.namedJdbcTemplate.update(sql, parameters);
			
		
	}
	
	
	public List<Contact> getContactList(int idPeriod			) 
	{
		
		String sql = "select * from contact order by lastName, firstName";
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
				
		List<Contact> vals = namedJdbcTemplate.query(sql,params,new ContactMapper());

		return vals;		
	}


	public List<ContactCall> getContactCallList(int idContact) {
		
		String sql = String.join("",
				" select ca.idContact_call, c.idContact, c.firstName + ' ' + c.lastName as contact,  ca.callDate, ca.createdOn, ", 
				" ca.createdByUserId,                                                                        ",
				" u.fullName as createdByUser, ca.memo, ca.spokenWithContact, ca.followUpCallId,fo.Description as followUpCall  ",
				" from contact c                                                                             ",
				" inner join contact_call  ca using(idContact)                                               ",
				" inner join `user` u on u.idUser = ca.createdByUserId                                       ",
				" inner join followup_call fo  on fo.idFollowUp_Call = ca.followUpCallId                     ",
				" where c.idContact =    :idContact order by ca.createdOn  desc                                  "
				);
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idContact", idContact);
				
		List<ContactCall> vals = namedJdbcTemplate.query(sql,params,new ContactCallMapper());

		return vals;
	}


	public void saveContactCall(Integer idContact, Date callDate, String memo, Boolean spokenWithContact,
			Integer idFollowUpCall, int idUser) {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");				
		String dateInString = format.format(callDate);
		
		String INSERT_SQL = "insert into contact_call (idContact, callDate, createdOn, createdByUserId, memo, spokenWithContact, followUpCallId) values (?, '" + dateInString +"', CURRENT_TIMESTAMP(), ?, ?, ?, ?  )";
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"idContact"});
		            
		            ps.setInt(1, idContact );		            
		            ps.setInt(2, idUser );
		            ps.setString(3, memo );
		            ps.setBoolean(4, spokenWithContact );
		            ps.setInt(5, idFollowUpCall );
		            		               
		            
		            return ps;
		            
		        }
		    },
		    keyHolder);
	}
	
	
	public List<ContactCallDetail> getContactCalls()
	{
		
		String sql = String.join("", 
				
				"select cc.idContact_call,cc.idContact, cc.callDate, cc.createdOn,																							", 
				"cc.memo, cc.spokenWithContact, cc.followUpCallId, f.Description as followUpCall, cc.idContact , concat(c.firstName ,' ' , c.lastName) as contact, c.email, ",
				"c.phone,c.address, c.city, c.state, c.zipcode, cc.idContact_call, f.Description as followUpCall, cc.createdByUserId, us.fullName as createdByUser                                                 ",
				"from contact_call cc                                                                                                                                       ",
				"inner join contact  c on c.idContact = cc.idContact                                                                                                        ",
				"inner join followup_call f on  f.idFollowUp_Call = cc.followUpCallId                                                                                       ",
				"inner join user us on cc.createdByUserId = us.idUser																										",
				"order by cc.createdOn desc																																	"				
				);
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		//params.put("idContact", idContact);
				
		List<ContactCallDetail> vals = namedJdbcTemplate.query(sql,params,new ContactCallDetailMapper());

		return vals;
				
	}

}
