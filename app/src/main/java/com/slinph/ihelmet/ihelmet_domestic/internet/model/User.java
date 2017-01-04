package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.*;


public class User {

	private Long id;

	private String userName;

	private String password;

	private Integer age;

	private Integer sex;

	private String realName;

	private String phone;

	private String nation;

	private Integer level;
	
	private Integer type;
	
	private String email;

	private Date firstDtm;
	
	private String openId;
	
	private int platform;
	
	private int mid;

	private Integer del;

	private Date delDtm;

	private Date updateDtm;

	private Date createDtm;

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setPassword(String password){
		this.password=password;
	}

	public String getPassword(){
		return password;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public Integer getAge(){
		return age;
	}

	public void setSex(Integer sex){
		this.sex=sex;
	}

	public Integer getSex(){
		return sex;
	}

	public void setRealName(String realName){
		this.realName=realName;
	}

	public String getRealName(){
		return realName;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setNation(String nation){
		this.nation=nation;
	}

	public String getNation(){
		return nation;
	}

	public void setLevel(Integer level){
		this.level=level;
	}

	public Integer getLevel(){
		return level;
	}
	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public void setFirstDtm(Date firstDtm){
		this.firstDtm=firstDtm;
	}

	public Date getFirstDtm(){
		return firstDtm;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public void setDel(Integer del){
		this.del=del;
	}

	public Integer getDel(){
		return del;
	}

	public void setDelDtm(Date delDtm){
		this.delDtm=delDtm;
	}

	public Date getDelDtm(){
		return delDtm;
	}

	public void setUpdateDtm(Date updateDtm){
		this.updateDtm=updateDtm;
	}

	public Date getUpdateDtm(){
		return updateDtm;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

}
