package cc.kinami.template.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DayMemo {
    private Integer ID;
    private String headline;
    private String detail;
}
