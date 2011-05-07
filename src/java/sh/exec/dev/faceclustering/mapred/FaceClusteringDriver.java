package info.iylk.dev.faceclustering.mapred;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import de.offis.faint.controller.MainController;
import de.offis.faint.model.FaceDatabase;
import de.offis.faint.model.Region;

@SuppressWarnings("deprecation")
public class FaceClusteringDriver extends Configured implements Tool {

	private static MainController faint = null;
	private static final String separator = "/";
	private static final String dir = "/tmp";

	@Override
	public int run(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.err.printf("Usage: " + getClass().getSimpleName() + " [generic options] <input-dir> <output-dir> [training-dir]");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		JobClient client = new JobClient();
		JobConf conf = new JobConf(FaceClusteringDriver.class);		
		
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));		
		
		try {
			FileSystem hdfs = FileSystem.get(new Configuration());
			if(hdfs.exists(new Path(dir + args[1])))
				hdfs.delete(new Path(dir + args[1]), true);
		} catch (IOException e) {
			e.printStackTrace();
		}		
					
		if (args.length == 3 && (faint = MainController.getInstance()) != null)
			trainingFaces(args[2]);
		
		System.out.println("Launching a job...");		
		
		conf.setInputFormat(ImageFileInputFormat.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(FaceWritable.class);

		conf.setMapperClass(FaceClusteringMapper.class);
		conf.setReducerClass(FaceClusteringReducer.class);

		conf.setNumMapTasks(1);
		conf.setNumReduceTasks(4);
		
		client.setConf(conf);
		
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;		
	}
	
	private static void trainingFaces(String path) {
		FaceDatabase faceDatabase = faint.getFaceDB();
		Region region;
		String result = "[OK]";
		int i = 0;
		
		System.out.print("Training pre-selected faces... ");
		
		try {
			FileSystem hdfs = FileSystem.get(new Configuration());
			
			// TODO: Optimize this subroutine
			for (FileStatus file : hdfs.listStatus(new Path(path))) {
				StringTokenizer tokenizer = new StringTokenizer(file.getPath().toString(), "/");
				
				tokenizer.nextToken();
				tokenizer.nextToken();
				tokenizer.nextToken();
				tokenizer.nextToken();
				tokenizer.nextToken();
				
				String filename = tokenizer.nextToken();
				
				region = new Region(38, 38, 75, 75, 0.0, path + separator + filename);
				region.toThumbnail(75, 75);
				faceDatabase.put(region, new StringTokenizer(filename, ".").nextToken());
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = "[ERROR]";
		}		
		
		System.out.print(i + " images ");
		System.out.println(result);
		System.out.print("Overwriting old face database... ");
		
		try {
			faceDatabase.writeToDisk();
		} catch (IOException e) {
			result = "[ERROR]";
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new FaceClusteringDriver(), args);
		System.exit(exitCode);
	}
}