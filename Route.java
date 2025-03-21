import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

class Route {
    private static final String API_KEY = System.getenv("gpsKey");
    private double totalDistance = 0;
    private ArrayList<City> visitTracker = new ArrayList<>();
    private City startingCity;
    private City currentCity;

    Route(City startingCity) {
        this.startingCity = startingCity;
        this.currentCity = startingCity;
        visitTracker.add(startingCity);
    }

    public void getShortestDis(City current, ArrayList<City> neighbors) {
        double shortestDistance = Double.MAX_VALUE;
        City closestCity = null;

        for (City neighbor : neighbors) {
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
            currentCity = closestCity;
            visitTracker.add(currentCity);
            totalDistance += shortestDistance;
        }
    }

    public String showVisitedCitiesOrder() {
        StringBuilder visitedCities = new StringBuilder();
        for (int i = 0; i < visitTracker.size(); i++) {
            visitedCities.append(visitTracker.get(i).getName());
            if (i < visitTracker.size() - 1) {
                visitedCities.append(" -> ");
            }
        }
        System.out.println("City Order: " + visitedCities);
        return "City Order: " + visitedCities;
    }

    public String showTotalDistance() {
        System.out.println("Total Distance: " + String.format("%.2f km", totalDistance));
        return "Total Distance: " + String.format("%.2f km", totalDistance);
    }

//    public City getCurrentCity() { // ✅ Added missing method
//        return currentCity;
//    }

    private double getDistance(String address1, String address2) {
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

            return jsonResponse.getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONObject("summary")
                    .getDouble("distance") / 1000.0; // Convert meters to km

        } catch (IOException e) {
            System.out.println("Error fetching distance: " + e.getMessage());
            return Double.MAX_VALUE;
        }
    }

    private double[] getCoordinates(String address) {
        try {
            String urlStr = "https://nominatim.openstreetmap.org/search?q=" + address.replace(" ", "+") +
                    "&format=json&limit=1";

            String response = fetchAPIResponse(urlStr);
            JSONArray jsonResponse = new JSONArray(response);

            if (jsonResponse.length() == 0) return null;

            JSONObject location = jsonResponse.getJSONObject(0);
            return new double[]{location.getDouble("lat"), location.getDouble("lon")};

        } catch (IOException e) {
            System.out.println("Error fetching coordinates: " + e.getMessage());
            return null;
        }
    }

    private String fetchAPIResponse(String urlStr) throws IOException {
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

    public City getCurrrentCity() {
        return currentCity;
    }
}
