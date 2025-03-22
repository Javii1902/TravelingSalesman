import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Route {
    private static final String API_KEY = System.getenv("gpsKey"); // Ensure this environment variable is set
    private double totalDistance = 0;
    private ArrayList<City> visitTracker = new ArrayList<>();
    private City startingCity;
    private City currentCity;

    // Constructor
    Route(City startingCity) {
        this.startingCity = startingCity;
        this.currentCity = startingCity;
        visitTracker.add(startingCity);
    }

    // Find the shortest path through all cities
    public void findShortestPath(ArrayList<City> cities) {
        City current = startingCity;
        while (visitTracker.size() < cities.size()) {
            int initialVisitedCount = visitTracker.size(); // Track the number of visited cities before the call
            getShortestDis(current, cities);

            // If no new city was visited, break the loop to prevent infinite looping
            if (visitTracker.size() == initialVisitedCount) {
                break;
            }

            current = currentCity; // Update the current city
        }
    }

    // Calculate the shortest distance from the current city to any unvisited city
    public void getShortestDis(City current, ArrayList<City> cities) {
        double shortestDistance = Double.MAX_VALUE;
        City closestCity = null;

        for (City neighbor : cities) {
            if (!neighbor.isVisited() && !neighbor.equals(current)) {
                double tempDis = getDistance(current.getAddress(), neighbor.getAddress());

                if (tempDis < shortestDistance) {
                    shortestDistance = tempDis;
                    closestCity = neighbor;
                }
            }
        }

        if (closestCity != null) {
            closestCity.setVisited(true);
            visitTracker.add(closestCity);
            totalDistance += shortestDistance;
            currentCity = closestCity; // Update the current city
        }
    }

    // Return the list of visited cities in order
    public ArrayList<City> getVisitedCities() {
        return visitTracker;
    }

    // Show the total distance traveled
    public String showTotalDistance() {
        return String.format("%.2f km", totalDistance);
    }

    // Use OpenRouteService API to calculate the distance between two addresses
    public double getDistance(String address1, String address2) {
        try {
            double[] coord1 = getCoordinates(address1);
            double[] coord2 = getCoordinates(address2);

            if (coord1 == null || coord2 == null) {
                return Double.MAX_VALUE;
            }

            String urlStr = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + API_KEY +
                    "&start=" + coord1[1] + "," + coord1[0] +
                    "&end=" + coord2[1] + "," + coord2[0];

            String response = fetchAPIResponse(urlStr);
            JSONObject jsonResponse = new JSONObject(response);

            // Check if the response contains "features"
            if (!jsonResponse.has("features")) {
                return Double.MAX_VALUE;
            }

            JSONArray featuresArray = jsonResponse.getJSONArray("features");
            if (featuresArray.length() == 0) {
                return Double.MAX_VALUE;
            }

            // Extract the first feature (route)
            JSONObject feature = featuresArray.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            JSONArray segments = properties.getJSONArray("segments");

            if (segments.length() == 0) {
                return Double.MAX_VALUE;
            }

            // Extract the distance from the first segment
            JSONObject segment = segments.getJSONObject(0);
            double distanceInMeters = segment.getDouble("distance");
            return distanceInMeters / 1000.0; // Convert to kilometers

        } catch (IOException | JSONException e) {
            return Double.MAX_VALUE;
        }
    }

    // Get coordinates for an address using Nominatim (OpenStreetMap)
    private double[] getCoordinates(String address) {
        try {
            String encodedAddress = address.replace(" ", "+");
            String urlStr = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress +
                    "&format=json&limit=1";

            String response = fetchAPIResponse(urlStr);
            JSONArray jsonResponse = new JSONArray(response);

            if (jsonResponse.length() == 0) {
                return null;
            }

            JSONObject location = jsonResponse.getJSONObject(0);
            if (!location.has("lat") || !location.has("lon")) {
                return null;
            }

            return new double[]{location.getDouble("lat"), location.getDouble("lon")};

        } catch (IOException | JSONException e) {
            return null;
        }
    }

    // Fetch API response from a URL
    private String fetchAPIResponse(String urlStr) throws IOException {
        try {
            Thread.sleep(1000); // Add a 1-second delay between API calls to avoid rate limits
        } catch (InterruptedException e) {
            // Ignore interruption
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        InputStream responseStream = conn.getInputStream();
        Scanner scanner = new Scanner(responseStream);
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return response;
    }
}