package user.zc.provider01.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptDao {
    @Select({"select name from dept" })
    List<String> deptlist();
}
