package info.iylk.dev.faceclustering.cluster;

import java.util.List;
import java.util.ArrayList;

public class Cluster {
    private String mName;
    private Centroid mCentroid;
    private double mSumSqr;
    private List<DataPoint> mDataPoints = new ArrayList<DataPoint>();

    /**
     * Constructor for the Cluster class.
     * @param name the name of the Cluster
     */
    public Cluster(String name) {
        mName = name;
    }

    /**
     * Sets the Cluster's Centroid
     * @param c the Centroid
     */
    public void setCentroid(Centroid c) {
        mCentroid = c;
    }

    /**
     * Returns the current Cluster's Centroid.
     * @return the current Cluster's Centroid
     */
    public Centroid getCentroid() {
        return mCentroid;
    }

    /**
     * Adds a DataPoint to the Cluster.
     * @param dp the DataPoint to be added
     */
    public void addDataPoint(DataPoint dp) {
        dp.setCluster(this);
        mDataPoints.add(dp);
        calcSumOfSquares();
    }

    /**
     * Removes a DataPoint from the Cluster.
     * @param dp the DataPoint to be removed
     */
    public void removeDataPoint(DataPoint dp) {
        mDataPoints.remove(dp);
        calcSumOfSquares();
    }

    /**
     * Returns the number of Data Points.
     * @return the number of Data Points
     */
    public int getNumDataPoints() {
        return mDataPoints.size();
    }

    /**
     * Returns the DataPoint in a given position.
     * @param pos the position of the DataPoint
     * @return the DataPoint in the specified position
     */
    public DataPoint getDataPoint(int pos) {
        return mDataPoints.get(pos);
    }

    /**
     * Called everytime a node joins or leaves the Cluster.
     */
    public void calcSumOfSquares() {
        double temp = 0;
        for (DataPoint dp : mDataPoints) {
            temp += dp.getCurrentEuDt();
        }
        mSumSqr = temp;
    }

    /**
     * Returns the sum of squares.
     * @return the sum of squares
     */
    public double getSumSqr() {
        return mSumSqr;
    }

    /**
     * Returns the Cluster's name.
     * @return the Cluster's name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the Cluster's DataPoints.
     * @return the Cluster's DataPoints
     */
    public List<DataPoint> getDataPoints() {
        return mDataPoints;
    }

}