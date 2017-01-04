package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.Date;


public class Address {

	private Long id;

	private Integer joinId;

	private Integer provinceId;

	private String province;

	private Integer cityId;

	private String city;

	private Integer areaId;

	private String area;

	private String detailAddress;

	private Integer isDefault;

	private String name;

	private String phone;

	private String fullAddress;

	private Integer del;

	private Date delDtm;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setJoinId(Integer joinId){
		this.joinId=joinId;
	}

	public Integer getJoinId(){
		return joinId;
	}

	public void setProvinceId(Integer provinceId){
		this.provinceId=provinceId;
	}

	public Integer getProvinceId(){
		return provinceId;
	}

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return province;
	}

	public void setCityId(Integer cityId){
		this.cityId=cityId;
	}

	public Integer getCityId(){
		return cityId;
	}

	public void setCity(String city){
		this.city=city;
	}

	public String getCity(){
		return city;
	}

	public void setAreaId(Integer areaId){
		this.areaId=areaId;
	}

	public Integer getAreaId(){
		return areaId;
	}

	public void setArea(String area){
		this.area=area;
	}

	public String getArea(){
		return area;
	}

	public void setDetailAddress(String detailAddress){
		this.detailAddress=detailAddress;
	}

	public String getDetailAddress(){
		return detailAddress;
	}

	public void setIsDefault(Integer isDefault){
		this.isDefault=isDefault;
	}

	public Integer getIsDefault(){
		return isDefault;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setFullAddress(String fullAddress){
		this.fullAddress=fullAddress;
	}

	public String getFullAddress(){
		return fullAddress;
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

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

}
