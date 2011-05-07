package info.iylk.dev.faceclustering.cluster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusterAnalysis {
    private ArrayList<Cluster> clusters;
    private double mNoise;
    private List<DataPoint> mDataPoints;
    private double mSWCSS;

    /**
     * Constructor for the ClusterAnalysis class.
     * @param k the initial number of centroids
     * @param noise sensitivity of the cluster
     * @param dataPoints the DataPoints to cluster
     */
    public ClusterAnalysis(int k, double noise, List<DataPoint> dataPoints) {
        clusters = new ArrayList<Cluster>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster("Cluster" + i));
        }
        mNoise = noise;
        mDataPoints = dataPoints;
    }

    /**
     * Calculates the sum of squares of all clusters
     */
    private void calcSWCSS() {
        double temp = 0;
        for (Cluster c : clusters) {
            temp += c.getSumSqr();
        }
        mSWCSS = temp;
    }

    /**
     * Starts the cluster algorithm
     */
    public void startAnalysis() {
    	
    	//set starting centroid positions
        setInitialCentroids();
        Iterator<DataPoint> n = mDataPoints.iterator();
        
        //assign DataPoint to clusters
        loop1:
        while (true) {
            for (Cluster c : clusters) 
            {
                c.addDataPoint(n.next());
                if (!n.hasNext())
                    break loop1;
            }
        }
        
        //calculate E for all the clusters
        calcSWCSS();
        
        //recalculate Cluster centroids
        for (Cluster c : clusters) {
            c.getCentroid().calcCentroid();
        }
        
        //recalculate E for all the clusters
        calcSWCSS();
        
        // check for points outside allowed noise
        boolean add = false;
        double Cx = -1;
        double Cy = -1;
        loop:
        while(true) {
        	add = false;
        	// check if points are in the closest cluster
        	dataPointsShifting();
        	for(Cluster c : clusters) {
        		for(Iterator<DataPoint> k = c.getDataPoints().iterator(); k.hasNext(); ) {
        			DataPoint dp = k.next();
        			double EuDt = dp.getCurrentEuDt();
        			// noise threshold passed: mark for addition of new Cluster
        			if(EuDt > mNoise) {
        				add = true;
        				Cx = dp.getX();
        				Cy = dp.getY();		
        			}
        		}
        	}
        	if(add) {
        		Cluster addCluster = new Cluster("Cluster"+clusters.size());
        		Centroid addCentroid = new Centroid(Cx, Cy);
        		addCentroid.setCluster(addCluster);
        		addCluster.setCentroid(addCentroid);
        		clusters.add(addCluster);
        	}
        	else
        		break loop;
        }
        
        // remove empty clusters
        int i = 0;
    	ArrayList<Integer> nullClusters = new ArrayList<Integer>();
    	for(Cluster c : clusters) {
    		if(c.getDataPoints().isEmpty())
    			nullClusters.add(i);
    		i++;
    	}
    	i = 0;
    	for(Integer r : nullClusters) {
    		clusters.remove(r.intValue() - i);
    		i++;
    	}

    }
    
    /**
     * Checks if DataPoints are in the closest Cluster,
     * if not, shifts them to the nearest one.
     */
    private void dataPointsShifting() {
    	
    	//enter the loop for cluster
        for (Cluster c : clusters) {
        	for (Iterator<DataPoint> k = c.getDataPoints().iterator(); k.hasNext(); ) {
        		DataPoint dp = k.next();
                
                // pick the first element of the first cluster
                // get the current euclidean distance
                double tempEuDt = dp.getCurrentEuDt();
                Cluster tempCluster = null;
                boolean matchFoundFlag = false;
    
                //call testEuclidean distance for all clusters
                for (Cluster d : clusters) {
                    
                	// check if point if eligible for shifting to other cluster
                	if (tempEuDt > dp.testEuclideanDistance(d.getCentroid())) {
                    	tempEuDt = dp.testEuclideanDistance(d.getCentroid());
                        tempCluster = d;
                        matchFoundFlag = true;
                    }    
                }
                if (matchFoundFlag) {
                    tempCluster.addDataPoint(dp);
                    k.remove();
                    for (Cluster d : clusters) {
                    	d.getCentroid().calcCentroid();
                    }
                    calcSWCSS();
                }
            }
        }
    }

    /**
     * Returns a list of DataPoints.
     * @return a list of DataPoints
     */
    public List<DataPoint>[] getClusterOutput() {
        List<DataPoint>[] v = new List[clusters.size()];
        int i =0;
        for (Cluster c : clusters) {
            v[i] = c.getDataPoints();
            i++;
        }
        return v;
    }
    
    /**
     * Sets the initial centroids position
     */
    private void setInitialCentroids() {
        //kn = (round((max-min)/k)*n)+min where n is from 0 to (k-1).
        double cx = 0, cy = 0;
        for (int n = 1; n <= clusters.size(); n++) {
            cx = (((getMaxXValue() - getMinXValue()) / (clusters.size() + 1)) * n) + getMinXValue();
            cy = (((getMaxYValue() - getMinYValue()) / (clusters.size() + 1)) * n) + getMinYValue();
            Centroid c1 = new Centroid(cx, cy);
            clusters.get(clusters.size() - 1).setCentroid(c1);
            c1.setCluster(clusters.get(clusters.size() - 1));
        }
    }

    private double getMaxXValue() {
        double temp = mDataPoints.get(0).getX();
        for (DataPoint dp : mDataPoints) {
            temp = (dp.getX() > temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMinXValue() {
        double temp = 0;
        temp = mDataPoints.get(0).getX();
        for (DataPoint dp : mDataPoints) {
            temp = (dp.getX() < temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMaxYValue() {
        double temp = 0;
        temp = mDataPoints.get(0).getY();
        for (DataPoint dp : mDataPoints) {
            temp = (dp.getY() > temp) ? dp.getY() : temp;
        }
        return temp;
    }

    private double getMinYValue() {
        double temp = 0;
        temp = mDataPoints.get(0).getY();
        for (DataPoint dp : mDataPoints) {
            temp = (dp.getY() < temp) ? dp.getY() : temp;
        }
        return temp;
    }

    public int getKValue() {
        return clusters.size();
    }

    public double getNoise() {
        return mNoise;
    }

    public int getTotalDataPoints() {
        return mDataPoints.size();
    }

    public double getSWCSS() {
        return mSWCSS;
    }

    public Cluster getCluster(int pos) {
        return clusters.get(pos);
    }
}