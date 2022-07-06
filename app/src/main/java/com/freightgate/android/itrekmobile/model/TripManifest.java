package com.freightgate.android.itrekmobile.model;

import java.util.List;

public class TripManifest implements java.io.Serializable {

	private static final long serialVersionUID = 7283493326644552377L;
	
	private String tip_name;
	private List<Shipment> shipments;
	
	public String getTip_name() {
		return tip_name;
	}
	public void setTip_name(String tip_name) {
		this.tip_name = tip_name;
	}
	public List<Shipment> getShipments() {
		return shipments;
	}
	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}
	
	

}
