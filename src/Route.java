import java.lang.Math;
import java.util.ArrayList;
class Route
{
    private double totalDistance = 0;
    private ArrayList<City> visitTracker = new ArrayList<>();
    private City startingCity;
    private City currentCity;

    // Set the starting city of the route
    Route(City startingCity)
    {
        this.startingCity = startingCity;
        this.currentCity = startingCity;
        visitTracker.add(startingCity);
    }
    
    //get the shortest distance between a city and its not visited neighbor
    public void getShortestDis(City current, ArrayList<City> neighbors)
    {
        //tempDis will be used to check distances
        double tempDis;
        City tempCity = current;
        double shortestDistance = 0;

        for (City neighbor : neighbors)
        {
            if (!current.equals(neighbor) && !neighbor.isVisited() && !startingCity.equals(neighbor))
            {
                // get distance between current city and a neighbor that hasn't been visited
                tempDis = getDistance(current, neighbor);

                // shortest distance has not been set yet
                if (shortestDistance == 0)
                {
                    shortestDistance = tempDis;
                    tempCity = neighbor;
                }
                else if (tempDis <= shortestDistance)
                {
                    shortestDistance = tempDis;
                    tempCity = neighbor;
                }
            }
        }

        // All cities have been visited excluding the starting city
        if (visitTracker.size() == neighbors.size())
        {
            startingCity.setVisited(true);
            currentCity = startingCity;
            visitTracker.add(startingCity);
            totalDistance = totalDistance + getDistance(current, startingCity);
        }
        else
        {
            tempCity.setVisited(true);
            currentCity = tempCity;
            visitTracker.add(currentCity);
            totalDistance = totalDistance + shortestDistance;
        }
    }

    public String showVisitedCitiesOrder()
    {
        StringBuilder visitedCities = new StringBuilder();
        int count = 0;

        for (City visitedCity : visitTracker)
        {
            count++;
            visitedCities.append(visitedCity.getName());

            if (count < visitTracker.size())
            {
                visitedCities.append(" -> ");
            }
        }
        System.out.println("City Order: " + visitedCities);
        return ("City Order: " + visitedCities);
    }

    public String showTotalDistance()
    {
        System.out.println("Total Distance: " + String.format("%.2f", totalDistance));
        return ("Total Distance: " + String.format("%.2f", totalDistance));
    }

    /*
     Use the getShortestDistance function to find the closest city that has not been visited. if
     not visited.
    */
    City getCurrrentCity()
    {
        return currentCity;
    }

    //gets the distance between 2 cities
    private double getDistance(City current, City neighbor)
    {
        double x = (neighbor.getX() - current.getX()) * (neighbor.getX() - current.getX());
        double y = (neighbor.getY() - current.getY()) * (neighbor.getY() - current.getY());
        return Math.sqrt(Math.abs(x + y));
    }
}
