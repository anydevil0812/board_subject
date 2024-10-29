package dto;

import java.time.LocalDateTime;

public class BoardDTO {
	
	int num;
	
	String title;

	String name;
	
	LocalDateTime date;
	
	String content;

	String views;
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
	
	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}
	
}
