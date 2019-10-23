package user.zc.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Ticket {
    private Long id;
    private String ip;
    private Integer no;
    private Date time;

    public Ticket(Long id, String ip,Integer no ,Date time ){
        this.id  = id;
        this.ip = ip;
        this.no = no;
        this.time = time;
    }
}
