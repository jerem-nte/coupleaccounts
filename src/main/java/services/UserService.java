package services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import core.User;
import dao.UserDao;
import dto.ResponseDto;
import dto.UserHtmlSelectDto;

@Path("/user")
public class UserService {

	
	@GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
    	
		List<User> users = UserDao.getUsers();
		return users;
    	
    }
	
	@GET
    @Path("listhtmlselect")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserHtmlSelectDto> listhtmlselect() {
    	
		List<User> users = UserDao.getUsers();
		return UserHtmlSelectDto.convert(users);
    	
    }
	
	
	@GET
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDto edit(@QueryParam("id") String userId, @QueryParam("name") String userName) {
    	
		try {
			UserDao.update(new User(userId, userName, null));
		} catch (Exception e) {
			return new ResponseDto(1, e.getMessage());
		}
		
		return new ResponseDto(0, "User successfully updated");
		
    }
}
