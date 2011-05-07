package info.iylk.dev.faceclustering.mapred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import info.iylk.dev.faceclustering.cluster.ClusterAnalysis;
import info.iylk.dev.faceclustering.cluster.DataPoint;


@SuppressWarnings("deprecation")
public class FaceClusteringReducer extends MapReduceBase implements
		Reducer<Text, FaceWritable, Text, Text> {

	private double noise = 50;
	
	public void reduce(Text key, Iterator<FaceWritable> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		List<DataPoint> dataPoints = new ArrayList<DataPoint>();
		String out = new String();
		
		// Conversion of Faces to DataPoints
		 while (values.hasNext()) {
			 Face face = values.next();
			 DataPoint dp = new DataPoint(face.getValue(), 0, face.getImage());
			 dataPoints.add(dp);
		 }
		 
		 // Clustering algorithm
		 ClusterAnalysis ca = new ClusterAnalysis(1, noise, dataPoints);
		 ca.startAnalysis();
		 int i = 0;
		 for(List<DataPoint> tempV : ca.getClusterOutput()) {
			 out += "\n-----Cluster" + i + "-----\n";
			 for(DataPoint dpTemp : tempV) {
				 out += dpTemp.getObjName() + "(" + dpTemp.getX() + ")\n";
			 }
			 i++;
		 }
		 output.collect(key, new Text(out));
	}
}
