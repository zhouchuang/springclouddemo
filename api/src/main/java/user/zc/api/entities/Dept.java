package user.zc.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Dept {
    private Long id;
    private String name;
    private String code;

    public Dept(Long id,String name){
        this.id  = id;
        this.name = name;
    }
}
