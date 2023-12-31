package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.AddressService;
import com.cydeo.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {
    private final ClientVendorService clientVendorService;
    private final AddressService addressService;

    public ClientVendorController(ClientVendorService clientVendorService, AddressService addressService) {
        this.clientVendorService = clientVendorService;
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public String listOfClientVendors(Model model) {
        model.addAttribute("clientVendors", clientVendorService.findAll());
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String showCreateClientVendorForm(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("countries", addressService.getCountryList());
        return "/clientVendor/clientVendor-create";

    }

    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        boolean isClientVendorNameExist = clientVendorService.isClientVendorExist(clientVendorDto);
        if (isClientVendorNameExist) {
            bindingResult.rejectValue("clientVendorName", " ", "This client-vendor already exists.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", addressService.getCountryList());
            return "clientVendor/clientVendor-create";
        }
        if (clientVendorDto.getAddress().getCountry().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Please select a country.");
            return "redirect:/clientVendors/create";
        }
        clientVendorService.save(clientVendorDto);

        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String editClientVendor(@PathVariable Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("countries", addressService.getCountryList());
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable Long id,
                                     @Valid @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        boolean isClientVendorNameExist = clientVendorService.isClientVendorExist(clientVendorDto);
        if (isClientVendorNameExist) {
            bindingResult.rejectValue("clientVendorName", " ", "This client-vendor already exists.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", addressService.getCountryList());
            return "clientVendor/clientVendor-update";
        }
        if (clientVendorDto.getAddress().getCountry().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Please select a country.");
            return "redirect:/clientVendors/update" + id;
        }
        clientVendorService.update(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVender(@PathVariable Long id) {
        clientVendorService.delete(clientVendorService.findById(id));
        return "redirect:/clientVendors/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("title", "Cydeo Accounting-Client&Vendor");
        model.addAttribute("countries", addressService.getCountryList());

    }
}
