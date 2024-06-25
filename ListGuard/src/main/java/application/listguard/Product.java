package application.listguard;

import java.time.LocalDate;

public class Product {
    private int productId;
    private LocalDate productListingDate;
    private String productName;
    private String productCategory;
    private double productPrice;
    private int productQuantity;
    private String productTags;
    private String productDesc;
    private String productAttributes;
    private double productDiscount;
    private String shopName; // New attribute to store seller's shop name

    // Constructor, getters, and setters
    public Product(int productId, LocalDate productListingDate, String productName, String productCategory,
                   double productPrice, int productQuantity, String productTags, String productDesc,
                   String productAttributes, double productDiscount, String shopName) {
        this.productId = productId;
        this.productListingDate = productListingDate;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productTags = productTags;
        this.productDesc = productDesc;
        this.productAttributes = productAttributes;
        this.productDiscount = productDiscount;
        this.shopName = shopName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getProductListingDate() {
        return productListingDate;
    }

    public void setProductListingDate(LocalDate productListingDate) {
        this.productListingDate = productListingDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductTags() {
        return productTags;
    }

    public void setProductTags(String productTags) {
        this.productTags = productTags;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(String productAttributes) {
        this.productAttributes = productAttributes;
    }

    public double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}