package cc.kinami.template.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMemoPostDTO {
    private String username;
    private String deadline;
    private String headline;
    private String detail;
}
