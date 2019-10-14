package user.zc.apitcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logs {
    private Long id;
    private String type;
    private String json;
    private Date time;
    private String tablename;
}
