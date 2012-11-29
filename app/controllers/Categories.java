package controllers;

import models.Category;
import models.Post;
import models.User;
import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.categories;
import views.html.showCategories;
import views.html.form;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;

/**
 * Controller class for the {@link models.Category} object.
 * Exposes RESTful CRUD services.
 */
public class Categories extends Controller {

	
    /**
     * Defines a form wrapping the User class.
     */ 
    final static Form<Category> addCat = form(Category.class);
  
    /**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(form.render(addCat));
    }
	
    public static Result submit() {
        Form<Category> filledForm = addCat.bindFromRequest();
        String name = filledForm.data().get("name").toString();
        if(!name.isEmpty()){
        Category cat = new Category(name, DateTime.now());
        cat.save();
        }
        return showAll();
    }
    
    public static Result getCategory(String name){
    	Category cat = Category.find.byId(name);
    	List<Post> posts = Post.findByCategory(name);
    	List<Category> categories = Category.find.all();
    	return ok(showCategories.render(cat, posts, categories));
    }
    
  
    /**
     * Gets list of all categories in the database
     *  @return
     */
    public static Result retrieveAll() {
        Logger.debug("Getting list of Categories");

        List<Category> categories = Category.find.all();
        return ok(Json.toJson(categories)).as("application/json");
    }
    

    public static Result showAll() {
        Logger.debug("Showing all Categories");

        List<Category> catList = Category.find.all();
        return ok(categories.render(catList));
    }

    public static Result retrieve(String name) {
        Logger.debug("Getting Category with name: " + name);
        Category cat = Category.find.byId(name);
        if(cat == null) {
            return notFound(name);
        }
        return ok(Json.toJson(cat)).as("application/json");
    }


    public static Result delete(String name) {
        Logger.debug("Deleting Category with name: " + name);
        Category cat = Category.find.byId(name);
        if(cat == null) {
            return notFound(name);
        }
        cat.delete();
        return ok(name).as("application/text");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result persist() {
        JsonNode request = request().body().asJson();
        Logger.info("Saving Category from JSON: " + request.asText());

        Category cat = null;
        ObjectMapper mapper = new ObjectMapper();
        //Attempt to parse JSON
        try {
            cat = mapper.readValue(request, Category.class);
            cat.save();
        } catch (Exception e) {
            Logger.error(e.getMessage(), e.getCause());
            return badRequest(e.getCause().getMessage());
        }
        return created(Json.toJson(cat));
    }


}

