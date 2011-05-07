package info.iylk.dev.faceclustering.cluster;

public class DataPoint {
	
	private double mX,mY;
	private String mObjName;
	private Cluster mCluster;
	private double mEuDt;

	/**
	 * Constructor for the DataPoint class.
	 * @param x the x coordinate of a point
	 * @param y the y coordinate of a point
	 * @param name the name of the point
	 */
	public DataPoint(double x, double y, String name) {
		mX = x;
		mY = y;
		mObjName = name;
	}

	/**
	 * Assigns the DataPoint to a Cluster.
	 * @param cluster the Cluster that the DataPoint belongs to
	 */
	public void setCluster(Cluster cluster) {
		mCluster = cluster;
		calcEuclideanDistance();
	}

	/**
	 * Called when DP is added to a cluster or when a Centroid is recalculated.
	 */
	public void calcEuclideanDistance() { 
		mEuDt = Math.hypot(mX - mCluster.getCentroid().getCx(),
                       mY - mCluster.getCentroid().getCy());
	}

	/**
	 * Tests the euclidean distance to a given Centroid.
	 * @param c the Centroid
	 * @return the euclidean distance to specified Centroid
	 */
	public double testEuclideanDistance(Centroid c) {
		return Math.sqrt(Math.pow((mX - c.getCx()), 2) + Math.pow((mY - c.getCy()), 2));
	}

	/**
	 * Returns the current Y coordinate of the DataPoint.
	 * @return the current Y coordinate of the DataPoint
	 */
	public double getX() {
		return mX;
	}

	/**
	 * Returns the current Y coordinate of the DataPoint.
	 * @return the current Y coordinate of the DataPoint
	 */
	public double getY() {
		return mY;
	}

	/**
	 * Returns the current Cluster the DataPoint belongs to.
	 * @return the current Cluster the DataPoint belongs to
	 */
	public Cluster getCluster() {
		return mCluster;
	}

	/**
	 * Returns the DataPoint's euclidean distance to a Cluster.
	 * @return the DataPoint's euclidean distance to a Cluster
	 */
 	public double getCurrentEuDt() {
		return mEuDt;
	}

	/**
	 * Returns the DataPoint name.
	 * @return the DataPoint name
	 */
	public String getObjName() {
		return mObjName;
	}

}