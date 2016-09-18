package com.sdll18.server.data.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COMMENT")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private int id;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "TIME")
	private String time;

	@Column(name = "PRAISE")
	private int praise;

	@Column(name = "TYPE", nullable = true)
	private short type;

	@Column(name = "URL", nullable = true)
	private String url;

	@ManyToOne(targetEntity = Passage.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSAGE_ID", updatable = false)
	private Passage passage;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", updatable = false)
	private User user;

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "praisedComment", fetch = FetchType.LAZY)
	private Set<User> praisedUser = new HashSet<User>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Passage getPassage() {
		return passage;
	}

	public void setPassage(Passage passage) {
		this.passage = passage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", time=" + time
				+ ", praise=" + praise + ", type=" + type + ", Url=" + url
				+ ", passage=" + passage + ", user=" + user + "]";
	}

}
