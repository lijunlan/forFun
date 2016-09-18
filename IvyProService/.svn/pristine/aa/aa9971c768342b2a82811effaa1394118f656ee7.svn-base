package com.ivypro.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ivypro.Dao.OrderDao;
import com.ivypro.Persistant.Order;

public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {

	@Override
	public void create(Order order) {
		getHibernateTemplate().save(order);
	}

	@Override
	public void remove(Order order) {
		getHibernateTemplate().delete(order);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate().bulkUpdate("delete from Order o where o.id=?",
				id);
	}

	@Override
	public void update(Order order) {
		getHibernateTemplate().update(order);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public Order query(int id, boolean lazy) {
		String hql = "from Order o where o.id=?";
		if (lazy) {
			hql = "from Order o left join fetch o.createUser where o.id=?";
		}
		@SuppressWarnings("unchecked")
		List<Order> list = getHibernateTemplate().find(hql, id);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> queryList(int userid) {
		return getHibernateTemplate().find(
				"from Order o where o.createUser.id=" + userid
						+ " ORDER BY o.createDate DESC");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> queryList(final int userid, final int page, final int pageSize) {
		List<Order> list = new ArrayList<Order>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Order>>() {

					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Order o where o.createUser.id="
								+ userid + " ORDER BY o.createDate DESC";
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Order> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> queryList(final int page, final int pageSize,
			final boolean lazy) {
		List<Order> list = new ArrayList<Order>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Order>>() {

					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Order o ORDER BY o.createDate DESC";
						if (lazy) {
							hql = "from Order o left join fetch o.createUser ORDER BY o.createDate DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Order> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> queryList(final int page, final int pageSize,
			final boolean lazy, final boolean isEnd) {
		List<Order> list = new ArrayList<Order>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Order>>() {

					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Order o where o.isEnd=" + isEnd
								+ " ORDER BY o.createTime DESC";
						if (lazy) {
							hql = "from Order o left join fetch o.createUser where o.isEnd="
									+ isEnd + " ORDER BY o.createDate DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Order> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> queryList(int userid, boolean isEnd) {
		return getHibernateTemplate().find(
				"from Order o where o.createUser.id=" + userid + "and o.isEnd="
						+ isEnd + " ORDER BY o.createDate DESC");
	}

}
