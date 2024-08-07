package hello.todo.web.item;

import hello.todo.domain.FinishType;
import hello.todo.domain.item.ItemRepository;
import hello.todo.domain.item.Item;
import hello.todo.domain.member.Member;
import hello.todo.web.login.SessionConst;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public String item(@PathVariable(name="itemId") Long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    //@GetMapping("/download")
    public void downloadExcel(HttpServletResponse response) throws IOException{
        List<Item> itemList = itemRepository.findAll();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("todo list");
        int rowNo=0;

        Row headerRow = sheet.createRow(rowNo++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("할 일");
        headerRow.createCell(2).setCellValue("기한");
        headerRow.createCell(3).setCellValue("완료 여부");

        for (Item item : itemList) {
            Row row = sheet.createRow(rowNo++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getWork());
            row.createCell(2)
                    .setCellValue(item.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            row.createCell(3).setCellValue(item.getFinishType());
        }
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition","attachment;filename=todoList.xls");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadExcel2(HttpServletResponse response) throws IOException{
        List<Item> itemList = itemRepository.findAll();
        File tempXls = File.createTempFile("tempXls",".xls",new File("D:\\todo\\file"));
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("todo list");
        int rowNo=0;

        Row headerRow = sheet.createRow(rowNo++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("할 일");
        headerRow.createCell(2).setCellValue("기한");
        headerRow.createCell(3).setCellValue("완료 여부");

        for (Item item : itemList) {
            Row row = sheet.createRow(rowNo++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getWork());
            row.createCell(2)
                    .setCellValue(item.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            row.createCell(3).setCellValue(item.getFinishType());
        }

        workbook.write(new FileOutputStream(tempXls));
        workbook.close();
        tempXls.deleteOnExit();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=todoList.xls")
                .contentType(MediaType.parseMediaType("ms-vnd/excel"))
                .body(new InputStreamResource(new FileInputStream(tempXls)));
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable(name="itemId") Long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable(name="itemId") Long itemId, @Validated @ModelAttribute Item item
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
        log.info("item={}",item);
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId(); //PRG 패턴 도입
        //return "basic/item"; //새로고침시 post가 재전송되는 문제
    }

    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable(name="itemId") Long itemId){
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
