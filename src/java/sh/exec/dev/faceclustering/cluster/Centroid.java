package info.iylk.dev.faceclustering.cluster;

public class Centroid {
    private double mCx, mCy;
    private Cluster mCluster;

    /**
     * Constructor for the Centroid class.
     * @param cx the X coordinate of the centroid
     * @param cy the Y coordinate of the centroid
     */
    public Centroid(double cx, double cy) {
        mCx = cx;
        mCy = cy;
    }

    /**
     * Calculates the centroid's position.
     */
    public void calcCentroid() {
        int numDP = mCluster.getNumDataPoints();
        double tempX = 0, tempY = 0;

        for (DataPoint dp : mCluster.getDataPoints()) {
            tempX += dp.getX(); 
            tempY += dp.getY(); 
        }
        mCx = tempX / numDP;
        mCy = tempY / numDP;
        
        for (DataPoint dp : mCluster.getDataPoints()) {
            dp.calcEuclideanDistance();
        }
        mCluster.calcSumOfSquares();
    }

    /**
     * Assigns the Centroid to a specific Cluster.
     * @param c the specified Cluster.
     */
    public void setCluster(Cluster c) {
        mCluster = c;
    }

    /**
     * Returns the Centroid's X coordinate.
     * @return the Centroid's X coordinate
     */
    public double getCx() {
        return mCx;
    }

    /**
     * Returns the Centroid's Y coordinate.
     * @return the Centroid's Y coordinate
     */
    public double getCy() {
        return mCy;
    }

    /**
     * Returns the current Cluster the Centroid belongs to.
     * @return the current Cluster the Centroid belongs to
     */
    public Cluster getCluster() {
        return mCluster;
    }

}