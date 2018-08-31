package com.aixuexi.pagedata.model.po;

import java.io.Serializable;
import java.util.Date;

public class DeckComposerPo implements Serializable {
    private static final long serialVersionUID = 6346584448821224038L;
    protected Integer id; //` int(11) NOT NULL AUTO_INCREMENT,
     protected Integer axxId; //` int(11) DEFAULT NULL,
     protected Integer deckId; //   ` int(11) DEFAULT NULL,
     protected String content; //   ` text,
     protected String settings; //  ` text,
     protected String desc; //  ` text,
     protected Date createdAt; //` datetime NOT NULL,
     protected Date updatedAt; //` datetime NOT NULL,
     protected String remark; //` text,
     protected Integer kind; //  ` int(11) DEFAULT '1',

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

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }
}
