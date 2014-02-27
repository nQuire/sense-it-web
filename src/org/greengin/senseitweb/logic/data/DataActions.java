package org.greengin.senseitweb.logic.data;

import java.util.Collection;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.data.DataCollectionActivity;
import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.project.AbstractActivityActions;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteManager;
import org.greengin.senseitweb.logic.voting.VoteRequest;


public abstract class DataActions<E extends AbstractDataProjectItem, F extends AbstractDataProjectItem, T extends DataCollectionActivity<E, F>> extends AbstractActivityActions<T> {

	private static final String ITEMS_QUERY = "SELECT d FROM %s d WHERE d.dataStore = :dataStore";
	
	Class<E> dataType;
	Class<F> analysisType;
	
	public DataActions(Long projectId, HttpServletRequest request, Class<E> dataType, Class<F> analysisType, Class<T> type) {
		super(projectId, request, type);
		this.dataType = dataType;
		this.analysisType = analysisType;
	}
	
		
	/** member actions **/
	
	private <K extends AbstractDataProjectItem> Collection<K> getItems(Class<K> type) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			String queryStr = String.format(ITEMS_QUERY, type.getName());
			TypedQuery<K> query = em.createQuery(queryStr, type);
			query.setParameter("dataStore", activity);
			Collection<K> list = query.getResultList();
			for (VotableEntity item : list) {
				item.selectVoteAuthor(user);
			}
			return list;
		} 
		
		return null;
	}
	
	private <K extends AbstractDataProjectItem> Collection<K> createItem(Class<K> type) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			em.getTransaction().begin();
			try {
				K dataItem = type.newInstance();
				dataItem.setDataStore(activity);
				dataItem.setAuthor(user);
				em.persist(dataItem);
				em.getTransaction().commit();
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
			} 
			
			return getItems(type);
		} 
		
		return null;
	}
	
	
	
	public Collection<E> getData() {
		return getItems(dataType);
	}

	public Collection<E> createData() {
		return createItem(dataType);
	}
	
	public Collection<F> getAnalysis() {
		return getItems(analysisType);
	}

	public Collection<F> createAnalysis() {
		return createItem(analysisType);
	}
	
	public VoteCount voteItem(Long itemId, VoteRequest voteData) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			AbstractDataProjectItem item = em.find(AbstractDataProjectItem.class, itemId);
			if (item != null && item.getDataStore() == activity) {
				VoteManager voter = new VoteManager(em, user, item);
				return voter.vote(voteData);
			}
		}

		return null;
	}

}
