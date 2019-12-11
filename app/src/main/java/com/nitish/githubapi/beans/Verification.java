package com.nitish.githubapi.beans;

import com.google.gson.annotations.SerializedName;

public class Verification{

	@SerializedName("reason")
	private String reason;

	@SerializedName("signature")
	private Object signature;

	@SerializedName("payload")
	private Object payload;

	@SerializedName("verified")
	private boolean verified;

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setSignature(Object signature){
		this.signature = signature;
	}

	public Object getSignature(){
		return signature;
	}

	public void setPayload(Object payload){
		this.payload = payload;
	}

	public Object getPayload(){
		return payload;
	}

	public void setVerified(boolean verified){
		this.verified = verified;
	}

	public boolean isVerified(){
		return verified;
	}

	@Override
 	public String toString(){
		return 
			"Verification{" + 
			"reason = '" + reason + '\'' + 
			",signature = '" + signature + '\'' + 
			",payload = '" + payload + '\'' + 
			",verified = '" + verified + '\'' + 
			"}";
		}
}