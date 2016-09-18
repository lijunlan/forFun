package com.sdll18.umedia.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sdll18.umedia.Dao.ApplicationDao;
import com.sdll18.umedia.Persistant.Application;

public class ApplicationDaoImpl extends HibernateDaoSupport implements
		ApplicationDao {

	@Override
	public void create(Application application) {
		getHibernateTemplate().save(application);
	}

	@Override
	public void remove(Application application) {
		getHibernateTemplate().delete(application);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate().bulkUpdate(
				"delete from Application a where a.id=?", id);
	}

	@Override
	public void update(Application application) {
		getHibernateTemplate().update(application);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public Application query(int id, boolean lazy) {
		String hql = "from Application a where a.id=?";
		if (lazy) {
			hql = "from Application a left join fetch a.createUser where a.id=?";
		}
		@SuppressWarnings("unchecked")
		List<Application> list = getHibernateTemplate().find(hql, id);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Application> queryList(final int page, final int pageSize,
			final boolean lazy) {
		List<Application> list = new ArrayList<Application>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Application>>() {

					@Override
					public List<Application> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Application a ORDER BY a.createTime DESC";
						if (lazy) {
							hql = "from Application a left join fetch a.createUser ORDER BY a.createTime DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Application> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Application> queryList(int userid) {
		return getHibernateTemplate().find(
				"from Application a where a.createUser.id=" + userid
						+ " ORDER BY a.createTime DESC");
	}

}
