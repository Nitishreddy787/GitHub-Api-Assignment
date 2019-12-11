package com.nitish.githubapi.beans;

import com.google.gson.annotations.SerializedName;

public class Tree{

	@SerializedName("sha")
	private String sha;

	@SerializedName("url")
	private String url;

	public void setSha(String sha){
		this.sha = sha;
	}

	public String getSha(){
		return sha;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"Tree{" + 
			"sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}