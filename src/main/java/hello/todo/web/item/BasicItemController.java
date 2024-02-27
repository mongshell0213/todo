package hello.todo.web.item;

import hello.todo.domain.FinishType;
import hello.todo.domain.item.ItemRepository;
import hello.todo.domain.item.Item;
import hello.todo.domain.member.Member;
import hello.todo.web.login.SessionConst;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        log.info("loginMember={}",loginMember);
        List<Item> itemList = itemRepository.findAll();
        model.addAttribute("items",itemList);
        model.addAttribute("member",loginMember);
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
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item
            ,BindingResult result){
        itemRepository.update(itemId,item);
        if(result.hasErrors())
            return "basic/editForm";
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item",new Item());
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String add(@Validated @ModelAttribute Item item, BindingResult result){
        if(result.hasErrors())
            return "basic/addForm";
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
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 02, 27, 12, 32);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 12, 28, 12, 32);
        itemRepository.save(new Item("숙제 1",localDateTime1,"NOT_FINISHED",null));
        itemRepository.save(new Item("숙제 2",localDateTime2,"FINISHED","대충 어디 어디 숙제라는 뜻"));
    }
}
