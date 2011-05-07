package info.iylk.dev.faceclustering.mapred;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;
import de.offis.faint.model.Region;

public class FaceWritable extends Face implements Writable {

	private static final long serialVersionUID = -2298863311467678659L;
	
	protected FaceWritable() {
	}

	public FaceWritable(Region region,Double value) {
		super(region,value);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.image = in.readUTF();
		this.x = in.readInt();
		this.y = in.readInt();
		this.height = in.readInt();
		this.width = in.readInt();
		this.value = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.image);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.height);
		out.writeInt(this.width);
		out.writeDouble(this.value);

	}
}