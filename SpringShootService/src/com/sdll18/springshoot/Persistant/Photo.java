package com.sdll18.springshoot.Persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PHOTO")
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PHOTO_ID")
	private Integer id;

	private String url;

	private String name;

	private String createTime;

	@ManyToOne(targetEntity = Album.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ALBUM_ID", updatable = false)
	private Album ownedAlbum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Album getOwnedAlbum() {
		return ownedAlbum;
	}

	public void setOwnedAlbum(Album ownedAlbum) {
		this.ownedAlbum = ownedAlbum;
	}

}
