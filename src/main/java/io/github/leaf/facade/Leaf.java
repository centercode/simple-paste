package io.github.leaf.facade;

import java.util.Date;


public class Leaf {

    private String id;

    private Date createTime;

    private Date endTime;

    private String content;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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

    @Override
    public String toString() {
        return "Leaf{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", content='" + content + '\'' +
                '}';
    }
}