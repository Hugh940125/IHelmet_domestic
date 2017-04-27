package com.slinph.ihairhelmet4.internet.model;

import java.util.*;


public class User {
	private Integer age;
	private String avgG;
	private String avgT;
	private Date createDtm;
	private String currentStatus;
	private Integer del;
	private Date delDtm;
	private String email;
	private String finishData;
	private Date firstDtm;
	private Long id;
	private Integer level;
	private Integer mid;
	private String nation;
	private String openId;
	private String password;
	private String phone;
	private Integer platform;
	private String realName;
	private Integer sex;
	private Integer type;
	private Date updateDtm;
	private String userName;

	public String getAvgG() {
		return avgG;
	}

	public void setAvgG(String avgG) {
		this.avgG = avgG;
	}

	public String getAvgT() {
		return avgT;
	}

	public void setAvgT(String avgT) {
		this.avgT = avgT;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getFinishData() {
		return finishData;
	}

	public void setFinishData(String finishData) {
		this.finishData = finishData;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

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
