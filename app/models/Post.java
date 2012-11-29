package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Post extends Model {

	private static final long serialVersionUID = 4166617896966160322L;
    public static final String TAGS_PROPNAME = "tags";

    @Id
    @GeneratedValue
    public long id;
    
	@Constraints.Required
	@Formats.NonEmpty
	public String title;
	
	@Formats.DateTime(pattern="yyyy-MM-dd")
    @Constraints.Required
	public DateTime datePosted;
	
	//@Lob Cant get this to work with ebean. Needs research.
	@Constraints.Required
	public String content;
	
	@Constraints.Required
    @ManyToOne
	public User author;
	
	@ManyToOne
    public Category category;
	
	@ManyToMany
    public List<Tag> tags;
	
	public static Finder<String, Post> find = new Finder<String, Post>(
			String.class, Post.class);

    public Post() {
        super();
    }

    public Post(DateTime datePosted, String title, String content, User author, Category category) {
		super();
		this.datePosted = datePosted;
		this.title = title;
		this.content = content;
		this.author = author;
		this.category = category;
	}

    /**
     * Creates new Post
     * @param authorEmail is the email (primary key) of the {@link User} author, must exist in database
     * @param categoryName is the name (primary key) of the {@link Category} category, must exist in database
     */
    public static Post create(String title, String content, String authorEmail, String categoryName) {
        Post post = new Post(new DateTime(), title, content,
        		User.find.where().eq("email", authorEmail).findUnique(), Category.find.byId(categoryName)
                );
        post.save();
        return post;
    }

    /**
     * Adds a tag to the post
     *
     * @param postName Name of post to add tag to. Must exist in database
     * @param tagName Name of {@link Tag} to add. Must exist in database
     */
    public static void addTag(String postName, String tagName) {
        // Why use this? Post p = Post.find.setId(postName).fetch("tags", "name").findUnique();
        Tag tag = Tag.find.byId(tagName);
        if(tag == null) return;

        Post p = Post.find.where().eq("title", postName).findUnique();;
        if(!p.tags.contains(tag)) {
            p.tags.add(tag);
            p.saveManyToManyAssociations(TAGS_PROPNAME);
        }
    }

    /**
     * Removes tag from post
     * @param postName post to remove tag from
     * @param tagName tag to remove
     */
    public static void removeTag(String postName, String tagName) {
        Post p = Post.find.byId(postName);
        p.tags.remove(Tag.find.byId(tagName));
        p.saveManyToManyAssociations(TAGS_PROPNAME);
    }

    /**
     * Finds posts by author
     * @param userEmail   email of author
     * @return  list of posts by author
     */
    public static List<Post> findByUser(String userEmail) {
        return find.where()
                .eq("author.email", userEmail)
                .findList();
    }

    /**
     * Finds posts by category
     * @param categoryName
     * @return list of posts by category
     */
    public static List<Post> findByCategory(String categoryName) {
        return find.where()
                .eq("category.name", categoryName)
                .findList();
    }

	/**
	 * Converts the title to an ID-string (no spaces)
	 */
	public String getFormattedTitleString(String stringToFormat) {
		return stringToFormat.replace(' ', '-').toLowerCase();
	}
}
