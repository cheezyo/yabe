package controllers;

import models.Category;
import models.Post;
import models.User;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;

import play.Logger;
import play.api.libs.json.JerksonJson;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.form;
import views.html.userForm;
import views.html.users;
import views.html.showUser;
import views.html.editUser;
import java.io.IOException;
import java.util.List;

/**
 * Controller class for the {@link models.User} object.
 * Exposes RESTful CRUD services.
 */
public class Users extends Controller {

    /**
     * Gets list of all users in the database
     * @return List of users as JSON
     */
    /**
     * Defines a form wrapping the User class.
     */ 
    final static Form<User> addUser = form(User.class);
  
    /**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(userForm.render(addUser));
    }
	
    public static Result submit() {
        Form<User> filledForm = addUser.bindFromRequest();
        String fname = filledForm.data().get("firstName").toString();
        String lname = filledForm.data().get("lastName").toString();
        String email = filledForm.data().get("email").toString();
        String password = filledForm.data().get("password").toString();
        

        User user = new User(fname, lname,DateTime.now(),email,password,true);
        user.save();
        
        return showAll();
    }
    

    public static Result updateUser(String email) {
        Form<User> filledForm = addUser.bindFromRequest();
        
        User user = User.find.where().eq("email", email).findUnique();
        
        user.firstName = filledForm.data().get("firstName");
        user.lastName = filledForm.data().get("lastName");   
        user.email = filledForm.data().get("email");
        user.password = filledForm.data().get("password");
        user.update();
        
        return showAll();
    }
    public static Result edit(String email) {
    	
    	User user = User.find.where().eq("email", email).findUnique();
    	
        return ok(editUser.render(addUser.fill(user),user));
    }
    	
    public static Result getUser(String email){
    	
    	User user = User.find.where().eq("email", email).findUnique();
    	List<Post> list = Post.findByUser(email);
    	
    	return ok(showUser.render(user,list));
    }
    
   
	
    public static Result retrieveAll() {
        Logger.debug("Getting list of users");
        List<User> users = User.find.all();
        return ok(Json.toJson(users)).as("application/json");
    }
    
    public static Result showAll() {
        Logger.debug("Showing all Categories");

        List<User> userList = User.find.all();
        return ok(users.render(userList));
    }

    /**
     * Retrieves {@link User} from db by email
     * @param email email (unique ID)
     * @return {@link User} object as JSON
     */
    public static Result retrieve(String email) {
        Logger.debug("Getting User with email: " + email);
        User user = User.find.byId(email);
        if(user == null) {
            return notFound(email);
        }
        return ok(Json.toJson(user)).as("application/json");
    }

    public static Result delete(String email) {
        Logger.debug("Deleting User with email: " + email);
        User user = User.find.byId(email);
        if(user == null) {
            return notFound(email);
        }
        user.delete();
        return showAll();
    }

    /**
     * Creates new {@link User} from request body JSON data
     * and persists to database
     * @return The resulting {@link User} object as JSON
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result persist() {
        JsonNode request = request().body().asJson();
        Logger.info("Saving User from JSON: " + request.asText());

        User user = null;
        ObjectMapper mapper = new ObjectMapper();
        //Attempt to parse JSON
        try {
            user = mapper.readValue(request, User.class);
            user.save();
        } catch (Exception e) {
            Logger.error(e.getMessage(), e.getCause());
            return badRequest(e.getCause().getMessage());
        }
        return created(Json.toJson(user));
    }

    /**
     * Updates {@link User} from request body JSON data
     * and saves to database
     * @return The resulting {@link User} object as JSON
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update() {
        JsonNode request = request().body().asJson();
        Logger.info("Updating User from JSON: " + request.asText());

        User user = null;
        ObjectMapper mapper = new ObjectMapper();
        //Attempt to parse JSON
        try {
            user = mapper.readValue(request, User.class);
            user.save();
            //user.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return badRequest(e.getCause().getMessage());
        }
        return ok(Json.toJson(user)).as("application/json");
    }
}
