package lk.kds_medical.asset.additional_service.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.additional_service.service.AdditionalServiceService;
import lk.kds_medical.util.interfaces.AbstractController;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/additionalService" )
public class AdditionalServiceController implements AbstractController< AdditionalService, Integer > {
  private final AdditionalServiceService additionalServiceService;

  public AdditionalServiceController(AdditionalServiceService additionalServiceService) {
    this.additionalServiceService = additionalServiceService;
  }

  private String commonMethod(Model model, AdditionalService additionalService, boolean addStatus) {
    model.addAttribute("additionalService", additionalService);
    model.addAttribute("addStatus", addStatus);
    return "additionalService/addAdditionalService";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("additionalServices", additionalServiceService.findAll());
    return "additionalService/additionalService";
  }

  @GetMapping( "/add" )
  public String addForm(Model model) {
    return commonMethod(model, new AdditionalService(), true);
  }

  @GetMapping( "/{id}" )
  public String view(@PathVariable Integer id, Model model) {
    model.addAttribute("additionalServiceDetail", additionalServiceService.findById(id));
    return "additionalService/additionalService-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonMethod(model, additionalServiceService.findById(id), false);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid AdditionalService additionalService, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonMethod(model, additionalService, true);
    }
    additionalServiceService.persist(additionalService);
    return "redirect:/additionalService";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {

    if ( !additionalServiceService.delete(id) ) {
      model.addAttribute("message", "Successfully deleted");
    } else {
      model.addAttribute("message", "Not deleted");
    }
    return "redirect:/additionalService";
  }

  @GetMapping( "/findPriceById/{id}" )
  @ResponseBody
  public MappingJacksonValue findPriceById(@PathVariable( "id" ) Integer id) {

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(additionalServiceService.findById(id));

    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept("price");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("AdditionalService", simpleBeanPropertyFilter);

    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }
}
