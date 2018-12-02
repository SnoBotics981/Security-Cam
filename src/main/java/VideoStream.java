import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;

public class VideoStream {
  private static CameraServer manager = CameraServer.getInstance();

  private MjpegServer stream;
  private CvSource source;
  private String title, sourceName;
  private int port;

  public VideoStream(String inputSource, String title, int port) {
    this.title = title;
    this.port = port;
    this.sourceName = inputSource;
    stream = enable();
    updateSource();
  }

  public void disable() { manager.removeServer(title); }
  public MjpegServer enable()  { return manager.addServer(title, port); }
  public CvSource source() { return source; }

  private void updateSource() {
    this.source = new CvSource(
      sourceName,
      VideoMode.PixelFormat.kMJPEG,
      640, 480, 10);
    stream.setSource(source);
  }
}
