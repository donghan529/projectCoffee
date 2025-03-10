package projectCoffee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projectCoffee.dto.ItemFormDto;
import projectCoffee.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class itemController {

    private final ItemService itemService;
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile>
                          itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
             model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
             return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto,itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
                    return "item/itemForm";
        }

        return "redirect:/";
    }

    //상품 수정하기
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
        System.out.println("12112222211111222" + itemId.toString());
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            System.out.println("212121212121" + itemFormDto);
            model.addAttribute("itemFormDto", itemFormDto);
            System.out.println("3232232323232323" + itemFormDto.toString());
        } catch (Exception e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
}
