package com.example.json.foogt.entity;

import java.util.ArrayList;
import java.util.List;

public class msgRelation1 {
	private List<msgRelation>list=new ArrayList<>();

	public List<msgRelation> getList() {
		return list;
	}

	public void setList(List<msgRelation> list) {
		this.list = list;
	}

	public msgRelation1(List<msgRelation> list) {
		super();
		this.list = list;
	}
	
	
}
