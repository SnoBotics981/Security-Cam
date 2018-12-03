import edu.wpi.cscore.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import net.engio.mbassy.listener.Handler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfFloat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;

public class Main {

  public static void main(String[] args) {
    // Loads our OpenCV library. This MUST be included
    System.loadLibrary("opencv_java310");
    //** System.loadLibrary("ntcore");

    /******************************************************
     * Configure USB Camera (device links are /dev/video#)
     *
     * Standardizing on 320x240 as the video resolution
     ******************************************************/
    UsbCameraManager camera = new UsbCameraManager("Vision Camera", 0);

    // This image feed displays the debug log (whatever the filters computed)
    //** VideoStream rawView = new VideoStream("Unprocessed Video Feed", "CV Image Stream", 1187);

    // This creates a CvSink for us to use. This grabs images from our selected camera,
    // and will allow us to use those images in OpenCV.  To toggle processing
    // feeds (below), set the source to use a different device.
    CvSink imageSink = new CvSink("CV Image Grabber");
    imageSink.setSource(camera);

    // All Mats and Lists should be stored outside the loop to avoid allocations
    // as they are expensive to create
    Mat inputImage = new Mat();   // Get frame from camera
    ArrayList<Mat> histogram = new ArrayList<Mat>();
    long frameTime = 0;

    // Need a valid frame to preload the histogram
    while (frameTime == 0) {
      frameTime = imageSink.grabFrame(inputImage);
    }

    // getting the first histogram
    getHistogram(inputImage, histogram);

    System.out.println("Server ready, starting the camera feeds");

    int counter = 0;
    double correlation = 0.0;
    // Infinitely process camera feeds
    while (true) {
      // Grab a frame. If it has a frame time of 0, there was an error.
      // If so, skip and continue

      // grabing a frame

      frameTime = imageSink.grabFrame(inputImage);
      if (frameTime == 0) continue;

      //** rawView.source().putFrame(inputImage);

      // Rob changed to 1 frame per second

      if (++counter == 1) {
        getHistogram(inputImage, histogram);
	correlation = Imgproc.compareHist(histogram.get(0), histogram.get(1), Imgproc.HISTCMP_CORREL);
	//** System.out.println("Correlation: " + correlation);
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = now.format(new Date());

	if (correlation < 0.8) {
	    Imgcodecs.imwrite("/home/pi/shared/" + timeStamp + ".jpg", inputImage);
	    //** System.out.println ("write image");
	} else {
	    //** System.out.println ("skip image");
	    // Want to copy histogram 1 into 0
	    histogram.get(1).copyTo(histogram.get(0));
	}
        counter = 0;
      }
      inputImage.release();
      System.gc();
    }
  }

  public static void getHistogram(Mat image, ArrayList<Mat> histogram) {
    Mat hsv = new Mat();
    Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);
    histogram.add(new Mat());
    Imgproc.calcHist(
        new ArrayList<>(Arrays.asList(image)),
        new MatOfInt(0, 1, 2),
        new Mat(),
        histogram.get(histogram.size() - 1),
        new MatOfInt(30, 64, 64),
        new MatOfFloat(0, 180, 0, 256, 0, 256)
    );
    Core.normalize(
        histogram.get(histogram.size() - 1).clone(),
        histogram.get(histogram.size() - 1)
    );
    if (histogram.size() > 2) {
      histogram.remove(0);
    }
  }

  public static class UsbCameraManager extends UsbCamera {
    public UsbCameraManager(String label, int device) {
      super(label, device);
      this.setResolution(640, 480);
      this.setFPS(30);
    }
  }
}
