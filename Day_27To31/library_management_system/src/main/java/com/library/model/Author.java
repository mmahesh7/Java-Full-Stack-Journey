package Day_27To31.library_management_system.src.main.java.com.library.model;

public class Author {
    private int authorId;
    private String name;
    private String email;
    private Integer birthYear;
    private String biography;

    // Constructors
    public Author() {}

    public Author(String name, String email, Integer birthYear, String biography) {
        this.name = name;
        this.email = email;
        this.birthYear = birthYear;
        this.biography = biography;
    }

    public Author(int authorId, String name, String email, Integer birthYear, String biography) {
        this.authorId = authorId;
        this.name = name;
        this.email = email;
        this.birthYear = birthYear;
        this.biography = biography;
    }

    // Getters and Setters
    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    @Override
    public String toString() {
        return String.format("Author{id=%d, name='%s', email='%s', birthYear=%s}",
                authorId, name, email, birthYear);
    }
}