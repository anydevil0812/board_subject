package dto;

import java.time.LocalDateTime;

public class BoardDTO {
	
	private int postNum;
	
	private String title;

	private String name;
	
	private LocalDateTime date;
	
	private String content;

	private int views;
	
	private Integer parentNum;
	
	private int depth;

	public int getPostNum() {
		return postNum;
	}

	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
	
	public Integer getParentNum() {
		return parentNum;
	}

	public void setParentNum(Integer parentNum) {
		this.parentNum = parentNum;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}
