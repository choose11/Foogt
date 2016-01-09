package entity;

public class msgRelation {
	private String name1;
	private String name2;
	private String content1;
	private String content2;
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	public msgRelation(String name1, String name2, String content1,
			String content2) {
		super();
		this.name1 = name1;
		this.name2 = name2;
		this.content1 = content1;
		this.content2 = content2;
	}
	public msgRelation() {
		super();
	}
	
	
	
	
	
}
