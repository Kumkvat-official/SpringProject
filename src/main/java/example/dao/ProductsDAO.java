package example.dao;

import example.models.Product;
import example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductsDAO(JdbcTemplate jdbcTemplate){this.jdbcTemplate=jdbcTemplate;}

    public List<Product> getAllProducts(){
        return jdbcTemplate.query("SELECT * FROM Assortment", new BeanPropertyRowMapper<>(Product.class));
    }

    public List<Product> getProducts(String type){
        //Возвращает лист определённого типа товаров
        return jdbcTemplate.query("SELECT * FROM Assortment where type=?", new Object[]{type}, new BeanPropertyRowMapper<>(Product.class));
    }

    public Product getProduct(String type, String name){
        //Сделать через .stream().findAny().orElse(null); компилятор мне почему-то не дал
        ArrayList<Product> products = (ArrayList<Product>) getAllProducts();
        for(Product thisProduct: products){
            if (thisProduct.getType().equals(type)&&thisProduct.getName().equals(name))
                return thisProduct;
        }
        return null;
    }


    public void save(Product product){
        jdbcTemplate.update("INSERT INTO Assortment VALUES(?, ?, ?, ?, ?, ?, ?, ?)", product.getType(), product.getName(),
                product.getDescription(), product.getCountInMoscow(), product.getCountInKazan(), product.getCountInBerlin(),
                product.getCountInTokyo(), product.getCountInStockholm());
    }

    public void update(String name, Product product){
        jdbcTemplate.update("UPDATE Assortment SET name=?, description=?, count_in_moscow=?, count_in_kazan=?," +
                "count_in_berlin=?, count_in_tokyo=?, count_in_stockholm=? WHERE name=?", product.getName(),
                product.getDescription(), product.getCountInMoscow(), product.getCountInKazan(), product.getCountInBerlin(),
                product.getCountInTokyo(), product.getCountInStockholm(), name);
    }

    public void delete(String name){
        jdbcTemplate.update("DELETE FROM Assortment WHERE name=?", name);
    }
}
