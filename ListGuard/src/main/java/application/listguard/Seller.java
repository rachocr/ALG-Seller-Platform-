package application.listguard;

public class Seller {
    private String name;
    private String email;
    private String shopName;
    private String accountStatus;
    private int id;

    public Seller(String name, String email, String shopName, String accountStatus) {
        this.name = name;
        this.email = email;
        this.shopName = shopName;
        this.accountStatus = accountStatus;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}