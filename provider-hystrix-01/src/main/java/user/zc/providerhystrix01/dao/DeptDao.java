package user.zc.providerhystrix01.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import user.zc.api.entities.Dept;
import user.zc.api.entities.Ticket;

import java.util.List;

@Mapper
public interface DeptDao {
    @Select({"select name from dept" })
    List<String> deptlist();

    @Update({"update dept set name = #{name} where id = #{id}"})
    Integer update(Dept dept);


    @Insert({"insert into ticket(id,ip,no,time) values(#{id},#{ip},#{no},#{time})"})
    Integer ticketSave(Ticket ticket);
}
