package models;

import helper.CustomDateTimeDeserializer;
import helper.CustomDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User extends Model {

	private static final long serialVersionUID = 893814552367259173L;
	
	@Id
	@GeneratedValue
	public long id;
	
    @Constraints.Required
    @Formats.NonEmpty
    public String email;

	@Constraints.Required
	public String firstName;
	
	@Constraints.Required
	public String lastName;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
	@Formats.DateTime(pattern=CustomDateTimeSerializer.SERIALIZE_FORMAT)
    @Constraints.Required
	public DateTime dateOfBirth;
	
	@Constraints.Required
	public String password;
	
	public boolean isAdmin;	

	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);

    public User() {
        super();
    }

	public User(String firstName, String lastName, DateTime dateOfBirth,
			String email, String password, boolean isAdmin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.password = password;
		this.isAdmin = isAdmin;
	}

    /**
     * Creates new User and persists to database
     *
     */
    public static User create(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, new DateTime(), email, password, false);
        user.save();
        return user;
    }
    
    public String toString(){
    	return firstName +" "+ lastName;
    }
}
