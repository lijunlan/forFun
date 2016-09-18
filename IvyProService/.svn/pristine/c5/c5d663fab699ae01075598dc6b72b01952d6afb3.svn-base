package com.ivypro.Persistant;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer id;

	@Column(name = "AGE", nullable = true)
	private Short age;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "NAME", nullable = true)
	private String name;

	@Column(name = "INTRODUCE", nullable = true)
	private String introduce;

	@Column(name = "ADDRESS", nullable = true)
	private String address;

	@Column(name = "ICONURL", nullable = true)
	private String iconUrl;

	@Column(name = "EMAIL", unique = true)
	private String email;

	@Column(name = "PHONE", nullable = true)
	private String phone;

	@Column(name = "SEX", nullable = true)
	private Short sex;

	@OneToMany(targetEntity = Order.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", updatable = false)
	private Set<Order> orders = new HashSet<Order>();

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserMark userMark;

	public UserMark getUserMark() {
		return userMark;
	}

	public void setUserMark(UserMark userMark) {
		this.userMark = userMark;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
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

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}