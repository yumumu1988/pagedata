package com.aixuexi.pagedata.dao;

import com.aixuexi.pagedata.model.bo.SlideBo;
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
public class SlideDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public SlideBo findSlideByDeckId(Integer deckId){
        String sql = "select id, body, deck_id from slides where deck_id = ? limit 1";
        List<SlideBo> list = jdbcTemplate.query(sql, new Object[]{deckId}, new RowMapper<SlideBo>() {
            @Override
            public SlideBo mapRow(ResultSet resultSet, int i) throws SQLException {
                SlideBo slideBo = new SlideBo();
                slideBo.setId(resultSet.getInt("id"));
                slideBo.setBody(resultSet.getString("body"));
                slideBo.setDeckId(resultSet.getInt("deck_id"));
                return slideBo;
            }
        });

        if (null == list || list.size() != 1){
            return null;
        }

        return list.get(0);

    }

}
