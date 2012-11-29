package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import java.util.Date;

@Entity
public class Category extends Model {
	private static final long serialVersionUID = 4482487573155059686L;
	
	@Id
	@Constraints.Required
	@Formats.NonEmpty
	public String name;

    @Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-dd")
	public DateTime dateCreated;
	
	public static Finder<String, Category> find = new Finder<String, Category>(
			String.class, Category.class);

    public Category() {
        super();
    }

    public Category(String name, DateTime dateCreated) {
		super();
		this.name = name;
		this.dateCreated = dateCreated;
	}

    /**
     * Creates a new category and saves it
     * @param name
     * @return
     */
    public static Category create(String name) {
        Category category = new Category(name, new DateTime());
        category.save();
        return category;
    }

}
