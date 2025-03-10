import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String URL = "jdbc:sqlite:/Users/razvanspineanu/Downloads/CARmanagement/car_rental.db";
    public static void createTable() {
        String sqlCars = "CREATE TABLE IF NOT EXISTS cars ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "brand TEXT NOT NULL, "
                + "model TEXT NOT NULL, "
                + "year INTEGER NOT NULL, "
                + "status TEXT NOT NULL, "
                + "price_per_day REAL NOT NULL"
                + ");";

        String sqlRentals = "CREATE TABLE IF NOT EXISTS rentals ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "customer_name TEXT NOT NULL, "
                + "phone TEXT NOT NULL, "
                + "car_info TEXT NOT NULL, "
                + "rental_period INTEGER NOT NULL, "
                + "total_cost REAL NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCars);
            stmt.execute(sqlRentals);
            System.out.println("Tabelele au fost create!");
        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelelor: " + e.getMessage());
        }
    }

    public static void addCar(String brand, String model, int year, String status, double pricePerDay) {
        String sql = "INSERT INTO cars(brand, model, year, status, price_per_day) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, model);
            pstmt.setInt(3, year);
            pstmt.setString(4, status);
            pstmt.setDouble(5, pricePerDay);
            pstmt.executeUpdate();
            System.out.println("Mașina a fost adăugată cu succes!");
        } catch (SQLException e) {
            System.out.println("Eroare la adăugarea mașinii: " + e.getMessage());
        }
    }

    public static class CarInfo {
        public String carInfo;
        public double pricePerDay;

        public CarInfo(String carInfo, double pricePerDay) {
            this.carInfo = carInfo;
            this.pricePerDay = pricePerDay;
        }
    }

    public static List<CarInfo> getAvailableCars() {
        List<CarInfo> availableCars = new ArrayList<>();
        String sql = "SELECT brand, model, price_per_day FROM cars WHERE status = 'disponibil'";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String carInfo = rs.getString("brand") + " " + rs.getString("model");
                double pricePerDay = rs.getDouble("price_per_day");
                availableCars.add(new CarInfo(carInfo, pricePerDay));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea mașinilor disponibile: " + e.getMessage());
        }
        return availableCars;
    }

    public static void showCars() {
        String sql = "SELECT * FROM cars";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Marca: " + rs.getString("brand") +
                        ", Model: " + rs.getString("model") +
                        ", An: " + rs.getInt("year") +
                        ", Status: " + rs.getString("status") +
                        ", Preț/zi: " + rs.getDouble("price_per_day"));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea mașinilor: " + e.getMessage());
        }
    }

    public static double getCarPrice(String carInfo) {
        String[] carParts = carInfo.split(" ");
        String sql = "SELECT price_per_day FROM cars WHERE brand = ? AND model = ?";
        double price = 0.0;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carParts[0]);
            pstmt.setString(2, carParts[1]);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("price_per_day");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea prețului mașinii: " + e.getMessage());
        }
        return price;
    }

    public static void removeCar(int carId) {
        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            pstmt.executeUpdate();
            System.out.println("Mașina cu ID-ul " + carId + " a fost ștearsă.");
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea mașinii: " + e.getMessage());
        }
    }

    public static void addRental(String customerName, String phone, String carInfo, int rentalPeriod, double totalCost) {
        String sql = "INSERT INTO rentals(customer_name, phone, car_info, rental_period, total_cost) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, phone);
            pstmt.setString(3, carInfo);
            pstmt.setInt(4, rentalPeriod);
            pstmt.setDouble(5, totalCost);
            pstmt.executeUpdate();

            updateCarStatus(carInfo, "indisponibil");

            System.out.println("Închirierea a fost adăugată cu succes!");
        } catch (SQLException e) {
            System.out.println("Eroare la adăugarea închirierii: " + e.getMessage());
        }
    }

    public static void updateCarStatus(String carInfo, String newStatus) {
        String[] carParts = carInfo.split(" ");
        String sql = "UPDATE cars SET status = ? WHERE brand = ? AND model = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, carParts[0]);
            pstmt.setString(3, carParts[1]);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea statusului mașinii: " + e.getMessage());
        }
    }

    public static void removeRental(int rentalId) {
        String sql1 = "SELECT car_info FROM rentals WHERE id = ?";
        String carInfo = "";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setInt(1, rentalId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                carInfo = rs.getString("car_info");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la găsirea mașinii: " + e.getMessage());
            return;
        }

        String sql2 = "DELETE FROM rentals WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, rentalId);
            pstmt.executeUpdate();

            if (!carInfo.isEmpty()) {
                updateCarStatus(carInfo, "disponibil");
            }

            System.out.println("Închirierea cu ID-ul " + rentalId + " a fost ștearsă.");
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea închirierii: " + e.getMessage());
        }
    }
}