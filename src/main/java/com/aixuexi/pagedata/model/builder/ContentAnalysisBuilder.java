package com.aixuexi.pagedata.model.builder;


import com.aixuexi.pagedata.model.bo.ContentAnalysisBo;

import java.io.Serializable;

public class ContentAnalysisBuilder implements Serializable {
    private static final long serialVersionUID = -3001760642735974579L;

    private ContentAnalysisBo contentAnalysisBo;

    private ContentAnalysisBo getContentAnalysisBo() {
        return contentAnalysisBo;
    }

    private void setContentAnalysisBo(ContentAnalysisBo contentAnalysisBo) {
        this.contentAnalysisBo = contentAnalysisBo;
    }

    public static ContentAnalysisBuilder createBuilder(){
        ContentAnalysisBuilder contentAnalysisBuilder = new ContentAnalysisBuilder();
        ContentAnalysisBo contentAnalysisBo = new ContentAnalysisBo();
        contentAnalysisBuilder.setContentAnalysisBo(contentAnalysisBo);
        return contentAnalysisBuilder;
    }

    public ContentAnalysisBo builder(){
        return this.getContentAnalysisBo();
    }

    public ContentAnalysisBuilder buildAxxId(Integer axxId){
        this.getContentAnalysisBo().setAxxId(axxId);
        return this;
    }

    public ContentAnalysisBuilder buildContentId(Integer contentId){
        this.getContentAnalysisBo().setContentId(contentId);
        return this;
    }

    public ContentAnalysisBuilder buildContentType(Integer contentType){
        this.getContentAnalysisBo().setContentType(contentType);
        return this;
    }

    public ContentAnalysisBuilder buildKind(Integer kind){
        this.getContentAnalysisBo().setKind(kind);
        return this;
    }

    public ContentAnalysisBuilder buildPages(String pages){
        this.getContentAnalysisBo().setPages(pages);
        return this;
    }
}
