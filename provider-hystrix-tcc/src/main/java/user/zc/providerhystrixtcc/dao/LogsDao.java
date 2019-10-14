package user.zc.providerhystrixtcc.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import user.zc.apitcc.entities.Logs;

import java.util.List;

@Mapper
public interface LogsDao {
    @Select({"select * from logs" })
    List<Logs> logslist();

    @Insert({"insert into logs(id,type,json,time,tablename) values(#{id},#{type},#{json},#{time},#{tablename})"})
    Boolean insert(Logs logs);
}
