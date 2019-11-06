package user.zc.providerhystrixtcc.dao;

import org.apache.ibatis.annotations.*;
import user.zc.apitcc.entities.Logs;

import java.util.List;

@Mapper
public interface LogsDao {
    @Select({"select id,type,tablename,time,json from logs order by time desc " })
    List<Logs> logslist();

    @Insert({"insert into logs(id,type,json,time,tablename) values(#{id},#{type},#{json},#{time},#{tablename})"})
    Boolean insert(Logs logs);

    @Delete({"delete logs where id  = #{id}"})
    Boolean delete(Long id);
}
