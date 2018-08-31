package com.aixuexi.pagedata.service;

import com.aixuexi.pagedata.dao.ContentAnalysisDao;
import com.aixuexi.pagedata.model.bo.ContentAnalysisBo;
import com.aixuexi.pagedata.model.bo.DeckComposerBo;
import com.aixuexi.pagedata.model.builder.ContentAnalysisBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class ContentAnalysisService {

    @Resource
    private ContentAnalysisDao contentAnalysisDao;

    private static final Set<String> contentTypeStringSet = new HashSet<>();
    private static final Set<Integer> contentTypeIntegerSet = new HashSet<>();

    static {
//        题目课件
        contentTypeStringSet.add("1");
        contentTypeIntegerSet.add(1);

//        ISpring
        contentTypeStringSet.add("2");
        contentTypeIntegerSet.add(2);

//        2013视频
        contentTypeStringSet.add("7");
        contentTypeIntegerSet.add(7);

//        课件碎片
        contentTypeStringSet.add("9");
        contentTypeIntegerSet.add(9);
    }

    /**
     * 获取课件碎片、题目课件、视频、ISPring页码数据
     * @param document
     * @param contentObj
     */
    @Transactional
    public void fetchContentPageInfo(Document document, DeckComposerBo.ContentObj contentObj){
        Integer axxId = contentObj.getId();
        Integer kind = contentObj.getKind();

        if (null == axxId || null == kind){
            log.warn("axxId: " + axxId + "; kind: " + kind + ". Some values are null");
            return;
        }

//        获取逻辑
        Map<String, ContentInfoObject> contentMap = getContentMap(contentObj);
        Elements slideElements = document.select("li[data-slide-number]");
        int slideNumber = 1;
        for (Element element : slideElements){
            Elements slide = element.select(".slide");
            if (null != slide && slide.size() > 0) {
//                从details里解析出contentID和contentType
                String details = slide.attr("details");
                String contentType = fetchValueFromDetails(details, "contentType");
                if (null != contentType && contentTypeStringSet.contains(contentType)){
                    String contentId = fetchValueFromDetails(details, "contentId");
                    String key = contentId + "_" + contentType;
                    ContentInfoObject contentInfoObject = contentMap.get(key);
                    if (null != contentInfoObject){
                        contentInfoObject.addPage(slideNumber);
                    }
                }
            }
            slideNumber++;
        }

        List<ContentAnalysisBo> contentAnalysisBoList = convertToContentAnalysisBoList(contentMap, axxId, kind);

        if (contentAnalysisDao.existContentAnalysisByAxxIdAndKind(axxId, kind)){
            contentAnalysisDao.deleteContentAnalysisByAxxIdAndKind(axxId, kind);
        }
        contentAnalysisDao.batchInsertContentAnalysi(contentAnalysisBoList);
    }

    /**
     * 从details中解析数据
     * @param details
     * @param key
     * @return
     */
    private String fetchValueFromDetails(String details, String key){
        if (null == details){
            log.warn("details is null");
            return null;
        }
        String value = null;
        String[] itemArray = details.replace("{", "").replace("}", "").replace("\"", "").replace("&quot;", "").split(",");
        for (int i = 0; i < itemArray.length; i++){
            String item = itemArray[i];
            if (item.contains(key)){
                String[] valueArray = item.split(":");
                value = valueArray[valueArray.length - 1];
                break;
            }
        }
        log.debug("fetchValueFromDetails-> Key: " + key + " Value: " + value);
        return value;
    }

    /**
     * 从content中提取需要统计的内容类型和个数
     * @param contentObj
     * @return
     */
    private Map<String, ContentInfoObject> getContentMap(DeckComposerBo.ContentObj contentObj){
        Map<String, ContentInfoObject> contentMap = new LinkedHashMap<>();
//        Map<String, ContentInfoObject> contentMap = new HashMap<>();
        for (DeckComposerBo.Part part : contentObj.getParts()){
            loopPartToFetchContent(part, contentMap);
        }
        return contentMap;
    }

    /**
     * 遍历subParts提取需要统计的内容类型和个数
     * @param part
     * @param contentMap
     */
    private void loopPartToFetchContent(DeckComposerBo.Part part, Map<String, ContentInfoObject> contentMap) {
        if (null != part.getSubParts()){
            loopSubPartsToFetchContent(part.getSubParts(), contentMap);
        } else {
//          检测part是否符合提取条件
            if (contentTypeIntegerSet.contains(part.getContentType())){
                String key = part.getId() + "_" + part.getContentType();
                ContentInfoObject contentInfoObject = contentMap.get(key);
                if (null != contentInfoObject){
                    contentInfoObject.count++;
                } else {
                    contentInfoObject = new ContentInfoObject(part.getId(), part.getContentType());
                    contentMap.put(key, contentInfoObject);
                    log.debug("ContentID: " + part.getId());
                }
            }
        }

    }

    /** 递归subParts
     *  @param jsonArray
     * @param contentMap
     */
    private void loopSubPartsToFetchContent(JSONArray jsonArray, Map<String, ContentInfoObject> contentMap) {
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject) object;
            JSONArray subParts = jsonObject.getJSONArray("subParts");
            if (null != subParts){
                loopSubPartsToFetchContent(subParts, contentMap);
            } else {
//              检测此content是否符合提取条件
                Integer contentType = jsonObject.getInteger("contentType");
                Integer contentId = jsonObject.getInteger("id");
                if (null != contentType && null != contentId && contentTypeIntegerSet.contains(contentType)){
                    String key = contentId + "_" + contentType;
                    ContentInfoObject contentInfoObject = contentMap.get(key);
                    if (null != contentInfoObject){
                        contentInfoObject.count++;
                    } else {
                        contentInfoObject = new ContentInfoObject(contentId, contentType);
                        contentMap.put(key, contentInfoObject);
                        log.debug("ContentID: " + contentId);
                    }
                }
            }
        }
    }

    /**
     * 转化得到Bo对象用于持久化
     * @param contentMap
     * @return
     */
    private List<ContentAnalysisBo> convertToContentAnalysisBoList(Map<String, ContentInfoObject> contentMap, Integer axxId, Integer kind){
        List<ContentAnalysisBo> contentAnalysisBoList = new ArrayList<>();
        contentMap.forEach(new BiConsumer<String, ContentInfoObject>() {
            @Override
            public void accept(String s, ContentInfoObject contentInfoObject) {
                contentInfoObject.sortPages();
                for (int i = 0; i < contentInfoObject.count; i++){
                    ContentAnalysisBuilder builder = ContentAnalysisBuilder.createBuilder();
                    String pages = contentInfoObject.getPages(i);
                    if (StringUtils.isEmpty(pages)){
                        log.warn("No slide for contentId: " + contentInfoObject.contentId + " contentType: " + contentInfoObject.contentType + " axxId: " + axxId + " kind: " + kind);
                    }
                    ContentAnalysisBo contentAnalysisBo = builder.buildAxxId(axxId)
                            .buildKind(kind)
                            .buildContentId(contentInfoObject.contentId)
                            .buildContentType(contentInfoObject.contentType)
                            .buildPages(pages)
                            .builder();
                    contentAnalysisBoList.add(contentAnalysisBo);
                }
            }
        });
        return contentAnalysisBoList;
    }


    class ContentInfoObject {
        private Integer contentId;
        private Integer contentType;
        private Integer count;
        private List<Integer> pages;

        ContentInfoObject(Integer contentId, Integer contentType){
            this.contentId = contentId;
            this.contentType = contentType;
            this.count = 1;
            this.pages = new ArrayList<>();
        }

        private String getPages(Integer index){
            Integer unit = pages.size() / count;
            if (unit <= 0){
                return "";
            }
            List<String> pageList = new ArrayList<>();
            for (int i = index * unit; i < (index + 1) * unit; i++){
                pageList.add(pages.get(i).toString());
            }
            return String.join(",", pageList);
        }

        void sortPages(){
            Collections.sort(this.pages);
        }

        private void addPage(Integer page){
            this.pages.add(page);
        }

        public String getKey(){
            return this.contentId + "_" + this.contentType;
        }
    }
}
