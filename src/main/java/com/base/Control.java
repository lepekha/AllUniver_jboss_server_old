package com.base;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Session Bean implementation class ControlName
 */
@Stateless
@LocalBean
public class Control {
	
	@PersistenceContext(unitName = "myPu")
	private EntityManager em;
	
    public Control() {
    }
    
    public boolean checkPhone(String phoneMD5){
		TypedQuery<UserData> createQuery = (TypedQuery<UserData>) em.createQuery("SELECT ud FROM UserData ud WHERE ud.phoneMD5 = ?1");
		TypedQuery<UserData> query = createQuery;
		query.setParameter(1,phoneMD5);
		List<UserData> userData = query.getResultList();
		return userData.get(0).getStatus();
	}
    
    
    

}
