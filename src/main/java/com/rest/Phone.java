package com.rest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;












import org.json.JSONArray;
import org.json.JSONObject;

import com.base.Contacts;
import com.base.Control;
import com.base.UserData;
import com.google.gson.Gson;

@Stateless
@Path("/phone")
public class Phone {

	
	@PersistenceContext(unitName = "myPu")
	private EntityManager em;
	
	@Inject
	Control control;
	
	public final int CREATED = 201;
	public final int OK = 200;
	public final int NOT_FOUND = 404;

	@Resource
	private SessionContext context;
	
	@Path("/test")
	@GET
	public void test(){
		//UserData userData = new UserData("123", true, " ");
		//em.persist(userData);
	}
	
	@Path("/register")
	@POST
	@Produces("*/*; charset=UTF-8")
	@Consumes("*/*; charset=UTF-8")
	public Response register(@FormParam("phoneMD5") String phoneMD5){
		
		TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		query.setParameter(1,phoneMD5);
		List<UserData> userDataTemp = query.getResultList();
		if(userDataTemp.size() > 0){//.get(0).getStatus()){
			return Response.status(OK).build();
		}else{
			UserData userData = new UserData(phoneMD5, true);
			em.persist(userData); 
			return Response.status(CREATED).build(); 
		}
	}
	
	
	
	@Path("/setSchedule")
	@POST
	@Produces("*/*; charset=UTF-8")
	@Consumes("*/*; charset=UTF-8")
	public Response saveSchedule(@FormParam("phoneMD5") String phoneMD5, @FormParam("schedule") String schedule, @FormParam("security") boolean security){
		TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		query.setParameter(1,phoneMD5);
		List<UserData> userData1 = query.getResultList();
		
		if(userData1.size() == 0){
			return Response.status(NOT_FOUND).build();
		}else{
			UserData userData =userData1.get(0);
			userData.setShedule(schedule);
			userData.setSecurity(security);
			em.merge(userData); 
			return Response.status(OK).build(); 
		}
	}
	
	@Path("/delete")
	@POST
	@Produces("*/*; charset=UTF-8")
	@Consumes("*/*; charset=UTF-8")
	public Response delete(@FormParam("phoneMD5") String phoneMD5){
		TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		query.setParameter(1,phoneMD5);
		List<UserData> userData1 = query.getResultList();
		
		if(userData1.size() == 0){
			return Response.status(NOT_FOUND).build();
		}else{
			em.remove(userData1.get(0));
			return Response.status(OK).build(); 
		}
	}
	
	@Path("/getSchedule")
	@POST
	@Produces("*/*; charset=UTF-8")
	@Consumes("*/*; charset=UTF-8")
	public Response getSchedule(@FormParam("phoneMD5") String phoneMD5, @FormParam("mySchedule") boolean mySchedule){
		TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		query.setParameter(1,phoneMD5);
		List<UserData> userData = query.getResultList();
		
		if(userData.size() == 0){
			return Response.status(NOT_FOUND).build();
		}else{
			if(mySchedule == false){
				if(userData.get(0).getSecurity() == true){
					return Response.ok(null).build();
				}else{
					return Response.ok(userData.get(0).getShedule()).build(); 
				}
			}
			return Response.ok(userData.get(0).getShedule()).build(); 
		}
	}
	
	@Path("/getCounter")
	@GET
	@Produces("*/*")
	@Consumes("*/*")
	public Response getCounter(){
		Query query = em.createQuery("SELECT COUNT(ud) FROM UserData ud");
		long count =(Long)query.getSingleResult();
		return Response.ok(count+"").build();  
		
	}
	
	@Path("/getStatus")
	@POST
	public Response getStatus(@FormParam("phoneMD5") String phoneMD5){
		TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		query.setParameter(1,phoneMD5);
		List<UserData> userData = query.getResultList();
		if(!userData.isEmpty()){
			return Response.ok(userData.get(0).getStatus()).build();
		}else{
			return Response.serverError().build();
		}
		
	}
	
	
	
	@Path("/checkContacts")
	@POST
	@Produces("*/*; charset=UTF-8")
	@Consumes("*/*; charset=UTF-8")
	public Response chackContacts(@FormParam("contactsMD5") String contactsMD5){
		Gson gson = new Gson();
		try{

		Contacts[] oldContactsList = gson.fromJson(contactsMD5, Contacts[].class);
		List<Contacts> contactsList = Arrays.asList(oldContactsList);
		List<Contacts> newContactsList = new ArrayList();	
		Contacts contacts;
		
	
		for(int i = 0; i < contactsList.size(); i++){
			TypedQuery<UserData> query = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1 AND ud.security = false");
			query.setParameter(1,contactsList.get(i).getPhone());
			List<UserData> userData = query.getResultList();
			
			if(userData.toString().length() > 2){
				contacts = new Contacts();
				contacts.setId(contactsList.get(i).getId());
				contacts.setName(contactsList.get(i).getName());
				contacts.setPhone(contactsList.get(i).getPhone());
				newContactsList.add(contacts);
			}
		}
		 
		return Response.ok(gson.toJson(newContactsList)).build();
		
		}catch(Exception e){
			return Response.status(NOT_FOUND).build();
		}
	}
}
