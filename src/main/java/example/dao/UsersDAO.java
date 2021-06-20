package example.dao;

import example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsersDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersDAO(JdbcTemplate jdbcTemplate){this.jdbcTemplate=jdbcTemplate;}

    public List<User> getAllUsers(){
        return jdbcTemplate.query("SELECT * FROM Users", new BeanPropertyRowMapper<>(User.class));
    }

    public User getUser(String name){
        //Сделать через .stream().findAny().orElse(null); компилятор мне почему-то не дал
        ArrayList<User> users = (ArrayList<User>) getAllUsers();
        for(User thisUser: users){
            if (thisUser.getUsername().equals(name))
                return thisUser;
        }
        return null;
    }

    public void save(User user){
        jdbcTemplate.update("INSERT INTO Users VALUES(?, ?)", user.getUsername(), user.getPassword());
    }

    public boolean isExist(User user){
        ArrayList<User> users = (ArrayList<User>) getAllUsers();
        for(User thisUser: users){
            if(thisUser.getUsername().equals(user.getUsername()))
                    return true;
        }
        return false;
    }

}
