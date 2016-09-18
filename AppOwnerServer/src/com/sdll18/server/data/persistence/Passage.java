package com.sdll18.server.data.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "PASSAGE")
public class Passage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PASSAGE_ID")
	private int id;

	@Column(name = "TITLE", nullable = true)
	private String title;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "TIME")
	private String time;

	@Column(name = "PRAISE")
	private int praise;

	@Column(name = "TYPE")
	private short type;

	@Column(name = "URL", nullable = true)
	private String url;

	@OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "PASSAGE_ID", updatable = false)
	private Set<Comment> comments = new HashSet<Comment>();

	public short getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Passage [id=" + id + ", message=" + message + ", time=" + time
				+ ", praise=" + praise + ", type=" + type + ", Url=" + url
				+ ", comments=" + comments + "]";
	}
}
