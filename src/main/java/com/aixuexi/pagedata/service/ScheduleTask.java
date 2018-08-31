package com.aixuexi.pagedata.service;

import com.aixuexi.pagedata.dao.DeckComposerDao;
import com.aixuexi.pagedata.dao.SlideDao;
import com.aixuexi.pagedata.model.bo.DeckComposerBo;
import com.aixuexi.pagedata.model.bo.SlideBo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    private ContentAnalysisService contentAnalysisService;

    @Autowired
    private SlideDao slideDao;

    @Autowired
    private DeckComposerDao deckComposerDao;

    private static Integer startAt = 0;

    private static final Integer size = 50;

    private static boolean flag = false;

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void processData(){
        if (!flag){
            flag = true;
            try {
                System.out.println("LoopRecord to process page info is starting. Please wait ...");
                log.info("Start Process Data");
                loopRecord(startAt);
            } catch (Exception e){
                e.printStackTrace();
                log.error("loopRecord failed", e);
                System.out.println("LoopRecord to process page faces some errors. Please check the error logs");
            } finally {
                log.debug("loopRecord finished");
                System.out.println("LoopRecord to process page info is finished. Please stop this process.");
                System.exit(0);
            }
        }

    }

    private void loopRecord(Integer index){

        List<DeckComposerBo> list = deckComposerDao.fetchDeckComposer(index, size);
//        List<DeckComposerBo> list = deckComposerDao.testFetchDeckComposer(99999);

        Integer currentLastIndex = index;
        for (DeckComposerBo deckComposerBo : list){
            currentLastIndex = deckComposerBo.getId();
            SlideBo slideBo = slideDao.findSlideByDeckId(deckComposerBo.getDeckId());
            if (null == slideBo || null == slideBo.getBody()){
                continue;
            }
            DeckComposerBo.ContentObj contentObj = processParts(deckComposerBo);
            if (null == contentObj){
                continue;
            }
            Document document = Jsoup.parse(slideBo.getBody(), "", Parser.xmlParser());
            contentAnalysisService.fetchContentPageInfo(document, contentObj);
        }

        if (index.equals(currentLastIndex) || index > currentLastIndex){
            return;
        } else {
            loopRecord(currentLastIndex);
        }
    }

    private DeckComposerBo.ContentObj processParts(DeckComposerBo deckComposerBo) {
        try {
            DeckComposerBo.ContentObj contentObj = JSON.parseObject(deckComposerBo.getContent(), DeckComposerBo.ContentObj.class);
            LinkedList<DeckComposerBo.Part> partList = new LinkedList<>();
            IntStream.range(0,contentObj.getParts().size()).forEachOrdered(index -> {
                DeckComposerBo.Part part = contentObj.getParts().get(index);
                if (part.getContentType() == 10) {
                    if (null != part.getSubParts() && part.getSubParts().size() > 0) {
                        part.getSubParts().toJavaList(DeckComposerBo.Part.class).stream().forEachOrdered(sub -> {
                            if (!StringUtils.isEmpty(part.getName())) {
                                sub.setParentName(part.getName());
                            }
                            sub.setParentIndex(index);
                            partList.add(sub);
                        });
                    } else {
                        part.setParentIndex(index);
                        if (null != part.getSubParts() && part.getSubParts().size() > 0) {
                            partList.addAll(part.getSubParts().toJavaList(DeckComposerBo.Part.class));
                        }
                    }
                } else {
                    part.setParentIndex(index);
                    partList.add(part);
                }
            });
            contentObj.setParts(partList);
            return contentObj;
        } catch (Exception e){
            e.printStackTrace();
            log.error("processParts Failed", e);
            return null;
        }
    }
}
