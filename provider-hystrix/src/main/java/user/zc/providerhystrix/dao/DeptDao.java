package user.zc.providerhystrix.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import user.zc.api.entities.Dept;

import java.util.List;

@Mapper
public interface DeptDao {
    @Select({"select name from dept" })
    List<String> deptlist();

    @Update({"update dept set name = #{name} where id = #{id}"})
    Integer update(Dept dept);
}
