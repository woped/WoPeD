package org.woped.gui.images.svg.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;

import org.pushingpixels.flamingo.api.svg.SvgTranscoder;
import org.pushingpixels.flamingo.api.svg.TranscoderListener;

public class SvgBatchConverter {
	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            First parameter should point to a folder with SVG images, and
	 *            the second parameter should be the package name for the
	 *            transcoded classes.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("param 0 : dir or svg-file, param 1 : package of java files");
			System.exit(1);
		}

		File file = new File(args[0]);
		if (file.getName().endsWith(".svg")) {
			if (file.exists()) {
				convert(file.getParent(), file, args[1]);
			}
			else {
				System.err.println("File " + args[0] + " does not exist");
				return;
			}
		}
		else {	
			File dir = new File(args[0]);
		
			if (!dir.exists()) {
					System.err.println("Folder " + args[0] + " does not exist");
					return;
				}

			for (File file1 : dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".svg");
				}
			})) {
				convert(args[0], file1, args[1]);
			}
		}
	}
	
	public static void convert(String dir, File file, String pkg) {
		String svgClassName = file.getName().substring(0, file.getName().length() - 4);
		svgClassName = svgClassName.replace('-', '_');
		svgClassName = svgClassName.replace(' ', '_');
		String javaClassFilename = dir + File.separator + svgClassName + ".java";
		System.out.print("Converting " + dir + File.separator + file.getName() + " to " + javaClassFilename);
		
		try {
			final CountDownLatch latch = new CountDownLatch(1);
			final PrintWriter pw = new PrintWriter(javaClassFilename);

			SvgTranscoder transcoder = new SvgTranscoder(file.toURI()
					.toURL().toString(), svgClassName);
			transcoder.setJavaToImplementResizableIconInterface(true);
			transcoder.setJavaPackageName(pkg);
			transcoder.setListener(new TranscoderListener() {
				public Writer getWriter() {
					return pw;
				}

				public void finished() {
					latch.countDown();
				}
			});
			transcoder.transcode();
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(" ... done");
	}
}
