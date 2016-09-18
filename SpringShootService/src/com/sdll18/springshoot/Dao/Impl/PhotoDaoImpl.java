package com.sdll18.springshoot.Dao.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sdll18.springshoot.Dao.PhotoDao;
import com.sdll18.springshoot.Persistant.Photo;

public class PhotoDaoImpl extends HibernateDaoSupport implements PhotoDao {

	@Override
	public void create(Photo photo) {
		getHibernateTemplate().save(photo);
	}

	@Override
	public void remove(Photo photo) {
		getHibernateTemplate().delete(photo);
	}

	@Override
	public void remove(int id) {
		getHibernateTemplate().bulkUpdate("delete from Photo p where p.id=?",
				id);
	}

	@Override
	public void update(Photo photo) {
		getHibernateTemplate().update(photo);
	}

	@Override
	public void updateFromSql(String sql) {
		getHibernateTemplate().bulkUpdate(sql);
	}

	@Override
	public Photo query(int id) {
		String hql = "from Photo p where p.id=?";
		@SuppressWarnings("unchecked")
		List<Photo> list = getHibernateTemplate().find(hql, id);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Photo> queryList(final int page, final int pageSize) {
		List<Photo> list = new ArrayList<Photo>();
		list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<Photo>>() {

					@Override
					public List<Photo> doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "from Photo p ORDER BY p.createTime DESC";
						Query query = session.createQuery(hql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Photo> list = query.list();
						return list;
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Photo> queryList(int albumid) {
		String hql = "from Photo p left join fetch p.ownedAlbum oa where oa.id=? ORDER BY p.createTime DESC";
		List<Photo> list = getHibernateTemplate().find(hql, albumid);
		return list;
	}

}
