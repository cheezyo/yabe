package controllers;

import models.Category;
import models.Post;
import models.Tag;
import models.User;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import play.data.*;
import play.Logger;
import play.api.libs.json.JerksonJson;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.editUser;
import views.html.newPost;
import views.html.posts;
import views.html.showPost;
import views.html.editPost;
import views.html.addTags;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the {@link models.User} object.
 * Exposes RESTful CRUD services.
 */
public class Posts extends Controller {

    /**
     * Gets list of all users in the database
     * @return List of users as JSON
     */
	
    /**
     * Defines a form wrapping the User class.
     */ 
    final static Form<Post> postForm = form(Post.class);
  
    /**
     * Display a blank form.
     */ 
    public static Result blank() {
    	
    	List<Tag> tags = Tag.find.all();
        return ok(newPost.render(postForm,tags));
    }
    
    public static List<String> getCategories(){
    	List<Category> list = Category.find.all();
    	List <String> names = new ArrayList<String>();
    	for (Category c :  list){
    		names.add(c.name);
    	}
    	return names;
    }
    
    public static List<String> getUsers(){
    	List<User> list = User.find.all();
    	List <String> names = new ArrayList<String>();
    	for (User u :  list){
    		names.add(u.email);
    	}
    	return names;
    }
    public static Reslut addTags(String postTitle){
    	
    	Post post = Post.find.where().eq("title", postTitle).findUnique();
    	
    	return ok(addTags.render(post, Tag.find.all()));
    	
    }
	
    public static Result submit() {
        Form<Post> filledForm = postForm.bindFromRequest();
        String title = filledForm.data().get("title");
        String content = filledForm.data().get("content");
        String author = filledForm.data().get("author");
        String category = filledForm.data().get("category");
      
        Post.create(title, content, author, category);
        
        
        return showAll();
    }
    
    public static Result updatePost(String title) {
        Form<Post> filledForm = postForm.bindFromRequest();
      
        Post post = Post.find.where().eq("title", title).findUnique();
       
       
        Category category = Category.find.byId(filledForm.data().get("category"));
        
        String email = filledForm.data().get("author");
        User user = User.find.where().eq("email", email).findUnique();
        
        post.title = filledForm.data().get("title");
        post.content = filledForm.data().get("content");
        post.author = user;
        post.category = category;
        
        post.update();
        return showAll();
    }
    public static Result edit(String title) {
    	
    	Post post = Post.find.where().eq("title", title).findUnique();
    	
        return ok(editPost.render(postForm.fill(post),post));
    }
    
    public static Result retrieveAll() {
        Logger.debug("Getting list of Posts");
        List<Post> post = Post.find.all();
        return ok(Json.toJson(post)).as("application/json");
    }
    
    public static Result showAll(){
    	List<Post> list = Post.find.all();
    	return ok(posts.render(list));
    }
    
    public static Result getPost(String title){
    	
    	Post post = Post.find.where().eq("title", title).findUnique();
    	User user = post.author;
    	
    	Logger.debug(user.email);
    	return ok(showPost.render(post,user));
    }
    /**
     * Retrieves {@link User} from db by email
     * @param email email (unique ID)
     * @return {@link User} object as JSON
     */
    public static Result retrieve(String title) {
        Logger.debug("Getting Post with title: " + title);
        Post post = Post.find.byId(title);
        if(post == null) {
            return notFound(title);
        }
        return ok(Json.toJson(post)).as("application/json");
    }

    public static Result delete(String title) {
        Logger.debug("Deleting Post with title: " + title);
        Post post = Post.find.byId(title);
        if(post == null) {
            return notFound(title);
        }
        post.delete();
        return ok(title).as("application/text");
    }

    /**
     * Creates new {@link User} from request body JSON data
     * and persists to database
     * @return The resulting {@link User} object as JSON
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result persist() {
        JsonNode request = request().body().asJson();
        Logger.info("Saving Post from JSON: " + request.asText());

        Post post = null;
        ObjectMapper mapper = new ObjectMapper();
        //Attempt to parse JSON
        try {
            post = mapper.readValue(request, Post.class);
            post.save();
        } catch (Exception e) {
            Logger.error(e.getMessage(), e.getCause());
            return badRequest(e.getCause().getMessage());
        }
        return created(Json.toJson(post));
    }

    /**
     * Updates {@link User} from request body JSON data
     * and saves to database
     * @return The resulting {@link User} object as JSON
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update() {
        JsonNode request = request().body().asJson();
        Logger.info("Updating Post from JSON: " + request.asText());

        Post post = null;
        ObjectMapper mapper = new ObjectMapper();
        //Attempt to parse JSON
        try {
            post = mapper.readValue(request, Post.class);
            post.save();
            //user.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return badRequest(e.getCause().getMessage());
        }
        return ok(Json.toJson(post)).as("application/json");
    }
}
