package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menu", menuDao.findAll());
        model.addAttribute("title", "Menu");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            model.addAttribute(new Menu());
            return "menu/add";
        }
        else {
            menuDao.save(menu);
            return "redirect:view/" + menu.getId();
        }

    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String viewMenu (Model model, @PathVariable int menuId) {
        model.addAttribute("menu", menuDao.findOne(menuId));
        model.addAttribute("title", "View Menu");
        return "view";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.GET)
    public String addItem (Model model, @PathVariable int menuId, int cheeseId){
        menuDao.findOne(menuId);
        model.addAttribute("menu", new AddMenuItemForm(menuId, cheeseId));
        model.addAttribute("title", menuDao.findOne(menuId)); //probably won't return the right thing but if it returns something I at least know it works
        return "add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem (Model model, @ModelAttribute @Valid AddMenuItemForm addMenuItemForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute(new AddMenuItemForm());
            return "add-item";
        }

        Cheese thisCheese = cheeseDao.findOne(addMenuItemForm.getCheeseId());
        Menu thisMenu = menuDao.findOne(addMenuItemForm.getMenuId());
        model.addAttribute(thisCheese);
        menuDao.save(thisMenu);
        return "view";
    }
}
