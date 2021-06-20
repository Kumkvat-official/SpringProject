package example.controllers;

import example.dao.ProductsDAO;
import example.dao.UsersDAO;
import example.models.Product;
import example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/SpringProject")
public class MainController {

    private final UsersDAO usersDAO;
    private final ProductsDAO productsDAO;
    private static boolean AUTHORIZED_USER = false;
    private static boolean AUTHORIZED_ADMIN = false;

    @Autowired
    public MainController(UsersDAO usersDAO, ProductsDAO productsDAO){
        this.usersDAO=usersDAO;
        this.productsDAO = productsDAO;
    }

    @GetMapping()
    public String Home(){
        return "pages/home";
    }

    @GetMapping("/assortment")
    public String assortment(){
        if (AUTHORIZED_ADMIN)
            return "pages/adminAssortment";
        else
            return "pages/assortment";
    }

    @GetMapping("/assortment/all")
    public String showAll(Model model){
        //При попытке перейти на эту страницу, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if (AUTHORIZED_ADMIN){
            model.addAttribute("vieocards", productsDAO.getProducts("videocards"));
            model.addAttribute("motherboards", productsDAO.getProducts("motherboards"));
            model.addAttribute("cpu", productsDAO.getProducts("cpu"));
            model.addAttribute("ssd", productsDAO.getProducts("ssd"));
            model.addAttribute("hdd", productsDAO.getProducts("hdd"));
            model.addAttribute("ram", productsDAO.getProducts("ram"));
            model.addAttribute("power", productsDAO.getProducts("power"));
            model.addAttribute("coolers", productsDAO.getProducts("coolers"));
            model.addAttribute("cases", productsDAO.getProducts("cases"));
            model.addAttribute("fans", productsDAO.getProducts("fans"));
            return "pages/allProducts";
        }
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user")User user){
        return "pages/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "pages/registration";
        else if(usersDAO.isExist(user))
            return "pages/userExistError";
        else{
            usersDAO.save(user);
            return "redirect:/SpringProject/assortment";
        }
    }

    @GetMapping("/authorization")
    public String authorization(@ModelAttribute("user")User user){return "pages/authorization";}

    @PostMapping("/authorization")
    public String logIn(@ModelAttribute("user") User user){
        //Если такой юзер уже есть, то появится соответствующее сообщение
        //То же самое, если введён неверный пароль
        if (!usersDAO.isExist(user))
            return "pages/userNotExistError";
        else if(!usersDAO.getUser(user.getUsername()).getPassword().equals(user.getPassword()))
            return "pages/wrongPasswordError";
        else {
            if (user.getUsername().equals("Admin")){
                AUTHORIZED_ADMIN = true;
                AUTHORIZED_USER = false;
            }
            else{
                AUTHORIZED_USER = true;
                AUTHORIZED_ADMIN = false;
            }
            return "redirect:/SpringProject/assortment";
        }
    }

    @GetMapping("/assortment/{type}")
    public String products(Model model, @PathVariable("type") String type){
        model.addAttribute("products", productsDAO.getProducts(type));
        return "pages/products";
    }

    @GetMapping("/assortment/add")
    public String addProductPage(@ModelAttribute("product") Product product){
        //При попытке перейти на эту страницу, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if(AUTHORIZED_ADMIN)
            return "pages/newProduct";
        return "redirect:/SpringProject/assortment";
    }

    @PostMapping("/assortment/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult){
        //При попытке отправить запрос с этой страницы, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if(AUTHORIZED_ADMIN)
        {
            if(bindingResult.hasErrors())
                return "pages/newProduct";
            else
                productsDAO.save(product);
        }
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/assortment/{type}/{name}")
    public String viewProduct(Model model, @PathVariable("type") String type,
                              @PathVariable("name") String name){
        //Передаём готовый продукт, на валидность можно не проверять
        //Админу вернётся страница с возможностью редактирования и удаления, юзеру - с возможностью покупки
        //Гостю сайта - только с информацией
        model.addAttribute("product", productsDAO.getProduct(type, name));
        if (AUTHORIZED_ADMIN)
            return "pages/adminCertainProduct";
        else if (AUTHORIZED_USER)
            return "pages/userCertainProduct";
        else
            return "pages/unregisteredCertainProduct";
    }

    @GetMapping("/assortment/{type}/{name}/edit")
    public String editProductPage(Model model, @PathVariable("type") String type, @PathVariable("name") String name){
        //При попытке перейти на эту страницу, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        model.addAttribute("product", productsDAO.getProduct(type, name));
        if (AUTHORIZED_ADMIN)
            return "pages/editProduct";
        return "redirect:/SpringProject/assortment";
    }

    @PatchMapping("/assortment/{type}/{name}/edit")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                              @PathVariable("name") String name){
        //При попытке отправить запрос с этой страницы, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if(AUTHORIZED_ADMIN){
            if(bindingResult.hasErrors())
                return "pages/editProduct";
            else{
                productsDAO.update(name, product);
                return "redirect:/SpringProject/assortment/{type}/{name}";
            }
        }
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/assortment/{type}/{name}/delete")
    public String deleteProductPage(Model model, @ModelAttribute("product") Product product,
                                @PathVariable("type") String type, @PathVariable("name") String name){
        //При попытке перейти на эту страницу, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if (AUTHORIZED_ADMIN){
            model.addAttribute("product", productsDAO.getProduct(type, name));
            return "pages/deleteProduct";
        }
        return "redirect:/SpringProject/assortment";
    }

    @DeleteMapping("/assortment/{type}/{name}/delete")
    public String deleteProduct(@PathVariable("type") String type, @PathVariable("name") String name){
        //При попытке отправить запрос с этой страницы, не залогинившись под админом, контроллер будет перебрасывать обратно на ассортимент
        if (AUTHORIZED_ADMIN)
            productsDAO.delete(name);
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/assortment/{type}/{name}/buy")
    public String buyProductPage(Model model, @ModelAttribute("product") Product product,
                                 @PathVariable("type") String type, @PathVariable("name") String name){
        //При попытке перейти на эту страницу, не залогинившись как юзер, контроллер будет перебрасывать обратно на ассортимент
        if(AUTHORIZED_USER) {
            model.addAttribute("product", productsDAO.getProduct(type, name));
            return "pages/buyProduct";
        }
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/assortment/{type}/{name}/buy/{place}")
    public String confirmTransactionPage(@ModelAttribute("product") Product product,
                                         @PathVariable("type") String type, @PathVariable("name") String name, @PathVariable("place") String place){
        //При попытке перейти на эту страницу, не залогинившись как юзер, контроллер будет перебрасывать обратно на ассортимент
        if (AUTHORIZED_USER){
            Product currentProduct = productsDAO.getProduct(type, name);
            switch (place){
                case "Москва": if (currentProduct.getCountInMoscow()==0)
                    return "pages/noProductsError";
                break;
                case "Казань": if (currentProduct.getCountInKazan()==0)
                    return "pages/noProductsError";
                break;
                case "Берлин": if (currentProduct.getCountInBerlin()==0)
                    return "pages/noProductsError";
                break;
                case "Токио": if (currentProduct.getCountInTokyo()==0)
                    return "pages/noProductsError";
                break;
                case "Стокгольм": if (currentProduct.getCountInStockholm()==0)
                    return "pages/noProductsError";
                break;
            }
            return "pages/confirmTransaction";
        }
        return "redirect:/SpringProject/assortment";
    }

    @PostMapping("/assortment/{type}/{name}/buy/{place}")
    public String confirmTransaction(@ModelAttribute("product") Product product,
                                     @PathVariable("type") String type, @PathVariable("name") String name, @PathVariable("place") String place){
        if (AUTHORIZED_USER){
            Product currentProduct = productsDAO.getProduct(type, name);
            switch (place){
                case "Москва":
                    currentProduct.setCountInMoscow(currentProduct.getCountInMoscow() - 1);
                    break;
                case "Казань":
                    currentProduct.setCountInKazan(currentProduct.getCountInKazan() - 1);
                    break;
                case "Берлин":
                    currentProduct.setCountInBerlin(currentProduct.getCountInBerlin() - 1);
                    break;
                case "Токио":
                    currentProduct.setCountInTokyo(currentProduct.getCountInTokyo() - 1);
                    break;
                case "Стокгольм":
                    currentProduct.setCountInStockholm(currentProduct.getCountInStockholm() - 1);
                    break;
            }
            productsDAO.update(name, currentProduct);
            return "redirect:/SpringProject/assortment/{type}/{name}/buy/{place}/congrats";
        }
        return "redirect:/SpringProject/assortment";
    }

    @GetMapping("/assortment/{type}/{name}/buy/{place}/congrats")
    public String congrats(@ModelAttribute("product") Product product,
                           @PathVariable("type") String type, @PathVariable("name") String name, @PathVariable("place") String place){
        //При попытке перейти на эту страницу, не залогинившись как юзер, контроллер будет перебрасывать обратно на ассортимент
        if(AUTHORIZED_USER)
            return "pages/congrats";
        return "redirect:/SpringProject/assortment";
    }
    
    @GetMapping("/exit")
    public String exit(){
        AUTHORIZED_USER = false;
        AUTHORIZED_ADMIN = false;
        return "redirect:/SpringProject";
    }
}

