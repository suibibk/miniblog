package com.suibibk.entity;

public class Content {
    private String id;//主键ID,雪花算法生成',
    private String content;//内容（最大65535）',
    private String viewcontent;//内容（最大65535）',//放转换后的数据
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getViewcontent() {
		return viewcontent;
	}

	public void setViewcontent(String viewcontent) {
		this.viewcontent = viewcontent;
	}

	public Content() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Content(String id, String content, String viewcontent) {
		super();
		this.id = id;
		this.content = content;
		this.viewcontent = viewcontent;
	}

	@Override
	public String toString() {
		return "TContent [id=" + id + ", content=" + content + ", viewcontent=" + viewcontent + "]";
	}
	
}
