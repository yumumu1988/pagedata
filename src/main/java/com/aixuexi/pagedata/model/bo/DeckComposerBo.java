package com.aixuexi.pagedata.model.bo;

import com.aixuexi.pagedata.model.po.DeckComposerPo;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;

@Data
public class DeckComposerBo extends DeckComposerPo {
    private static final long serialVersionUID = -5464036625325449212L;
    /**
     * 合成结果
     */
    private Integer resultStatus;

    @Data
    public static class Part{
        private Integer id;
        private Integer contentType;
        private JSONArray subParts;
        private String name;
        private String uri;
        private String orderLabel;
        private String labelIndex;
        private String topicBelong;
        private Integer topicType;
        private String resourceUrl;
        private Integer parentIndex;
        private String parentName;
    }

    @Data
    public static class ContentObj{
        private Integer id;
        private String logo;
        private Integer theme;
        private Integer kind;
        private String name;
        private List<Part> parts;
    }
}
