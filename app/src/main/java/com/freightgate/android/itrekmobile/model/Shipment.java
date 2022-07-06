package com.freightgate.android.itrekmobile.model;

public class Shipment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6233882665039696308L;
	
	private String agent_ref;
	private String agent_num;
	private String agent_name;
    private String agent_add1;
    private String agent_add2;
    private String agent_city;
    private String agent_postal;
    private String agent_state;
    private String agent_phone;
    
    private String billno;
    private String signature;
    private String ttl_pieces;
    private String ttl_weight;
    private String carrier;
    private String pay_status;
    
    private String csg_ref;
    private String csg_name;
    private String csg_add1;
    private String csg_add2;
    private String csg_city;
    private String csg_postal;
    private String csg_state;
    private String csg_phone;
    
    private String shp_ref;
    private String shp_name;
    private String shp_add1;
    private String shp_add2;
    private String shp_city;
    private String shp_postal;
    private String shp_state;
    private String shp_phone;
    
    private String deliveryagent_ref;
    private int position;
    
    private boolean selected;

	public String getAgent_ref() {
		return agent_ref;
	}

	public void setAgent_ref(String agent_ref) {
		this.agent_ref = agent_ref;
	}

	public String getAgent_num() {
		return agent_num;
	}

	public void setAgent_num(String agent_num) {
		this.agent_num = agent_num;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getAgent_add1() {
		return agent_add1;
	}

	public void setAgent_add1(String agent_add1) {
		this.agent_add1 = agent_add1;
	}

	public String getAgent_add2() {
		return agent_add2;
	}

	public void setAgent_add2(String agent_add2) {
		this.agent_add2 = agent_add2;
	}

	public String getAgent_city() {
		return agent_city;
	}

	public void setAgent_city(String agent_city) {
		this.agent_city = agent_city;
	}

	public String getAgent_postal() {
		return agent_postal;
	}

	public void setAgent_postal(String agent_postal) {
		this.agent_postal = agent_postal;
	}

	public String getAgent_state() {
		return agent_state;
	}

	public void setAgent_state(String agent_state) {
		this.agent_state = agent_state;
	}

	public String getAgent_phone() {
		return agent_phone;
	}

	public void setAgent_phone(String agent_phone) {
		this.agent_phone = agent_phone;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTtl_pieces() {
		return ttl_pieces;
	}

	public void setTtl_pieces(String ttl_pieces) {
		this.ttl_pieces = ttl_pieces;
	}

	public String getTtl_weight() {
		return ttl_weight;
	}

	public void setTtl_weight(String ttl_weight) {
		this.ttl_weight = ttl_weight;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getCsg_ref() {
		return csg_ref;
	}

	public void setCsg_ref(String csg_ref) {
		this.csg_ref = csg_ref;
	}

	public String getCsg_name() {
		return csg_name;
	}

	public void setCsg_name(String csg_name) {
		this.csg_name = csg_name;
	}

	public String getCsg_add1() {
		return csg_add1;
	}

	public void setCsg_add1(String csg_add1) {
		this.csg_add1 = csg_add1;
	}

	public String getCsg_add2() {
		return csg_add2;
	}

	public void setCsg_add2(String csg_add2) {
		this.csg_add2 = csg_add2;
	}

	public String getCsg_city() {
		return csg_city;
	}

	public void setCsg_city(String csg_city) {
		this.csg_city = csg_city;
	}

	public String getCsg_postal() {
		return csg_postal;
	}

	public void setCsg_postal(String csg_postal) {
		this.csg_postal = csg_postal;
	}

	public String getCsg_state() {
		return csg_state;
	}

	public void setCsg_state(String csg_state) {
		this.csg_state = csg_state;
	}

	public String getCsg_phone() {
		return csg_phone;
	}

	public void setCsg_phone(String csg_phone) {
		this.csg_phone = csg_phone;
	}

	public String getShp_ref() {
		return shp_ref;
	}

	public void setShp_ref(String shp_ref) {
		this.shp_ref = shp_ref;
	}

	public String getShp_name() {
		return shp_name;
	}

	public void setShp_name(String shp_name) {
		this.shp_name = shp_name;
	}

	public String getShp_add1() {
		return shp_add1;
	}

	public void setShp_add1(String shp_add1) {
		this.shp_add1 = shp_add1;
	}

	public String getShp_add2() {
		return shp_add2;
	}

	public void setShp_add2(String shp_add2) {
		this.shp_add2 = shp_add2;
	}

	public String getShp_city() {
		return shp_city;
	}

	public void setShp_city(String shp_city) {
		this.shp_city = shp_city;
	}

	public String getShp_postal() {
		return shp_postal;
	}

	public void setShp_postal(String shp_postal) {
		this.shp_postal = shp_postal;
	}

	public String getShp_state() {
		return shp_state;
	}

	public void setShp_state(String shp_state) {
		this.shp_state = shp_state;
	}

	public String getShp_phone() {
		return shp_phone;
	}

	public void setShp_phone(String shp_phone) {
		this.shp_phone = shp_phone;
	}

	public String getDeliveryagent_ref() {
		return deliveryagent_ref;
	}

	public void setDeliveryagent_ref(String deliveryagent_ref) {
		this.deliveryagent_ref = deliveryagent_ref;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
   
    
	
}
