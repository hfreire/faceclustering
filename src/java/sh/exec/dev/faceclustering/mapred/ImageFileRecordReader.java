package info.iylk.dev.faceclustering.mapred;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;

@SuppressWarnings("deprecation")
class ImageFileRecordReader implements RecordReader<NullWritable, Path> {
	private FileSplit fileSplit;
	private Configuration conf;
	private boolean processed = false;
	
	public ImageFileRecordReader(FileSplit fileSplit, Configuration conf) throws IOException {
	
		this.fileSplit = fileSplit;
		this.conf = conf;
	}
	
	@Override
	public NullWritable createKey() {
		return NullWritable.get();
	}
	
	@Override
	public Path createValue() {
		return fileSplit.getPath();
	}
	
	@Override
	public long getPos() throws IOException {
		return processed ? fileSplit.getLength() : 0;
	}
	
	@Override
	public float getProgress() throws IOException {
		return processed ? 1.0f : 0.0f;
	}
	
	@Override
	public boolean next(NullWritable key, Path value) throws IOException {
		if (!processed) {
			value = fileSplit.getPath();
			processed = true;
			return true;
		}
		return false;
	}
	
	@Override
	public void close() throws IOException {
	// do nothing
	}
}
