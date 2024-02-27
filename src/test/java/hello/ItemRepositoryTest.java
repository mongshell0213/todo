package hello;

import hello.todo.domain.item.Item;
import hello.todo.domain.item.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        //Item item = new Item("숙제","2024.01.20","미완",null);

        LocalDateTime localDateTime = LocalDateTime.of(2019, 11, 12, 12, 32, 22, 3333);
        Item item = new Item("숙제",localDateTime,"미완",null);
        itemRepository.save(item);

        //when
        Item savedItem = itemRepository.findById(1L);

        //then
        assertThat(item).isEqualTo(savedItem);
    }


    @Test
    public void findByIdTest(){
        //give
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 02, 27, 12, 32);
        Item item = new Item("숙제",localDateTime1,"미완",null);
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
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 02, 27, 12, 32);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 12, 28, 12, 32);
        Item item1 = new Item("숙제1",localDateTime1,"미완",null);
        Item item2 = new Item("숙제2",localDateTime2,"미완",null);
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
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 02, 27, 12, 32);
        Item item = new Item("숙제1",localDateTime1,"미완",null);
        itemRepository.save(item);

        //when
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 12, 28, 12, 32);
        Item updateItem = new Item("숙제1",localDateTime2,"완",null);
        itemRepository.update(item.getId(),updateItem);

        //then
        assertThat(item.getWork()).isEqualTo(updateItem.getWork());
        assertThat(item.getEndDate()).isEqualTo(updateItem.getEndDate());
        assertThat(item.getFinishType()).isEqualTo(updateItem.getFinishType());

    }

    @Test
    public void deleteTest(){
        //give
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 02, 27, 12, 32);
        Item item = new Item("숙제1",localDateTime1,"미완",null);
        itemRepository.save(item);
        //when
        itemRepository.delete(item.getId());
        List<Item> itemList = itemRepository.findAll();
        //then
        assertThat(itemList.size()).isEqualTo(0);
        assertThat(itemList).doesNotContain(item);
    }


}
