package com.nitish.githubapi.beans;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CommitsApiResponse{

	@SerializedName("committer")
	private Committer committer;

	@SerializedName("author")
	private Author author;

	@SerializedName("html_url")
	private String htmlUrl;

	@SerializedName("commit")
	private Commit commit;

	@SerializedName("comments_url")
	private String commentsUrl;

	@SerializedName("sha")
	private String sha;

	@SerializedName("url")
	private String url;

	@SerializedName("node_id")
	private String nodeId;

	@SerializedName("parents")
	private List<ParentsItem> parents;

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

	public void setHtmlUrl(String htmlUrl){
		this.htmlUrl = htmlUrl;
	}

	public String getHtmlUrl(){
		return htmlUrl;
	}

	public void setCommit(Commit commit){
		this.commit = commit;
	}

	public Commit getCommit(){
		return commit;
	}

	public void setCommentsUrl(String commentsUrl){
		this.commentsUrl = commentsUrl;
	}

	public String getCommentsUrl(){
		return commentsUrl;
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

	public void setNodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getNodeId(){
		return nodeId;
	}

	public void setParents(List<ParentsItem> parents){
		this.parents = parents;
	}

	public List<ParentsItem> getParents(){
		return parents;
	}

	@Override
 	public String toString(){
		return 
			"CommitsApiResponse{" + 
			"committer = '" + committer + '\'' + 
			",author = '" + author + '\'' + 
			",html_url = '" + htmlUrl + '\'' + 
			",commit = '" + commit + '\'' + 
			",comments_url = '" + commentsUrl + '\'' + 
			",sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			",node_id = '" + nodeId + '\'' + 
			",parents = '" + parents + '\'' + 
			"}";
		}
}