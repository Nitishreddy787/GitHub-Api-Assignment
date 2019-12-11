package com.nitish.githubapi.beans;

import com.google.gson.annotations.SerializedName;

public class ParentsItem{

	@SerializedName("html_url")
	private String htmlUrl;

	@SerializedName("sha")
	private String sha;

	@SerializedName("url")
	private String url;

	public void setHtmlUrl(String htmlUrl){
		this.htmlUrl = htmlUrl;
	}

	public String getHtmlUrl(){
		return htmlUrl;
	}

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
			"ParentsItem{" + 
			"html_url = '" + htmlUrl + '\'' + 
			",sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}