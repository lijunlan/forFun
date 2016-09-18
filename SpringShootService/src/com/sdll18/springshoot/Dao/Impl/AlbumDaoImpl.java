package com.sdll18.springshoot.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sdll18.springshoot.Dao.AlbumDao;
import com.sdll18.springshoot.Persistant.Album;

public class AlbumDaoImpl extends HibernateDaoSupport implements AlbumDao {

	@Override
	public void create(Album album) {
		getHibernateTemplate().save(album);
	}

	@Override
	public void remove(Album album) {
		getHibernateTemplate().delete(album);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate().bulkUpdate("delete from Album a where a.id=?",
				id);
	}

	@Override
	public void update(Album album) {
		getHibernateTemplate().update(album);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public Album query(int id, boolean lazy) {
		String hql = "from Album a where a.id=?";
		if (lazy) {
			hql = "from Album a left join fetch a.photos where a.id=?";
		}
		@SuppressWarnings("unchecked")
		List<Album> list = getHibernateTemplate().find(hql, id);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Album> queryList(final int page, final int pageSize,
			final boolean lazy) {
		List<Album> list = new ArrayList<Album>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Album>>() {

					@Override
					public List<Album> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Album a ORDER BY a.createTime DESC";
						if (lazy) {
							hql = "from Album a left join fetch a.photos ORDER BY a.createTime DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Album> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Album> queryList(final String category, final int page,
			final int pageSize, final boolean lazy) {
		List<Album> list = new ArrayList<Album>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Album>>() {

					@Override
					public List<Album> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Album a where a.category = '"
								+ category + "'  ORDER BY a.createTime DESC";
						if (lazy) {
							hql = "from Album a left join fetch a.photos where a.category = '"
									+ category
									+ "'  ORDER BY a.createTime DESC";
						}
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Album> list = query.list();
						return list;
					}
				});
		return list;
	}

	@Override
	public List<Album> queryList(String category) {
		String hql = "from Album a where a.category=?";
		@SuppressWarnings("unchecked")
		List<Album> list = getHibernateTemplate().find(hql, category);
		return list;
	}

}
