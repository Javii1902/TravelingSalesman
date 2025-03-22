import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TravelingSalesman {
    public static void main(String[] args) {
        // Create a list of cities with their addresses
        ArrayList<City> cities = new ArrayList<>();
        cities.add(new City("Houston Museum of Natural Science", false, "5555 Hermann Park Dr, Houston, TX 77030"));
        cities.add(new City("Hermann Park", false, "6001 Fannin St, Houston, TX 77030"));
        cities.add(new City("The Houston Zoo", false, "6200 Hermann Park Dr, Houston, TX 77030"));
        cities.add(new City("Discovery Green", false, "1500 McKinney St, Houston, TX 77010"));
        cities.add(new City("Buffalo Bayou Park", false, "1800 Allen Pkwy, Houston, TX 77019"));

        // Set the starting city
        City startingCity = cities.get(0);
        Route aRoute = new Route(startingCity);

        // Find the shortest path
        aRoute.findShortestPath(cities);

        // Write the order of addresses and total distance to a file
        try (FileWriter writer = new FileWriter("Order List.txt")) {
            writer.write("Order of addresses to visit:\n");
            for (City city : aRoute.getVisitedCities()) {
                writer.write(city.getAddress() + "\n");
            }
            writer.write("\nTotal Distance: " + aRoute.showTotalDistance());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}