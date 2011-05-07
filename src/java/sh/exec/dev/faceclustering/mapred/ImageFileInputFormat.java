package info.iylk.dev.faceclustering.mapred;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class ImageFileInputFormat extends FileInputFormat<NullWritable, Path> {
	
	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return false;
	}
	
	@Override
	public RecordReader<NullWritable, Path> getRecordReader(InputSplit split, JobConf job, Reporter reporter) throws IOException {
		return (RecordReader) new ImageFileRecordReader((FileSplit) split, job);
	}
}
