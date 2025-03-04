package com.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping(value = "/admin")
    public String admin() {
        return "redirect:admin_product_list";
    }

    @GetMapping(value = "/admin_product_list")
    public String listCategory() {
        return "admin/listProduct";
    }

    @GetMapping(value = "/admin_non_standard_product_list")
    public String listNonStandardCategory() {
        return "admin/listNonStandardProduct";
    }

    @GetMapping(value = "/admin_temp_product_list")
    public String listTempCategory() {
        return "admin/listTempProduct";
    }

    @GetMapping(value = "/admin_product_edit")
    public String editCategory() {
        return "admin/editProduct";
    }

    @GetMapping(value = "/admin_non_standard_product_edit")
    public String editNonStandardCategory() {
        return "admin/editNonStandardProduct";
    }

    @GetMapping(value = "/admin_temp_product_edit")
    public String editTempCategory() {
        return "admin/editTempProduct";
    }

    @GetMapping(value = "/admin_user_list")
    public String listUser() {
        return "admin/listUser";

    }
}
