package hello.todo.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store= new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long id, Item updated){
        Item item = findById(id);
        item.setWork(updated.getWork());
        item.setEndDate(updated.getEndDate());
        item.setFinishType(updated.getFinishType());
    }

    public void delete(Long id){
        store.remove(id);
    }

    public void clearAll(){
        store.clear();
    }
}
