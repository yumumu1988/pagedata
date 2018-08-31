package com.aixuexi.pagedata.model.po;

import java.io.Serializable;
import java.util.Date;

public class SlidePo implements Serializable {
    private static final long serialVersionUID = 1622413670804823185L;
    protected Integer id;     //` int(11) NOT NULL AUTO_INCREMENT,
     protected Integer deckId;        //` int(11) DEFAULT NULL,
     protected String body;       //` longtext COLLATE utf8mb4_bin,
     protected Date createdAt;     //` datetime DEFAULT NULL,
     protected Date updatedAt;     //` datetime DEFAULT NULL,
     protected Date deletedAt;     //` datetime DEFAULT NULL,
     protected String cbody;      //` longtext COLLATE utf8mb4_bin,
     protected String bodyText;      //` mediumtext COLLATE utf8mb4_bin,
    @Deprecated //线上数据库没有这个字段
     protected String classText;     //` text COLLATE utf8mb4_bin,

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCbody() {
        return cbody;
    }

    public void setCbody(String cbody) {
        this.cbody = cbody;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    @Deprecated
    public String getClassText() {
        return classText;
    }

    @Deprecated
    public void setClassText(String classText) {
        this.classText = classText;
    }
}
