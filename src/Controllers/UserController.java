package Controllers;

import Dao.SinglotonConnectionDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class UserController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("geroingarga");
    }

    public List<User> findAll() {
        List<User>users=new ArrayList<>();
        Connection connection= SinglotonConnectionDB.getConnection();
        try {
            Statement st=connection.createStatement();
            st.execute("SELECT * FROM User");
            ResultSet rs=st.getResultSet();
            while (rs.next()){
                users.add(new User(rs.getString("name"),rs.getInt("score")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
