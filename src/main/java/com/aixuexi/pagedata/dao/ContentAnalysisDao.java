package com.aixuexi.pagedata.dao;


import com.aixuexi.pagedata.model.bo.ContentAnalysisBo;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ContentAnalysisDao {

    @Resource
    private JdbcTemplate mysqlClient;

    /**
     * 根据axx_id和content_id删除页码统计记录
     * @param axxId
     * @param contentId
     * @return
     */
    public boolean deleteContentAnalysisByAxxIdContentId(Integer axxId, Integer contentId){
        String deleteSql = "delete from content_analysis where axx_id = ? and content_id = ?";
        int result = mysqlClient.update(deleteSql, new Object[]{axxId, contentId});
        return result > 0;
    }

    /**
     * 根据axx_id删除页码统计记录
     * @param axxId
     * @return
     */
    public boolean deleteContentAnalysisByAxxIdAndKind(Integer axxId, Integer kind){
        String deleteSql = "delete from content_analysis where axx_id = ? and kind = ?";
        int result = mysqlClient.update(deleteSql, new Object[]{axxId, kind});
        return result > 0;
    }

    /**
     * 根据查询条件判断是否存在ContentAnalysis数据
     * @param axxId
     * @param kind
     * @return
     */
    public boolean existContentAnalysisByAxxIdAndKind(Integer axxId, Integer kind){
        String sql = "select count(*) as count from content_analysis where axx_id = ? and kind = ?";
        List<Map<String, Object>> list = mysqlClient.queryForList(sql, new Object[]{axxId, kind});
        if (null != list && list.size() == 1){
            return !list.get(0).get("count").toString().equals("0");
        } else {
            return false;
        }
    }

    /**
     * 插入ContentAnalysis对象
     * @param contentAnalysisBo
     * @return
     */
    public int insertContentAnalysis(ContentAnalysisBo contentAnalysisBo){
        String insertSql = "INSERT INTO `content_analysis` (`axx_id`, `content_type`, `content_id`, `pages`, `kind`) VALUES (?, ?, ?, ?, ?)";
        return mysqlClient.update(insertSql, new Object[]{contentAnalysisBo.getAxxId(), contentAnalysisBo.getContentType(), contentAnalysisBo.getContentId(), contentAnalysisBo.getPages(), contentAnalysisBo.getKind()});
    }

    /**
     * 插入ContentAnalysis对象集合
     * @param contentAnalysisBoList
     * @return
     */
    public int insertContentAnalysis(List<ContentAnalysisBo> contentAnalysisBoList){
        int rowCount = 0;
        for (ContentAnalysisBo contentAnalysisBo : contentAnalysisBoList){
            rowCount += insertContentAnalysis(contentAnalysisBo);
        }
        return rowCount;
    }

    /**
     * 批量插入ContentAnalysis
     * @param contentAnalysisBoList
     */
    public void batchInsertContentAnalysi(List<ContentAnalysisBo> contentAnalysisBoList){
        String insertSql = "INSERT INTO `content_analysis` (`axx_id`, `content_type`, `content_id`, `pages`, `kind`) VALUES (?, ?, ?, ?, ?)";
        mysqlClient.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                ContentAnalysisBo contentAnalysisBo = contentAnalysisBoList.get(i);
                preparedStatement.setInt(1, contentAnalysisBo.getAxxId());
                preparedStatement.setInt(2, contentAnalysisBo.getContentType());
                preparedStatement.setInt(3, contentAnalysisBo.getContentId());
                preparedStatement.setString(4, contentAnalysisBo.getPages());
                preparedStatement.setInt(5, contentAnalysisBo.getKind());
            }

            @Override
            public int getBatchSize() {
                return contentAnalysisBoList.size();
            }
        });
    }

}
