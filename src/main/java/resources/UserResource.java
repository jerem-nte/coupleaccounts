package resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import core.User;
import dao.UserDao;
import dto.UserHtmlSelectDto;

@Path("/user")
public class UserResource {

	
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
}
