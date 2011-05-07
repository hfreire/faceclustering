package info.iylk.dev.faceclustering.mapred;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import de.offis.faint.controller.MainController;
import de.offis.faint.model.ImageModel;
import de.offis.faint.model.Region;

@SuppressWarnings("deprecation")
public class FaceClusteringMapper extends MapReduceBase implements
		Mapper<NullWritable, Path, Text, FaceWritable> {

	private MainController faint = null;
	private static final String separator = "/";
	private static final String sourceDirectory = "src";

	@Override
	public void map(NullWritable key, Path value,
			OutputCollector<Text, FaceWritable> output,
			Reporter reporter) throws IOException {

		if ((faint = MainController.getInstance()) != null) {
			System.out.println("Running mapper...");

			// Detect faces from input image
			ImageModel imageModel = new ImageModel(sourceDirectory + separator
					+ value.getName());
			imageModel.initThumbnail();
			Region[] regions = faint.detectFaces(imageModel, false);

			System.out.println("Processing image: " + value.getName());
			System.out.println("Number of regions detected: " + regions.length);

			// For each face detected try to recognize it based on the eigen
			// vectors from the faces previously learned
			for (Region region : regions) {
				region.cacheToDisk();
				region.setUsedForTraining(false);

				HashMap<String, Double> points = faint.recognizeFace(region);
				
				System.out.println("HASH: " + points);
				
				//Detect the low value
				Double lowValue=Double.MAX_VALUE;
				String reduceKey="";
				
				for (String person : points.keySet()) {
					
					if (points.get(person) < lowValue) {
						reduceKey = person;
						lowValue = points.get(reduceKey);
					}
				}
				output.collect(new Text(reduceKey),new FaceWritable(region, lowValue));	
			}
		}
	}
}