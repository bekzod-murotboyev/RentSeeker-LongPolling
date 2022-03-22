package uz.pdp.rentseekerlongpolling.model.locationModels;


import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class LocationsItem implements Serializable {

	private boolean dragPoint;

	private DisplayLatLng displayLatLng;

	private String adminArea4;

	private String unknownInput;

	private String adminArea5;

	private String adminArea6;

	private String postalCode;

	private String adminArea1;

	private String adminArea3;

	private String sideOfStreet;

	private String type;

	private String adminArea6Type;

	private String geocodeQualityCode;

	private String adminArea4Type;

	private String linkId;

	private String street;

	private String adminArea5Type;

	private String mapUrl;

	private String geocodeQuality;

	private String adminArea1Type;

	private String adminArea3Type;

	private LatLng latLng;

	public void setDragPoint(boolean dragPoint){
		this.dragPoint = dragPoint;
	}

	public boolean isDragPoint(){
		return dragPoint;
	}

	public void setDisplayLatLng(DisplayLatLng displayLatLng){
		this.displayLatLng = displayLatLng;
	}

	public DisplayLatLng getDisplayLatLng(){
		return displayLatLng;
	}

	public void setAdminArea4(String adminArea4){
		this.adminArea4 = adminArea4;
	}

	public String getAdminArea4(){
		return adminArea4;
	}

	public void setUnknownInput(String unknownInput){
		this.unknownInput = unknownInput;
	}

	public String getUnknownInput(){
		return unknownInput;
	}

	public void setAdminArea5(String adminArea5){
		this.adminArea5 = adminArea5;
	}

	public String getAdminArea5(){
		return adminArea5;
	}

	public void setAdminArea6(String adminArea6){
		this.adminArea6 = adminArea6;
	}

	public String getAdminArea6(){
		return adminArea6;
	}

	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public void setAdminArea1(String adminArea1){
		this.adminArea1 = adminArea1;
	}

	public String getAdminArea1(){
		return adminArea1;
	}

	public void setAdminArea3(String adminArea3){
		this.adminArea3 = adminArea3;
	}

	public String getAdminArea3(){
		return adminArea3;
	}

	public void setSideOfStreet(String sideOfStreet){
		this.sideOfStreet = sideOfStreet;
	}

	public String getSideOfStreet(){
		return sideOfStreet;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setAdminArea6Type(String adminArea6Type){
		this.adminArea6Type = adminArea6Type;
	}

	public String getAdminArea6Type(){
		return adminArea6Type;
	}

	public void setGeocodeQualityCode(String geocodeQualityCode){
		this.geocodeQualityCode = geocodeQualityCode;
	}

	public String getGeocodeQualityCode(){
		return geocodeQualityCode;
	}

	public void setAdminArea4Type(String adminArea4Type){
		this.adminArea4Type = adminArea4Type;
	}

	public String getAdminArea4Type(){
		return adminArea4Type;
	}

	public void setLinkId(String linkId){
		this.linkId = linkId;
	}

	public String getLinkId(){
		return linkId;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		return street;
	}

	public void setAdminArea5Type(String adminArea5Type){
		this.adminArea5Type = adminArea5Type;
	}

	public String getAdminArea5Type(){
		return adminArea5Type;
	}

	public void setMapUrl(String mapUrl){
		this.mapUrl = mapUrl;
	}

	public String getMapUrl(){
		return mapUrl;
	}

	public void setGeocodeQuality(String geocodeQuality){
		this.geocodeQuality = geocodeQuality;
	}

	public String getGeocodeQuality(){
		return geocodeQuality;
	}

	public void setAdminArea1Type(String adminArea1Type){
		this.adminArea1Type = adminArea1Type;
	}

	public String getAdminArea1Type(){
		return adminArea1Type;
	}

	public void setAdminArea3Type(String adminArea3Type){
		this.adminArea3Type = adminArea3Type;
	}

	public String getAdminArea3Type(){
		return adminArea3Type;
	}

	public void setLatLng(LatLng latLng){
		this.latLng = latLng;
	}

	public LatLng getLatLng(){
		return latLng;
	}

	@Override
 	public String toString(){
		return 
			"LocationsItem{" + 
			"dragPoint = '" + dragPoint + '\'' + 
			",displayLatLng = '" + displayLatLng + '\'' + 
			",adminArea4 = '" + adminArea4 + '\'' + 
			",unknownInput = '" + unknownInput + '\'' + 
			",adminArea5 = '" + adminArea5 + '\'' + 
			",adminArea6 = '" + adminArea6 + '\'' + 
			",postalCode = '" + postalCode + '\'' + 
			",adminArea1 = '" + adminArea1 + '\'' + 
			",adminArea3 = '" + adminArea3 + '\'' + 
			",sideOfStreet = '" + sideOfStreet + '\'' + 
			",type = '" + type + '\'' + 
			",adminArea6Type = '" + adminArea6Type + '\'' + 
			",geocodeQualityCode = '" + geocodeQualityCode + '\'' + 
			",adminArea4Type = '" + adminArea4Type + '\'' + 
			",linkId = '" + linkId + '\'' + 
			",street = '" + street + '\'' + 
			",adminArea5Type = '" + adminArea5Type + '\'' + 
			",mapUrl = '" + mapUrl + '\'' + 
			",geocodeQuality = '" + geocodeQuality + '\'' + 
			",adminArea1Type = '" + adminArea1Type + '\'' + 
			",adminArea3Type = '" + adminArea3Type + '\'' + 
			",latLng = '" + latLng + '\'' + 
			"}";
		}
}