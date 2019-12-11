package com.nitish.githubapi.beans;

import com.google.gson.annotations.SerializedName;

public class Commit{

	@SerializedName("comment_count")
	private int commentCount;

	@SerializedName("committer")
	private Committer committer;

	@SerializedName("author")
	private Author author;

	@SerializedName("tree")
	private Tree tree;

	@SerializedName("message")
	private String message;

	@SerializedName("url")
	private String url;

	@SerializedName("verification")
	private Verification verification;

	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}

	public int getCommentCount(){
		return commentCount;
	}

	public void setCommitter(Committer committer){
		this.committer = committer;
	}

	public Committer getCommitter(){
		return committer;
	}

	public void setAuthor(Author author){
		this.author = author;
	}

	public Author getAuthor(){
		return author;
	}

	public void setTree(Tree tree){
		this.tree = tree;
	}

	public Tree getTree(){
		return tree;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setVerification(Verification verification){
		this.verification = verification;
	}

	public Verification getVerification(){
		return verification;
	}

	@Override
 	public String toString(){
		return 
			"Commit{" + 
			"comment_count = '" + commentCount + '\'' + 
			",committer = '" + committer + '\'' + 
			",author = '" + author + '\'' + 
			",tree = '" + tree + '\'' + 
			",message = '" + message + '\'' + 
			",url = '" + url + '\'' + 
			",verification = '" + verification + '\'' + 
			"}";
		}
}