package lk.kds_medical.asset.additionalService.controller;

import lk.kds_medical.asset.additionalService.entity.AdditionalService;
import lk.kds_medical.asset.additionalService.service.AdditionalServiceService;
import lk.kds_medical.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/additionalService" )
public class AdditionalServiceController implements AbstractController< AdditionalService, Integer > {
    private final AdditionalServiceService additionalServiceService;

    public AdditionalServiceController(AdditionalServiceService additionalServiceService) {
        this.additionalServiceService = additionalServiceService;
    }

    private String commonMethod(Model model, AdditionalService additionalService, boolean torf){
        model.addAttribute("additionalService", additionalService);
        model.addAttribute("addStatus", torf);
        return "additionalService/addAdditionalService";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("additionalServices", additionalServiceService.findAll());
        return "additionalService/additionalService";
    }
@GetMapping("/new")
    public String addForm(Model model) {
        return commonMethod(model, new AdditionalService(), true);
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("additionalServiceDetail", additionalServiceService.findById(id));
        return "additionalService/additionalService-detail";
    }
@GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonMethod(model,additionalServiceService.findById(id), false);
    }
@PostMapping(value = {"/save","/update"})
    public String persist(@Valid AdditionalService additionalService, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ){
            return commonMethod(model,additionalService, true);
        }
        additionalServiceService.persist(additionalService);
        return "redirect:/additionalService";
    }
@GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {

        if (!additionalServiceService.delete(id) ) {
            model.addAttribute("message", "Successfully deleted");
        } else {
            model.addAttribute("message", "Not deleted");
        }
    return "redirect:/additionalService";
    }
}
