public class Customer {
    private String name;
    private String id;

    // Constructor
    public Customer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    // Getter și Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
