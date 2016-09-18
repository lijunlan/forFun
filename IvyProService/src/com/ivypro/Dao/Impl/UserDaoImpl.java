package com.ivypro.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ivypro.Dao.UserDao;
import com.ivypro.Persistant.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	@Override
	public void create(User user) {
		getHibernateTemplate().save(user);
	}

	@Override
	public void remove(User user) {
		getHibernateTemplate().delete(user);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate()
		.bulkUpdate("delete from User u where u.id=?", id);
	}

	@Override
	public void update(User user) {
		getHibernateTemplate().update(user);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public User query(int id, boolean lazy) {
		String hql = "from User u where u.id=?";
		if (lazy) {
			hql = "from User u left join fetch u.orders where u.id=?";
		}
		@SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find(hql, id);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public User query(String email, boolean lazy) {
		String hql = "from User u where u.email=?";
		if (lazy) {
			hql = "from User u left join fetch u.orders where u.email=?";
		}
		@SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find(hql, email);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryList(final int page, final int pageSize, final boolean lazy) {
		List<User> list = new ArrayList<User>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<User>>() {

					@Override
					public List<User> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from User u ORDER BY u.email DESC";
						if (lazy) {
							hql = "from User u left join fetch u.orders ORDER BY u.email DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<User> list = query.list();
						return list;
					}
				});
		return list;
	}

}
