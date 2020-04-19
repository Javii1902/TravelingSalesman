class City
{
    private String name;
    private boolean isVisited;
    private int x;
    private int y;

    City(String name, boolean isVisited, int x, int y)
    {
        this.name = name;
        this.isVisited = isVisited;
        this.x = x;
        this.y = y;
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
}
