package com.dmi.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.IMessageModelDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MessageModel;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class MessageModelDAO implements IMessageModelDAO {

	private static final Logger LOG = Logger.getLogger(MessageModelDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MessageModel getMessageModelByDeviceType(Long deviceTypeId) {
		
		MessageModel messageModel = null;
		
		try{
			String queryStr = "SELECT mm FROM MessageModel mm where mm.deviceTypeBean.id =:deviceTypeId";
			TypedQuery<MessageModel> query = entityManager.createQuery(queryStr, MessageModel.class);
			query.setParameter("deviceTypeId", deviceTypeId);
			messageModel = query.getSingleResult();
			
			return messageModel;
			
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			return messageModel;
		}
	}
	
	@Override
	public List<MessageModel> getByIds(List<DeviceType> deviceTypes) {

		String queryStr = "SELECT mm from MessageModel mm WHERE mm.deviceTypeBean.id IN:deviceTypeIds";
		TypedQuery<MessageModel> query = entityManager.createQuery(queryStr, MessageModel.class);

		List<Long> deviceTypeIds = new LinkedList<>();
		for (DeviceType deviceType : deviceTypes) {
			deviceTypeIds.add(deviceType.getId());
		}

		query.setParameter("IN:deviceTypeIds", deviceTypeIds);

		List<MessageModel> result = query.getResultList();

		return result;
	}

	
	@Override
	public void deleteById(MessageModel messageModel) {
		
		entityManager.remove(messageModel);
		entityManager.flush();

		/*String queryChild = "DELETE from MessageRule mr where mr.MessageModel.messageId =:messageId";
		Query deleteChild = entityManager.createQuery(queryChild);

		deleteChild.setParameter("messageId", mm.getId());
		deleteChild.executeUpdate();

		String queryParent = "DELETE from MessageModel mm where mm.messageId =:messageId";
		Query deleteParent = entityManager.createQuery(queryParent);

		deleteParent.setParameter("messageId", mm.getId());
		deleteParent.executeUpdate();*/

	}

	public Long getMessageCount(Long deviceTypeId) {
		String sqlQuery = "SELECT COUNT(mm.id) FROM MessageModel mm WHERE mm.deviceTypeBean.id = :deviceTypeId";
		Query query = entityManager.createQuery(sqlQuery);
		return (Long) query.getSingleResult();
	}

	@Override
	@Transactional
	public void save(MessageModel model) {
		
		try{
			entityManager.persist(model);
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			
		}
	}

	@Override
	@Transactional
	public void update(MessageModel model) {

		try{
			entityManager.merge(model);
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			
		}
		
	}

	

	@Override
	public List<MessageModel> getAllMessageModel(Long oemId) {

		List<MessageModel> messageModels = null;
		
		try{
			String queryStr = "SELECT mm FROM MessageModel mm where mm.deviceTypeBean.oemBean.id=:oemId";
			TypedQuery<MessageModel> query = entityManager.createQuery(queryStr, MessageModel.class);
			query.setParameter("oemId", oemId);
			messageModels = query.getResultList();
			
			return messageModels;
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			return messageModels;
			
		}
		

	}

	@Override
	public MessageModel getMessageModelById(Long messageModelId) {
		
		MessageModel messageModel = null;
		
		try{
			String queryStr = "SELECT mm FROM MessageModel mm where mm.id =:messageModelId";
			TypedQuery<MessageModel> query = entityManager.createQuery(queryStr, MessageModel.class);
			query.setParameter("messageModelId", messageModelId);
			messageModel = query.getSingleResult();
			
			return messageModel;
			
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			return messageModel;
		}
	}

}