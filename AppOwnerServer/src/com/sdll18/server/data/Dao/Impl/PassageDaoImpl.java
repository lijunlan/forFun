package com.sdll18.server.data.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sdll18.server.data.Dao.PassageDao;
import com.sdll18.server.data.persistence.Passage;


public class PassageDaoImpl extends HibernateDaoSupport implements PassageDao {

	@Override
	public void create(Passage passage) {
		getHibernateTemplate().save(passage);
	}

	@Override
	public void remove(Passage passage) {
		getHibernateTemplate().delete(passage);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate().bulkUpdate(
				"delete from Passage p where p.passage_id=?", id);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public void update(Passage passage) {
		getHibernateTemplate().update(passage);
	}

	@Override
	public Passage query(int id) {
		return getHibernateTemplate().get(Passage.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Passage> queryList(final int page, final int pageSize) {
		List<Passage> list = new ArrayList<Passage>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Passage>>() {

					@Override
					public List<Passage> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("from Passage p ORDER BY p.time DESC");
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Passage> list = query.list();
						return list;
					}
				});
		return list;
	}

}
