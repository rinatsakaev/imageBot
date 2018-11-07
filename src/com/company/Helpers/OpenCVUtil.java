package Helpers;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVUtil {

    public OpenCVUtil() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void changeBrightness(String filename, double alpha, double beta) {
        Mat source = Imgcodecs.imread(filename, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        source.convertTo(destination, -1, alpha, beta);
        Imgcodecs.imwrite("./output.jpg", destination);
    }

}
