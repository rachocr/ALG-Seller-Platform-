package application.listguard;

import java.time.LocalDate;

public class Order {
    private String orderNo;
    private int sellerId;
    private int buyerId;
    private LocalDate orderDate;
    private int orderItemId;
    private String productName;
    private String productCategory;
    private double itemPrice;
    private int itemQuantity;
    private String nameBuyer;
    private String emailBuyer;
    private String addressBuyer;
    private String contactInfoBuyer;
    private String orderStatus;

    public Order(String orderNo, int sellerId, int buyerId, LocalDate orderDate, int orderItemId,
                 String productName, String productCategory, double itemPrice, int itemQuantity,
                 String nameBuyer, String emailBuyer, String addressBuyer, String contactInfoBuyer,
                 String orderStatus) {
        this.orderNo = orderNo;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.orderDate = orderDate;
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.nameBuyer = nameBuyer;
        this.emailBuyer = emailBuyer;
        this.addressBuyer = addressBuyer;
        this.contactInfoBuyer = contactInfoBuyer;
        this.orderStatus = orderStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
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

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getNameBuyer() {
        return nameBuyer;
    }

    public void setNameBuyer(String nameBuyer) {
        this.nameBuyer = nameBuyer;
    }

    public String getEmailBuyer() {
        return emailBuyer;
    }

    public void setEmailBuyer(String emailBuyer) {
        this.emailBuyer = emailBuyer;
    }

    public String getAddressBuyer() {
        return addressBuyer;
    }

    public void setAddressBuyer(String addressBuyer) {
        this.addressBuyer = addressBuyer;
    }

    public String getContactInfoBuyer() {
        return contactInfoBuyer;
    }

    public void setContactInfoBuyer(String contactInfoBuyer) {
        this.contactInfoBuyer = contactInfoBuyer;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}