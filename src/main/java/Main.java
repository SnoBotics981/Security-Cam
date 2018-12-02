import edu.wpi.cscore.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.engio.mbassy.listener.Handler;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoWriter;

public class Main {

  public static void main(String[] args) {
    // Loads our OpenCV library. This MUST be included
    System.loadLibrary("opencv_java310");
    System.loadLibrary("ntcore");

    /******************************************************
     * Configure USB Camera (device links are /dev/video#)
     *
     * Standardizing on 320x240 as the video resolution
     ******************************************************/
    UsbCameraManager camera = new UsbCameraManager("Vision Camera", 0);

    // This image feed displays the debug log (whatever the filters computed)
    VideoStream rawView = new VideoStream("Unprocessed Video Feed", "CV Image Stream", 1187);

    // This creates a CvSink for us to use. This grabs images from our selected camera,
    // and will allow us to use those images in OpenCV.  To toggle processing
    // feeds (below), set the source to use a different device.
    CvSink imageSink = new CvSink("CV Image Grabber");
    imageSink.setSource(camera);

    // All Mats and Lists should be stored outside the loop to avoid allocations
    // as they are expensive to create
    Mat inputImage = new Mat();   // Get frame from camera

    System.out.println("Server ready, starting the camera feeds");

    int counter = 0;
    // Infinitely process camera feeds
    while (true) {
      // Grab a frame. If it has a frame time of 0, there was an error.
      // If so, skip and continue
      long frameTime = imageSink.grabFrame(inputImage);
      if (frameTime == 0) continue;

      rawView.source().putFrame(inputImage);
      if (++counter == 50) {
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = now.format(new Date());
        Imgcodecs.imwrite("/home/pi/shared/" + timeStamp + ".jpg", inputImage);
        counter = 0;
      }
      inputImage.release();
      System.gc();
    }
  }

  public static class UsbCameraManager extends UsbCamera {
    public UsbCameraManager(String label, int device) {
      super(label, device);
      this.setResolution(640, 480);
      this.setFPS(10);
    }
  }
}
