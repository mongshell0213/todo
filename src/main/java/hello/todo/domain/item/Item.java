package hello.todo.domain.item;

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
    private String work;
    private String endDate;
    private String memoFinish;

    public Item(String work, String endDate, String memoFinish){
        this.work = work;
        this.endDate = endDate;
        this.memoFinish = memoFinish;
    }
}
