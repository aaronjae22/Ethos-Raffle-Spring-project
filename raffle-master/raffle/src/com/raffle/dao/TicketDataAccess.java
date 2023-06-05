package com.raffle.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.raffle.dao.mapper.BundleDetailsMapper;
import com.raffle.dao.mapper.BundleValidationInfoMapper;
import com.raffle.dao.mapper.EditTicketValidationInfoMapper;
import com.raffle.dao.mapper.TicketListItemMapper;
import com.raffle.dao.mapper.TicketNumbersMapper;
import com.raffle.dao.mapper.TicketPurchaseItemMapper;
import com.raffle.pojo.BundleDetails;
import com.raffle.pojo.Contact;
import com.raffle.pojo.ProductEnum;
import com.raffle.pojo.TicketCounterWeek;
import com.raffle.pojo.TicketListItem;
import com.raffle.pojo.TicketNumbers;
import com.raffle.pojo.User;
import com.raffle.view.model.BundleTickets;
import com.raffle.view.model.BundleValidationInfo;
import com.raffle.view.model.EditTicketValidationInfo;
import com.raffle.view.model.MultipleTicketFormValues;
import com.raffle.view.model.PurchasedTicketInformation;
import com.raffle.view.model.contact.TicketPurchaseItem;


@Component
public class TicketDataAccess extends BaseDataAccess {

	
	@Autowired
	PeriodDataAccess periodDataAccess;
	
	@Autowired
	ContactDataAccess contactDataAccess;
	
	public List<TicketListItem> getTicketList(int limit,int idPeriod, String searchText, Map<String, String > columnsFilters, List<Integer> idProducts ) {

		String sql = "select * from current_ticket_list where idPeriod = :idPeriod ";
		String sqlSufix = " order by creationDate desc limit " + limit;
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		
		params.put("idPeriod", idPeriod);
		
		if(searchText != null && !searchText.trim().equals("") )
		{
			String where  = 
					 " and  ( firstName like concat('%' , :searchText , '%') or       "
					+ " lastName like concat('%' , :searchText , '%') or              "
					+ " address like concat('%' , :searchText , '%') or               "
					+ " city like concat('%' , :searchText , '%') or                  "
					+ " state like concat('%' , :searchText , '%') or                 "
					+ " zipcode like concat('%' , :searchText , '%') or               "
					+ " phone like concat('%' , :searchText , '%') or                 "
					+ " email like concat('%' , :searchText , '%') or                 "
					+ " ticketNumber like concat('%' , :searchText , '%')    )         " ;

			sql += where;
			
			params.put("searchText", searchText );			
		}
		
		if(columnsFilters != null)
		{
			int cont = 0;
			
			for(Entry<String,String> item : columnsFilters.entrySet())
			{
				if(cont < columnsFilters.size())
				{
					sql += " AND ";
				}
				cont++;
				sql += "  " + item.getKey() + " like concat(:" + item.getKey() + " , '%')  " ;
				params.put(item.getKey(), item.getValue());
			}
		}
		
		
		if(idProducts != null && idProducts.size() > 0)
		{
			sql += " and idProduct in (:idProducts) ";
			params.put("idProducts", idProducts);
		}
		
		sql += sqlSufix;
		 
		System.out.println(sql);
		
		List<TicketListItem> vals = namedJdbcTemplate.query(sql,params,new TicketListItemMapper());

		return vals;

	}
	
	public void saveTicket(TicketListItem ticket, int idUser, String transactionTag)
	{
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");				
		String dateInString = format.format(ticket.getPurchaseDate());
		
		
		String INSERT_SQL = "insert into ticket (idPeriod,idContact, ticketNumber, purchaseDate, creationDate, idUser, paymentType, idProduct, tax, price, bundleNumber, transactionTag, memo , weekNumber) " +
		" values(?,?,?,?,current_timestamp(),?,?,?,?,?,?,?,?, (select ROUND(DATEDIFF('" + dateInString +  "', beginDate)/7, 0) + 1 AS weeksout from period where active = 1) );";
		
		
		int currentPeriod = periodDataAccess. getCurrentPeriod();
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"idContact"});
		            
		            
		            
		            ps.setInt(1, currentPeriod);
		            ps.setInt(2, ticket.getIdContact());
		            ps.setString(3, ticket.getTicketNumber());
		         
		        
		            ps.setDate(4, new java.sql.Date( ticket.getPurchaseDate().getTime()));
		            //ps.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
		            
		            ps.setInt(5, idUser);
		            ps.setString(6, ticket.getPaymentType());
		            ps.setInt(7, ticket.getIdProduct());
		            
		            ps.setBigDecimal(8, new BigDecimal(0));		//TODO: falta set el tax
		            ps.setBigDecimal(9, ticket.getPrice() );
		            
		            ps.setString(10, ticket.getBundleNumber());
		            ps.setString(11, transactionTag);
		            
		            ps.setString(12, ticket.getMemo());
		            
		            
		            //ps.setDate(13, new java.sql.Date( ticket.getPurchaseDate().getTime()));
		           
		            
		            return ps;
		            
		        }
		    },
		    keyHolder);
		
				
		
	}

	
	/**
	 * Guarda toda la informacion de las tickets, en forma de lista
	 * @param allTickets 
	 */
	public void saveAllTickets(List<TicketListItem> allTickets, String loggedUser) {
		
		UUID transactionTag = UUID.randomUUID();
		
		User user = this.getUserByUserName(loggedUser);
		
		// TODO Generar esto de una mejor forma, con los statements multiples
		for(TicketListItem ticket : allTickets)
		{
			this.saveTicket(ticket, user.getIdUser(), transactionTag.toString());
		}
		
		
	}

	public void deleteTicket(int idTicket, int currentUserId, boolean remove) 
	{
		String sql = "";
		if(remove)
			sql = " delete from  ticket where idTicket = :idTicket ";
		else
			sql = " update ticket set active =  0, modifiedBy = :currentUserId where idTicket = :idTicket ";
		
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("idTicket", idTicket);
		parameters.put("currentUserId", currentUserId);
	
		this.namedJdbcTemplate.update(sql, parameters);
		
	}

	public List<String> validateTickets(List<String> ticketNumbers, int idProduct ) 
	{
		
		if(ticketNumbers != null && ticketNumbers.size() > 0)
		{
			String sql = "select distinct t.ticketNumber                                        				 "+
						"	from ticket t                                                       				 "+
						"	where t.idPeriod in (select p.idPeriod from period p where p.active = 1) and t.active = 1  "+
						"	and t.ticketNumber in ({ids}) {productWhere} order by t.ticketNumber asc							 ";							
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			
			//validate by product
			String where = " and t.idProduct = :idProduct ";			
			sql  = sql.replace("{productWhere}", where);
			
			int i = 0 ;			
			String paramSql = "";
			for(String number : ticketNumbers)
			{
				String paramName = "number" + i;
				parameters.put( paramName, number);				
				paramSql += " :" + paramName ;  								
				i++;
				
				if(i != ticketNumbers.size())
				{
					paramSql += " , ";
				}				
			}	
			
			parameters.put("idProduct", idProduct);
			
			sql = sql.replace("{ids}", paramSql);
			System.out.println(sql);	
			
			List<String> vals = namedJdbcTemplate.query(sql,parameters, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					String invalidTicket = rs.getString("ticketNumber");
					String productName = "";
					
					switch(idProduct)
					{
						case ProductEnum.Car:
							productName = "CAR";
						break;
						
						case ProductEnum.Vacation:
							productName = "VAC";						
						break;
						
						case ProductEnum.Bundle:
							productName = "BUN";
						break;
					}
					
					invalidTicket = invalidTicket + " ("+ productName + ") ";
					
					return invalidTicket;
				}
			});

			return vals;
		}
		else
		{		
			return new ArrayList<String>();
		}
	}
	
	
	
	public List<String> validateTickets( MultipleTicketFormValues allTickets ) 
	{
		List<String> allCarTickets = new ArrayList<String>();
		List<String> allVacTickets = new ArrayList<String>();
		List<String> allBundleTickets = new ArrayList<String>();
		
		for(BundleTickets bundleTicket : allTickets.getBundleTickets() )
		{
			if(  bundleTicket.getCarTickets() != null )
				allCarTickets.addAll(bundleTicket.getCarTickets());
			
			if(bundleTicket.getVacTickets() != null )
				allVacTickets.addAll(bundleTicket.getVacTickets());
			
			if(bundleTicket.getBundleNumber() != null &&  !"".equals( bundleTicket.getBundleNumber().trim()) )
			{
				allBundleTickets.add(bundleTicket.getBundleNumber());
			}
				
		}
					
		List<String> carMessages = 		this.validateTickets(allCarTickets, ProductEnum.Car);
		List<String> vacMessages = 		this.validateTickets(allVacTickets, ProductEnum.Vacation);
		List<String> bundleMessages = 	this.validateTickets(allBundleTickets, ProductEnum.Bundle);
		
		List<String> allTicketsErrors = new ArrayList<String>();
		
		allTicketsErrors.addAll(carMessages);
		allTicketsErrors.addAll(vacMessages);
		allTicketsErrors.addAll(bundleMessages);
		
		return allTicketsErrors;		
	}

	/**
	 * Obtiene la informacion de un ticket en especifico
	 * @param idTicket
	 * @return
	 */
	public TicketListItem getTicketDetails(int idTicket) {
		
		
			String sql = " select * , if( det.idBundle_details is Null , false , true ) as isInBundle , det.bundleNumber as bundleNumberFromCatalog"
					+ " from current_ticket_list t "
					+ " left join bundle_details det on t.ticketNumber =det.ticketNumber and t.idPeriod = det.idPeriod "
					+ " where idTicket = :idTicket ";						
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			
			parameters.put("idTicket", idTicket);
		
			List<TicketListItem> vals = namedJdbcTemplate.query(sql,parameters, new TicketListItemMapper());

			return vals.get(0);
	}

		
	/**
	 * Retrieve Ticket count by Week
	 * @param idPeriod
	 * @return
	 */
	public List<TicketCounterWeek> getTicketCountByWeek(int idPeriod) {
		
		String sql = "	select count(1) as TicketCounter , weekNumber "+ 
					 "	from ticket 					"+
					 "	where idPeriod = :idPeriod and active=1		"+
					 "	group by weekNumber				" +
					 "	order by weekNumber asc 		";
		
		HashMap<String, Object > parameters = new HashMap<String, Object>();
		parameters.put("idPeriod", idPeriod);
		
		List<TicketCounterWeek> counters =  namedJdbcTemplate.query(sql, parameters, new RowMapper<TicketCounterWeek>(){
			public TicketCounterWeek mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				TicketCounterWeek mapper = new TicketCounterWeek();
				
				mapper.setCounter( 		rs.getInt("TicketCounter")	);
				mapper.setWeekNumber(	rs.getInt("WeekNumber")	);
				
				return mapper;				
			}} );
		
		
		return counters;
		
		
	}

	public void winnerTicket(int idTicket, int idUser, boolean makeWinner) {
		
		String sql = " update ticket set IsWinner =  :makeWinner,SetWinnerBy = :currentUserId, SetWinnerOn = CURRENT_TIMESTAMP() , modifiedBy = :currentUserId where idTicket = :idTicket ";
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		
		if(makeWinner)
		{
			parameters.put("makeWinner", 1);
		}
		else
		{
			parameters.put("makeWinner", 0);
		}
		
		parameters.put("idTicket", idTicket);
		parameters.put("currentUserId", idUser);
	
		this.namedJdbcTemplate.update(sql, parameters);
	
	}

	public List<TicketPurchaseItem> getTicketPurchaseList(int idContact) 
	{
		
		String sql = String.join(
				"",
				" select l.idPeriod, p.name as period, l.idTicket, l.idContact, l.firstName + ' '  + l.lastName as contact,										",
				" l.ticketNumber, l.purchaseDate, 0 as createdByUserId , l.createdBy as createdByUser, l.paymentType, l.idProduct, l.productName as product,    ",
				" l.bundleNumber, l.price                                                                                                                       ",
				" from current_ticket_list l                                                                                                                    ",
				" inner join period p on l.idPeriod =p.idPeriod                                                                                                 ",
				" where l.idContact= :idContact                                                                                                                 ",
				" order by l.purchaseDate desc                                                                                                                  "	
				);
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idContact", idContact);
				
		List<TicketPurchaseItem> vals = namedJdbcTemplate.query(sql,params,new TicketPurchaseItemMapper());

		return vals;
		
		
	}

	public List<BundleDetails> retrieveBundleDetails(int idPeriod, String ticketNumber, boolean isBundleNumber) {
		
		
		String sql = String.join(
				"",
				" select det.idBundle_details,det.idPeriod, det.bundleNumber, det.ticketNumber, det.isBroken, det.idProduct 		"+
				" from  bundle_details det												"+
				" where det.idPeriod= :idPeriod  and " + (isBundleNumber ? "det.bundleNumber" : "det.ticketNumber" ) + "= :ticketNumber			"
				);
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idPeriod", idPeriod);
		
			params.put("ticketNumber", ticketNumber);			
			
		List<BundleDetails> vals = namedJdbcTemplate.query(sql,params,new BundleDetailsMapper());
 
		return vals;
		
	}

	public void breakBundleTicket(int idPeriodId, String bundleNumber, int idUser) {
		
		
		String sql = "";
		
		sql = " update bundle_details set isBroken=1 ,brokenByUserId = :idUser , brokenOn = current_timestamp() " +
				" where idPeriod  = :idPeriodId and bundleNumber = :bundleNumber "   ;
		
		
		HashMap<String, Object > parameters = new HashMap<String,Object>();
		
		parameters.put("idPeriodId", idPeriodId);
		parameters.put("bundleNumber", bundleNumber);
		parameters.put("idUser", idUser);
	
		this.namedJdbcTemplate.update(sql, parameters);
		
		
	}

	
	
	/**
	 * 
	 * Obtiene la informacion de un ticket en especifico, para validaciones
	 * @param idPeriod
	 * @param ticketNumber
	 * @return
	 */
	public List<TicketNumbers> getTicketData(int idPeriod, String ticketNumber)
	{
		String sql = String.join(
				"",
				" 	select t.idTicket_numbers, t.idPeriod,  t.ticket_number, t.idProduct 			" +
				"	from ticket_numbers t												" +
				"	where t.idPeriod = :idPeriod and t.ticket_number = :ticketNumber 	" 			
				);
		
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idPeriod", idPeriod);
		
		params.put("ticketNumber", ticketNumber);
		params.put("idPeriod", idPeriod);
			
		List<TicketNumbers> vals = namedJdbcTemplate.query(sql,params,new TicketNumbersMapper());
 
		return vals;
	}
	
	
	
	public PurchasedTicketInformation getPurchasedTicketInformation(int idPeriod, int idTicket)
	{
		PurchasedTicketInformation info = new PurchasedTicketInformation();
				
		
		TicketListItem ticketInformation = this.getTicketDetails(idTicket);
		info.setBundle( ticketInformation.getIdProduct() == ProductEnum.Bundle );	
		info.setDeleted( !ticketInformation.isActive() );
		
		info.setPaymentType(ticketInformation.getPaymentType());
		info.setIdTicket(ticketInformation.getIdTicket());
		info.setPurchaseDate(ticketInformation.getPurchaseDate());
		info.setTicketNumber(ticketInformation.getTicketNumber());
		
		info.setBundleNumberFromCatalog(ticketInformation.getBundleNumberFromCatalog());		
		info.setFromBundle( ticketInformation.isInBundle() );
		
		Contact contact = this.contactDataAccess.getContactById( ticketInformation.getIdContact() );
		info.setContact(contact);
		
		
		List<BundleDetails> bundleInfo = this.retrieveBundleDetails(idPeriod, ticketInformation.getTicketNumber(),  info.isBundle() );
		
		String productName = ProductEnum.getProductName(ticketInformation.getIdProduct());
			
		
		info.setProductName( productName  );
		
		if(info.isBundle())
		{
			List<String> carTickets = new ArrayList<String>();
			List<String> vacTickets =new ArrayList<String>();
			
			bundleInfo.forEach( (t) -> { if (t.getIdProduct() == ProductEnum.Car){ 				
					carTickets.add(t.getTicketNumber());				
			}
			else if(t.getIdProduct() == ProductEnum.Vacation)				
				{
					vacTickets.add(t.getTicketNumber());
				}
			});
			
			info.setCarTickets( carTickets );
			info.setVacTickets(vacTickets);
		}
			
		return info;		
	}

	public BundleValidationInfo getBundleValidationInfo(int idPeriod, String ticketNumber) {
		
		String sql = String.join(
				"",
				"select bd.bundleNumber, bd.brokenOn, bd.brokenByUserId, bd.isBroken, if( t.idTicket is null, FALSE , TRUE ) IsSold " +
				"from bundle_details bd                                                                                      " +
				"left join ticket t on t.ticketNumber = bd.bundleNumber and t.idPeriod = bd.idPeriod  and t.active = 1 		 " +
				"where bd.idPeriod = :idPeriod and bd.bundleNumber = :ticketNumber                                           " +
				"limit 1                                                                                                     " 				
				);
		 
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idPeriod", idPeriod);		
		params.put("ticketNumber", ticketNumber);
			
		List<BundleValidationInfo> vals = namedJdbcTemplate.query(sql,params,new BundleValidationInfoMapper());
 
		if(vals != null && vals.size()>0)
			return vals.get(0);
		else			
			return null;
	}

	public EditTicketValidationInfo getEditTicketValidationInfo(int idPeriod, String ticketNumber) 
	{
		/*
		    select tn.ticket_number as ticketNumber, tn.idProduct, if(t.ticketNumber is null , FALSE, TRUE) AS isSold, det.bundleNumber, det.isBroken, det.brokenByUserId, det.brokenOn
			from ticket_numbers tn 
			left join ticket t on t.ticketNumber = tn.ticket_number and t.idPeriod = tn.idPeriod and t.active = 1
			left join bundle_details det on det.ticketNumber = tn.ticket_number and det.idPeriod = tn.idPeriod
			where tn.idProduct in (1,2)
			and tn.ticket_number='0501' and tn.idPeriod=7
		 */
		
		String sql = String.join(
					 "",
					 "select tn.ticket_number as ticketNumber, tn.idProduct, if(t.ticketNumber is null , FALSE, TRUE) AS isSold, "+
					"det.bundleNumber, det.isBroken, det.brokenByUserId, det.brokenOn											 "+
					"from ticket_numbers tn                                                                                      "+
					"left join ticket t on t.ticketNumber = tn.ticket_number and t.idPeriod = tn.idPeriod and t.active = 1       "+
					"left join bundle_details det on det.ticketNumber = tn.ticket_number and det.idPeriod = tn.idPeriod          "+
					"where tn.idProduct in (1,2)											                                     "+
					"and tn.ticket_number=:ticketNumber and tn.idPeriod=:idPeriod "
				);
		 
		HashMap<String,Object> params=  new HashMap<String,Object>();
		params.put("idPeriod", idPeriod);		
		params.put("ticketNumber", ticketNumber);
			
		List<EditTicketValidationInfo> vals = namedJdbcTemplate.query(sql,params,new EditTicketValidationInfoMapper());
 
		if(vals != null && vals.size()>0)
			return vals.get(0);
		else			
			return null;
		
	}

	
	public void saveNewEditTicketInformation(int idPeriod, String newTicketNumber, 
			String oldTicketNumber,int idProduct, Date purchaseDate, String paymentType, int idUser) throws IOException 
	{

		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format( purchaseDate  );
		
		
		if(idProduct != 3)
		{
			String sql = "";
			sql = " update ticket t set t.ticketNumber = :newTicketNumber, t.purchaseDate='" +  dateString + "' , t.paymentType=:paymentType, t.modifiedBy=:idUser "
				+ " where t.ticketNumber = :oldTicketNumber and t.idPeriod = :idPeriod";
			HashMap<String, Object > parameters = new HashMap<String,Object>();
			
			parameters.put("idPeriod", idPeriod);
			
			parameters.put("newTicketNumber", newTicketNumber);
			parameters.put("oldTicketNumber", oldTicketNumber);
			
			
			parameters.put("newBundleNumber", newTicketNumber);
			parameters.put("oldBundleNumber", oldTicketNumber);
			
			
			parameters.put("paymentType", paymentType);
			parameters.put("idUser", idUser);
			
			this.namedJdbcTemplate.execute(sql, parameters, new PreparedStatementCallback<Object>() 
			{  
			    @Override  
			    public Object doInPreparedStatement(PreparedStatement ps)  
			            throws SQLException, DataAccessException {  
			        return ps.executeUpdate();  
			    }  
			} );
		}
		else
		{
			executeSpEditTicket(idUser, oldTicketNumber, newTicketNumber, paymentType, purchaseDate, idPeriod);
		}				
	}
	private void executeSpEditTicket(int idUser, String oldTicketNumber, String newTicketNumber,String paymentType, Date purchaseDate,  int idPeriod)
	{
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.jdbcTemplate.getDataSource()).withProcedureName("saveNewTicketChanges").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("@idUser", java.sql.Types.INTEGER))
				.declareParameters(new SqlParameter("@oldBundleNumber", java.sql.Types.VARCHAR))
				.declareParameters(new SqlParameter("@newBundleNumber", java.sql.Types.VARCHAR))
				.declareParameters(new SqlParameter("@paymentType", java.sql.Types.VARCHAR))
				.declareParameters(new SqlParameter("@purchaseDate", java.sql.Types.DATE))
				.declareParameters(new SqlParameter("@idPeriod", java.sql.Types.INTEGER))
				;
		MapSqlParameterSource source = new MapSqlParameterSource();
		
		
		source = source.addValue("@idUser", idUser)
			.addValue("@oldBundleNumber", oldTicketNumber)
			.addValue("@newBundleNumber", newTicketNumber)
			.addValue("@paymentType", paymentType)
			.addValue("@purchaseDate", purchaseDate)
			.addValue("@idPeriod", idPeriod);
		
		Map<String, Object> out = jdbcCall.execute(source);
		
		
		
	}
}