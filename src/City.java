class City
{
    private String name;
    private boolean isVisited;
    private int x;
    private int y;
    private String address;

    City(String name, boolean isVisited, int x, int y)
    {
        this.name = name;
        this.isVisited = isVisited;
        this.x = x;
        this.y = y;
    }
    City(String name, boolean isVisited, String address){
        this.name = name;
        this.isVisited = isVisited;
        this.address = address;
    }

    String getName()
    {
        return name;
    }

    boolean isVisited()
    {
        return isVisited;
    }

    void setVisited(boolean isVisited)
    {
        this.isVisited = isVisited;
    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

    String getAddress() {  // ✅ Added missing getAddress() method
        return address;
    }
}
