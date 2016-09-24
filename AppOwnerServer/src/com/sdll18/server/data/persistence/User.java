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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "USER", uniqueConstraints = { @UniqueConstraint(columnNames = { "USERNAME" }) })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private int id;

	@Column(name = "STATE")
	private short state;

	@Column(name = "AGE", nullable = true)
	private short age;

	@Column(name = "USERNAME", unique = true)
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "NAME", nullable = true)
	private String name;

	@Column(name = "INTRODUCE", nullable = true)
	private String introduce;

	@Column(name = "ICONURL", nullable = true)
	private String iconUrl;

	@Column(name = "EMAIL", nullable = true)
	private String email;

	@Column(name = "PHONE", nullable = true)
	private String phone;

	@Column(name = "SEX", nullable = true)
	private short sex;

	@OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "USER_ID", updatable = false)
	private Set<Comment> comments = new HashSet<Comment>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "PRAISE_COMMENT", joinColumns = { @JoinColumn(name = "PRAISE_USER_ID", referencedColumnName = "USER_ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "PRAISE_COMMENT_ID", referencedColumnName = "COMMENT_ID", nullable = false) })
	private Set<Comment> praisedComment = new HashSet<Comment>();

	public Set<Comment> getPraisedComment() {
		return praisedComment;
	}

	public void setPraisedComment(Set<Comment> praisedComment) {
		this.praisedComment = praisedComment;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", state=" + state + ", age=" + age
				+ ", username=" + username + ", password=" + password
				+ ", name=" + name + ", introduce=" + introduce + ", iconUrl="
				+ iconUrl + ", email=" + email + ", phone=" + phone + ", sex="
				+ sex + ", comments=" + comments + "]";
	}

}
