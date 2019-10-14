package user.zc.providerhystrixtcc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import user.zc.apitcc.entities.Logs;

import java.util.List;

@Mapper
public interface LogsDao {
    @Select({"select * from logs" })
    List<Logs> logslist();

    @Update({"insert logs values(#{id},#{type},#{json},#{time},#{tablename})"})
    Integer insert(Logs logs);
}
