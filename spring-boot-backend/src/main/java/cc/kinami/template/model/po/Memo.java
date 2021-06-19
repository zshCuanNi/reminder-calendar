package cc.kinami.template.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Memo {
    private Integer ID;
    private String deadline;
    private String headline;
    private String detail;
}
