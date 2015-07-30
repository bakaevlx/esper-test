package org.my.cep.espertest.eventsrc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.my.cep.espertest.event.MultiSensorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Reads sensor data from a file generated by OMNeT. Clears the file after
 * reading and appends the data to an archive file.
 * 
 * TODO Use a database instead of dealing with file lock portability issues etc.
 *
 */
@Component
public class SharedFileMultiSensorEventSource implements
		EventSource<MultiSensorEvent> {
	@Autowired
	private Environment env;
	private int eventCount = 0;
	// data and archive files
	private String sharedDataFilePath;
	private String archiveFilePath;
	// 
	

	@Override
	public void init() throws IOException {
		sharedDataFilePath = env.getProperty("omnet.basedir") + "/"
				+ env.getProperty("omnet.datafile");
		archiveFilePath = env.getProperty("omnet.basedir") + "/"
				+ env.getProperty("omnet.archivefile");
		System.out.println("sharedDataFilePath=" + sharedDataFilePath
				+ " archiveFilePath=" + archiveFilePath);
		
		clearArchive();
		
	}

	@Override
	public List<MultiSensorEvent> getEventsWithinTimeInterval(int tFrom, int tTo) {
		try {
			String data = readAndCleartextSharedDataFile();
			if(data != null && data.length() > 0) {				
				MultiSensorEvent e = readSensorData(data);
				// TODO optimize this
				if(e.getTemperature() <= 0 || e.getSpeed() <= 0) {
					 System.out.println("discard: missing data, temp or speed");
					 return null;
				} else {
					return (List<MultiSensorEvent>) Arrays
						.asList(new MultiSensorEvent[] { e }) ;
				}
			} else {
				return null;
			}

		} catch (IOException e) {
			throw new RuntimeException("SharedFileMultiSensorEventSource error", e);
		}
	}
	
	MultiSensorEvent readSensorData(String data) throws IOException {
		
		MultiSensorEvent res = null;
		double[] val = new double[]{0,0};
		int[] count = new int[]{0,0};	// temp, speed


		StringTokenizer st = new StringTokenizer(data, "\n");
		while (st.hasMoreTokens()) {
			String s = st.nextToken().trim();
	         System.out.println("sensor reading: " + s);
	         // todo regex validation
	         if(s != null && s.length() > 0) {
	        	 int idx = s.indexOf("temp") != -1 ? 0 : 1;
	        	 count[idx]++;
	        	 val[idx] += getVal(s);
	         }
	     }
		
		res = new MultiSensorEvent(eventCount++, Calendar.getInstance().getTime(), 
				(int)Math.round(val[0]/count[0]), (int)Math.round(val[1]/count[1]));
		System.out.println(res);
		return res;
	}

	String readAndCleartextSharedDataFile() throws IOException {

		String res = null;

		File f = new File(sharedDataFilePath);
		if (!f.exists())
			throw new FileNotFoundException("no shared data file");

		// Tries to prevent concurrent access from OMNeT. But It wouldn't work
		// on Linux. And not even sure if it works reliably for text files on
		// Win. Better use a DB.
		try (RandomAccessFile raf = new RandomAccessFile(f, "rw");
				FileChannel fc = raf.getChannel();) {
			if (!isAlreadyOpen(fc) && raf.length() > 0) {
					byte[] data = new byte[(int) raf.length()];
					raf.read(data);
					res = new String(data);
					// clear & appendToArchive
					raf.setLength(0L);
					appendToArchive(res);
			}
		} catch (IOException e) {
			System.out.println("File already open?\n"
					+ e.getMessage());
		}

		System.out.println("sharedData: " + res);
		return res;

	}

	private void clearArchive() throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(new File(archiveFilePath), "rw");) {
					raf.setLength(0L);
		} 
		System.out.println("cleared: " + archiveFilePath);
	}
	
	private void appendToArchive(String s) throws IOException {
		final boolean append = true;
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(archiveFilePath, append)))) {
			out.println(s);
		}
	}

	private boolean isAlreadyOpen(final FileChannel fc) throws IOException {
		boolean isOpen = false;
		FileLock lock = null;
		try {
			if ((lock = fc.tryLock()) == null) {
				isOpen = true;
			}
		} catch (IOException e) {
			isOpen = true;
		} finally {
			if (lock != null) {
				System.out.println("release lock");
				lock.release();
			}
		}
		System.out.println("isOpen=" + isOpen);
		return isOpen;
	}
	
	private int getVal(String line) {
		int n = line.lastIndexOf(':');
		return Integer.parseInt(line.substring(n + 1).trim());
	}

}
