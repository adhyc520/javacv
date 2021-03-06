package cn.edu.zua.javacv.face;

import cn.edu.zua.javacv.util.ImageUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Random;

import static cn.edu.zua.javacv.util.ImageUtils.*;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

/**
 * @author ascend
 * @date 2018/12/20
 */
public class ClientTest {
    @BeforeClass
    public void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 各参数含义：
     * const Mat& image: 需要被检测的图像（灰度图）
     * vector<Rect>& objects: 保存被检测出的人脸位置坐标序列
     * double scaleFactor: 表示在前后两次相继的扫描中，搜索窗口的比例系数。默认为1.1即每次搜索窗口依次扩大10%;
     * int minNeighbors: 每一个人脸至少要检测到多少次才算是真的人脸
     * int flags： 决定是缩放分类器来检测，还是缩放图像
     * Size(): 表示人脸的最大最小尺寸
     * ---------------------
     * 作者：zuidao3105
     * 来源：CSDN
     * 原文：https://blog.csdn.net/zuidao3105/article/details/79346591
     * 版权声明：本文为博主原创文章，转载请附上博文链接！
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testFace() {
        String faceXml = Client.class.getClassLoader().getResource("data/lbpcascades/lbpcascade_frontalface_improved.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face8.jpg");
        Mat grayMat = gray(image);

        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(grayMat, faceDetections,1.1, 5, 0, new Size(40, 40));
        MatOfInt numDetections = new MatOfInt();
        faceDetector.detectMultiScale2(grayMat, faceDetections, numDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        for (int i : numDetections.toArray()) {
            System.out.println("i = " + i);
        }

        showImg(image);
    }

    /**
     * 1
     * image	Matrix of the type CV_8U containing an image where objects are detected.
     * objects	Vector of rectangles where each rectangle contains the detected object, the rectangles may be partially outside the original image.
     * scaleFactor	Parameter specifying how much the image size is reduced at each image scale.
     * minNeighbors	Parameter specifying how many neighbors each candidate rectangle should have to retain it.
     * flags	Parameter with the same meaning for an old cascade as in the function cvHaarDetectObjects. It is not used for a new cascade.
     * minSize	Minimum possible object size. Objects smaller than that are ignored.
     * maxSize	Maximum possible object size. Objects larger than that are ignored. If maxSize == minSize model is evaluated on single scale.
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testFace2() {
        String faceXml = Client.class.getClassLoader().getResource("data/lbpcascades/lbpcascade_frontalface_improved.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face8.jpg");
        Mat grayMat = gray(image);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(grayMat, faceDetections, 1.1, 5, 0, new Size(40, 40));
        MatOfInt numDetections = new MatOfInt();
        faceDetector.detectMultiScale2(grayMat, faceDetections, numDetections);
//        faceDetector.detectMultiScale3(grayMat, faceDetections, numDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (int i = 0; i < numDetections.toArray().length; i++) {
            int numDetection = numDetections.toArray()[i];
            Rect rect = faceDetections.toArray()[i];
            // 22 4 9
//            System.out.println(numDetection);
            if (numDetection > 0) {
                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
            }
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testFace3() {
        String faceXml = Client.class.getClassLoader().getResource("data/logo/logo/cascade.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/logo/tmp/logo7.jpg");
        Mat grayMat = gray(image);

        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(grayMat, faceDetections,1.1, 3, 0);
        // CV_HAAR_DO_CANNY_PRUNING
        faceDetector.detectMultiScale(grayMat, faceDetections, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testFace4() {
        String faceXml = Client.class.getClassLoader().getResource("data/logo/logo/cascade.xml").getPath().substring(1);
        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);

        int frame = 2000;
        for (int i = 0; i < 30; i++) {
            Mat mat = convertToOrgOpenCvCoreMat(findFrame(frame));
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(gray(mat), faceDetections, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING, new Size(80, 45), new Size(120, 80));
            for (Rect rect : faceDetections.toArray()) {
                Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
            }
            showImg(mat, 2);
            frame += 100;
        }
    }

    /**
     * CV_HAAR_DO_CANNY_PRUNING =    1
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testEye() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_eye.xml").getPath().substring(1);

        CascadeClassifier detector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face6.jpg");

        MatOfRect detections = new MatOfRect();
        Mat detectorMat = ImageUtils.equalizeHist(gray(image));
        detector.detectMultiScale(detectorMat, detections, 1.5, 5, CV_HAAR_DO_CANNY_PRUNING, new Size(40, 40));

        System.out.println(String.format("Detected %s eye", detections.toArray().length));
        for (Rect rect : detections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testBody() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_fullbody.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face7.jpg");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testSmile() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_smile.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face6.jpg");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    // ----------------显示图片

    private void showImg(Mat mat) {
        HighGui.imshow("结果", mat);
        HighGui.waitKey(1000);
    }

    private void showImg(Mat mat, int second) {
        HighGui.imshow("结果", mat);
        HighGui.waitKey(second * 1000);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogo() {
        Mat logo = Imgcodecs.imread("/tmp/logo/pos/images/logo_40_result.jpg");
        Mat grayMat = gray(logo);
        int topRow = 0;
        int leftCol = 0;
        // 开始
        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < grayMat.width(); col++) {
                System.out.print(grayMat.get(row, col)[0]);
                System.out.print("\t");
            }
            System.out.println();
        }
//        showImg(logo);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoPos() {
//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(700));
//        showImg(cut(mat, 26, 30, 102, 52));
//        Mat small = ImageUtils.cut(mat, 28, 30, 51, 51);
//        showImg(small);

        int index = 200;
        int frame = 200;

        for (int i = 0; i < 200; i++) {
            Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));
            Mat small = ImageUtils.cut(mat, 26, 30, 102, 52);
            ImageUtils.save("/tmp/logo/pos/" + (index++) + ".jpg", gray(resize(small, new Size(51, 26))));
            frame += 200;
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoNeg() {
//        Frame frame = findFrame(30);
//        Mat result = ImageUtils.convertToOrgOpenCvCoreMat(frame);
//        showImg(result);
//        System.out.println("result = " + result.width());
//        System.out.println("result.height() = " + result.height());


//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(500));
//        showImg(mat);
//        Mat small = ImageUtils.cut(mat, 28, 30, 51, 51);
//        showImg(small);

        int index = 1;
        int frame = 1000;

        for (int i = 0; i < 1000; i++) {
            Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));

            Mat neg1 = cut(mat, 320, 120, 60, 60);
            Mat neg2 = cut(mat, 320, 180, 60, 60);
            Mat neg3 = cut(mat, 320, 240, 60, 60);


            ImageUtils.save("/tmp/logo/neg/" + (index++) + ".jpg", gray(neg1));
            ImageUtils.save("/tmp/logo/neg/" + (index++) + ".jpg", gray(neg2));
            ImageUtils.save("/tmp/logo/neg/" + (index++) + ".jpg", gray(neg3));
            frame += 25;
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoNeg2() {
//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(2400));
//        Mat smallMat = ImageUtils.removeBlackEdge(mat);
//        Mat neg1 = ImageUtils.cut(smallMat, 600, 260, 200, 100);
//        showImg(ImageUtils.gray(neg1));

        Random random = new Random();
        int index = 1;
        int frame = 100;
        int base = 100;
        int inc = 200;

        for (int i = 0; i < 500; i++) {
            try {
                Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));
                Mat smallMat = ImageUtils.removeBlackEdge(mat);
                Mat grayMat = gray(smallMat);
                Mat sunMat1;
                sunMat1 = cut(grayMat, 130, 85, 500, 390);

                int value = base + random.nextInt(inc);
                Mat neg1 = resize(sunMat1, new Size(value, value));

                ImageUtils.save("/tmp/logo/neg2/" + (index++) + ".jpg", neg1);
//                ImageUtils.save("/tmp/neg/sayout_" + (index++) + ".jpg", neg1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame += 100;
        }

    }


    /**
     * 从视频中找出指定帧
     *
     * @return 指定帧
     */
    @SuppressWarnings("Duplicates")
    private Frame findFrame(int frameIndex) {
        Frame frame = null;
        String fileName = "Z:/tmp/logo/test2.mp4";
        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(fileName)) {
            fFmpegFrameGrabber.start();
            fFmpegFrameGrabber.setFrameNumber(frameIndex);
            Frame srcFrame = fFmpegFrameGrabber.grabImage();
            if (srcFrame != null) {
                frame = srcFrame.clone();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return frame;
    }


    @Test
    @SuppressWarnings("Duplicates")
    public void createNegTxt() {
        String path = "Z:\\tmp\\logo\\neg\\";
        File txtFile = new File("Z:\\tmp\\logo\\neg.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos, true);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            pw.println("neg/" + file.getName());
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void createPosTxt() {
        String path = "Z:\\tmp\\logo\\pos\\";
        File txtFile = new File("Z:\\tmp\\logo\\pos.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos, true);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            Mat mat = Imgcodecs.imread(file.getAbsolutePath());
            pw.println("pos/" + file.getName() + " 1 0 0 " + mat.width() + " " + mat.height());
        }

    }

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 11; i++) {
            Mat mat = Imgcodecs.imread("Z:\\tmp\\logo\\pos\\result_0" + (i + 1) + ".jpg");
            Mat grayMat = gray(mat);
            ImageUtils.save("/tmp/logo/pos/result_" + (i + 1) + ".jpg", grayMat);
        }
    }

    @Test
    public void test2() {
        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(525));
        showImg(mat);



    }
    @Test
    public void test3() {
        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(525));
        showImg(mat);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Net net = Dnn.readNetFromDarknet("/Users/tianyubing/keras-yolo3/yolov3.cfg","/Users/tianyubing/keras-yolo3/yolov3.weights");
        net.setPreferableBackend(Dnn.DNN_BACKEND_OPENCV);
        net.setPreferableTarget(Dnn.DNN_TARGET_CPU);

        Mat blob = new Mat();
        Dnn.blobFromImage(blob,1/255.0,new Size(blob.width(), blob.height()),new Scalar(0,0,0), true, false);
        net.setInput(blob);
    }
}
