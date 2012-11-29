package models;

import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Tag extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Constraints.Required
	@Formats.NonEmpty
	public String name;

    @Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-dd")
	public DateTime dateCreated;
	
	public static Finder<String, Tag> find = new Finder<String, Tag>(
			String.class, Tag.class);

    public Tag() {
        super();
    }

    public Tag(String name, DateTime dateCreated) {
		super();
		this.name = name;
		this.dateCreated = dateCreated;
	}

    /**
     * Creates new tag and saves it
     */
    public static Tag create(String name) {
        Tag tag = new Tag(name, new DateTime());
        tag.save();
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Tag tag = (Tag) o;

        if (dateCreated != null ? !dateCreated.equals(tag.dateCreated) : tag.dateCreated != null) return false;
        if (!name.equals(tag.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        return result;
    }
}
