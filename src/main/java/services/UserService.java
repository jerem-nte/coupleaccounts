package services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.User;
import dao.UserDao;
import dto.ResponseDto;
import dto.UserHtmlSelectDto;

@RestController
@RequestMapping("/user")
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@RequestMapping("/list")
    public List<User> list() throws SQLException {
    	
		List<User> users = userDao.getUsers();
		return users;
    	
    }
	
	@RequestMapping("/listhtmlselect")
    public List<UserHtmlSelectDto> listhtmlselect() throws SQLException {
    	
		List<User> users = userDao.getUsers();
		return UserHtmlSelectDto.convert(users);
    	
    }
	
	
	@RequestMapping("edit")
    public ResponseDto edit(@RequestParam("id") String userId, @RequestParam("name") String userName) {
    	
		try {
			userDao.update(new User(userId, userName, null));
		} catch (Exception e) {
			return new ResponseDto(1, e.getMessage());
		}
		
		return new ResponseDto(0, "User successfully updated");
		
    }
}
