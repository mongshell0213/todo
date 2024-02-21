package hello.todo.web.basic;

import hello.todo.domain.FinishType;
import hello.todo.domain.item.ItemRepository;
import hello.todo.domain.item.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> itemList = itemRepository.findAll();
        model.addAttribute("items",itemList);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item",new Item());
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Item item){
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId(); //PRG 패턴 도입
        //return "basic/item"; //새로고침시 post가 재전송되는 문제
    }

    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable Long itemId){
        itemRepository.delete(itemId);
        return "redirect:/basic/items";
    }

    @ModelAttribute("finishTypes")
    public FinishType[] finishTypes(){
        FinishType[] values = FinishType.values();
        return values;
    }

    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("숙제 1","2024-01-24","NOT_FINISHED",null));
        itemRepository.save(new Item("숙제 2","2024-01-28","FINISHED","대충 어디 어디 숙제라는 뜻"));
    }
}
