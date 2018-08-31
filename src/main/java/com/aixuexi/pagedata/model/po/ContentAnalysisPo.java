package com.aixuexi.pagedata.model.po;

import java.io.Serializable;

public class ContentAnalysisPo implements Serializable {

    private static final long serialVersionUID = -1647437032304690105L;
    protected Integer id;
    protected Integer axxId;
    protected Integer contentType;
    protected Integer contentId;
    protected String pages;
    protected Integer kind;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAxxId() {
        return axxId;
    }

    public void setAxxId(Integer axxId) {
        this.axxId = axxId;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }
}
