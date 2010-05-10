package com.clairiot.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.clairiot.domain.Picture;

public class PictureDAO {

	HibernateTemplate hibernateTemplate;
	
	private static final Logger log = Logger.getLogger(PictureDAO.class);
	
	public PictureDAO() { }

	public Picture find(long id) {		
		return (Picture) hibernateTemplate.get(Picture.class, id);
	}
	
	public List<Picture> findAll() {
		return hibernateTemplate.loadAll(Picture.class);
	}
	
	public Picture save(Picture picture) {
		if (picture != null) {
			this.hibernateTemplate.persist(picture);
		}
		
		return picture;
	}
	
	// for injection
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

}
