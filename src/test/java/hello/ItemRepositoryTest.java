package hello;

import hello.todo.domain.item.Item;
import hello.todo.domain.item.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ItemRepositoryTest {
    private static ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    public void clearAll(){
        itemRepository.clearAll();
    }

    @Test
    public void saveTest(){
        //give
        Item item = new Item("숙제","2024.01.20","미완");
        itemRepository.save(item);

        //when
        Item savedItem = itemRepository.findById(1L);

        //then
        assertThat(item).isEqualTo(savedItem);
    }

    @Test
    public void findByIdTest(){
        //give
        Item item = new Item("숙제","2024.01.20","미완");
        itemRepository.save(item);

        //when
        Long itemId = item.getId();
        Item findItem = itemRepository.findById(itemId);

        //then
        assertThat(item).isEqualTo(findItem);

    }

    @Test
    public void findAllTest(){
        //give
        Item item1 = new Item("숙제1","2024.01.20","미완");
        Item item2 = new Item("숙제2","2024.01.20","미완");
        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> itemList = itemRepository.findAll();

        //then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).contains(item1,item2);
    }

    @Test
    public void updateTest(){
        //give
        Item item = new Item("숙제1","2024.01.20","미완");
        itemRepository.save(item);

        //when
        Item updateItem = new Item("숙제1","2024.01.24","완");
        itemRepository.update(item.getId(),updateItem);

        //then
        assertThat(item.getWork()).isEqualTo(updateItem.getWork());
        assertThat(item.getEndDate()).isEqualTo(updateItem.getEndDate());
        assertThat(item.getMemoFinish()).isEqualTo(updateItem.getMemoFinish());

    }

    @Test
    public void deleteTest(){
        //give
        Item item = new Item("숙제1","2024-01-20","미완");
        itemRepository.save(item);
        //when
        itemRepository.delete(item.getId());
        List<Item> itemList = itemRepository.findAll();
        //then
        assertThat(itemList.size()).isEqualTo(0);
        assertThat(itemList).doesNotContain(item);
    }
}
