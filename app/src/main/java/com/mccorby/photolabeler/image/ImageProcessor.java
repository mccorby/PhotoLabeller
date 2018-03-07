package com.mccorby.photolabeler.image;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {

    private static final String TAG = ImageProcessor.class.getSimpleName();
    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }


    private SharedConfig sharedConfig;

    public ImageProcessor(SharedConfig sharedConfig) {

        this.sharedConfig = sharedConfig;
    }

    public RawImage getImage(Bitmap bitmap) {
        int imageSize = sharedConfig.getImageSize();
        Mat tmp = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);

        Mat newImage = transformImage(tmp);

        float[] values = new float[imageSize * imageSize];
        for (int row = 0; row < newImage.rows(); row++) {
            for (int col = 0; col < newImage.cols(); col++) {
                double[] valueAtPixel = newImage.get(row, col);
                values[row * imageSize + col] = (float) valueAtPixel[0];
            }
        }

        return new RawImage(values);
    }

    private Mat transformImage(Mat image) {
        Mat newImage = new Mat();
        Imgproc.resize(image, newImage, new Size(28, 28));
        return newImage;
    }
}
