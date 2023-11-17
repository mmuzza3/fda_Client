class Order {
    private String orderId;
    private String firstWord;

    public Order(String orderId, String firstWord) {
        this.orderId = orderId;
        this.firstWord = firstWord;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFirstWord() {
        return firstWord;
    }
}