public class Car {
    private String model;
    private String brand;
    private String licensePlate;
    private int year;
    private double pricePerDay;
    private String imagePath;

    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    // Getteri și setteri existenți...

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}