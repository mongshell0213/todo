package hello.todo.domain.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 아이디
 * 해야하는 일
 * 기한
 * 완료 여부
 */
@Getter @Setter
public class Item {
    private Long id;
    @NotBlank
    private String work;
    @NotBlank
    private String endDate;
    @NotNull(message = "여부를 지정해주세요.")
    private String finishType;
    private String note; //비고란

    public Item(String work, String endDate, String finishType,String note){
        this.work = work;
        this.endDate = endDate;
        this.finishType = finishType;
        this.note = note;
    }
    public Item(){

    }
}
