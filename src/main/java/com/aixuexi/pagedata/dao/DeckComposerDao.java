package com.aixuexi.pagedata.dao;

import com.aixuexi.pagedata.model.bo.DeckComposerBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class DeckComposerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DeckComposerBo> fetchDeckComposer(Integer startAt, Integer limitSize){
        String sql = "select id, deck_id, content, kind from deck_composers where id > ? order by id limit ?";
        List<DeckComposerBo> list = jdbcTemplate.query(sql, new Object[]{startAt, limitSize}, new RowMapper<DeckComposerBo>() {
            @Override
            public DeckComposerBo mapRow(ResultSet resultSet, int i) throws SQLException {
                DeckComposerBo deckComposerBo = new DeckComposerBo();
                deckComposerBo.setId(resultSet.getInt("id"));
                deckComposerBo.setDeckId(resultSet.getInt("deck_id"));
                deckComposerBo.setContent(resultSet.getString("content"));
                deckComposerBo.setKind(resultSet.getInt("kind"));
                return deckComposerBo;
            }
        });
        return list;
    }

    public List<DeckComposerBo> testFetchDeckComposer(Integer id){
        String sql = "select id, deck_id, content, kind from deck_composers where id = ?";
        List<DeckComposerBo> list = jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            DeckComposerBo deckComposerBo = new DeckComposerBo();
            deckComposerBo.setId(resultSet.getInt("id"));
            deckComposerBo.setDeckId(resultSet.getInt("deck_id"));
            deckComposerBo.setContent(resultSet.getString("content"));
            deckComposerBo.setKind(resultSet.getInt("kind"));
            return deckComposerBo;
        });
        return list;
    }

}
