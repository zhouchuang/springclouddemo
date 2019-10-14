package user.zc.apitcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Logs {
    private Long id;
    private String type;
    private String json;
    private Date time;
    private String tablename;

    public Logs(Long id, String type, String json, Date time, String tablename) {
        this.id = id;
        this.type = type;
        this.json = json;
        this.time = time;
        this.tablename = tablename;
    }
}
