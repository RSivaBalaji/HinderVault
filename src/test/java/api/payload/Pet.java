package api.payload;

import java.util.List;

public class Pet 
{
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;  // List to store photo URLs
    private List<Tag> tags;
    private String status;

    // Getter and Setter for id
    public int getId() 
    {
        return id;
    }
    public void setId(int id) 
    {
        this.id = id;
    }

    // Getter and Setter for category
    public Category getCategory()
    {
        return category;
    }
    public void setCategory(Category category) 
    {
        this.category = category;
    }

    // Getter and Setter for name
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    // Getter and Setter for photoUrls (containing the specific URL)
    public List<String> getPhotoUrls()
    {
        return photoUrls;
    }
    public void setPhotoUrls(List<String> photoUrls) 
    {
        this.photoUrls = photoUrls;
    }

    // Getter and Setter for tags
    public List<Tag> getTags() 
    {
        return tags;
    }
    public void setTags(List<Tag> tags) 
    {
        this.tags = tags;
    }

    // Getter and Setter for status
    public String getStatus() 
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    // Category class for nested category object
    public static class Category 
    {
        private int id;
        private String name;

        // Getter and Setter for id
        public int getId() 
        {
            return id;
        }
        public void setId(int id) 
        {
            this.id = id;
        }

        // Getter and Setter for name
        public String getName()
        {
            return name;
        }
        public void setName(String name) 
        {
            this.name = name;
        }
    }

    // Tag class for nested tags list
    public static class Tag 
    {
        private int id;
        private String name;

        // Getter and Setter for id
        public int getId() 
        {
            return id;
        }
        public void setId(int id) 
        {
            this.id = id;
        }

        // Getter and Setter for name
        public String getName()
        {
            return name;
        }
        public void setName(String name)
        {
            this.name = name;
        }
    }
}
