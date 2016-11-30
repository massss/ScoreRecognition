package scoreRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.lang.Math;

/**
 * Created by ev50063 on 2016/11/08.
 */
public class StaveProcessor {
    private Mat linesMat;
    private Mat edgesMat;
    private double rho = 3.0;
    private double theta = Math.PI / 180.0 * 90.0;
    private int threshold = 300;
    //private double srn = 0.0;
    //private double stn = 0.0;
    //private double minTheta = 80.0;
    //private double maxTheta = 100.0;
    
    private int minLineLength = 0;
    private int maxLineGap = 3;
    
    private Mat cannyedMat;
    private Mat linedMat;
    private Mat outputMat;
    
    private int linesNum;
    private double[] meanY;
    
    StaveProcessor(Mat inputMat) {
        TestClass test = new TestClass();
        edgesMat = new Mat();
        linesMat = new Mat();
        linedMat = inputMat.clone();
        outputMat = new Mat();
            //Imgproc.HoughLines();
            //Imgproc.HoughLines(inputMat, lines, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        
            //Imgproc.HoughLines(inputMat, linesMat, rho, theta, threshold);
            //Imgproc.HoughLinesP(inputMat, lines, rho, theta, threshold, minLineLength, maxLineGap);
        
            //Imgproc.Canny(inputMat, edgesMat, 150,100);
            //Imgproc.Canny(inputMat, edgesMat, 80, 100);
            //ImageViewer viewer = new ImageViewer();
            //viewer.show(edgesMat, "edgesMat");
            //Imgproc.HoughLinesP(edgesMat, linesMat, 5, Math.PI/180, 200, 30, 5);
        
            //Imgproc.HoughLinesP(edgesMat, linesMat, 1, Math.PI / 180 , 50, 100 ,10);
            //Imgproc.HoughLines(edgesMat, linesMat, 5, Math.PI/180, threshold);
            //Imgproc.HoughLines(edgesMat, linesMat, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        //Imgproc.HoughLines(inputMat, linesMat, rho, theta, threshold, srn, stn, minTheta, maxTheta);
            //Imgproc.HoughLinesP(inputMat, linesMat, rho, theta, threshold);
    
        minLineLength = (int)(inputMat.cols() * 0.6);
        Imgproc.HoughLinesP(inputMat, linesMat, rho, theta, threshold, minLineLength, maxLineGap);
        //Imgproc.HoughLinesP(inputMat, linesMat, rho, theta, threshold);
        //System.out.println("HoughLines - " + inputMat + "\n   by rho:" + rho + ", theta:" + theta + ", threshold:" + threshold + ", srn:" + srn + ", stn:" + stn + ", minTheta:" + minTheta + ", maxTheta:" + maxTheta);
        linesNum = linesMat.rows();
        System.out.println("HoughLinesP - " + inputMat);
        System.out.println("    by rho:" + rho + ", theta:" + theta + ", threshold:" + threshold + ", minLineLength:" + minLineLength + ", maxLineGap:" + maxLineGap);
        test.matInfo(linesMat, "linesMat");
        //test.matChannel(linesMat, 4);
        sortStave();
        drawLines();
        test.matChannel(linesMat, 4);
        test.matInfo(linedMat, "linedMat");
    }
    
    private void sortStave() {
        double tempLX, tempLY, tempRX, tempRY;
        double mean1, mean2;
        for (int i = 0; i < linesNum - 1; i++) {
            for (int j = 0; j < linesNum - i - 1; j++) {
                mean1 = (linesMat.get(j, 0)[1] + linesMat.get(j, 0)[3]) / 2;
                mean2 = (linesMat.get(j + 1, 0)[1] + linesMat.get(j + 1, 0)[3]) / 2;
                if (mean1 > mean2) {
                    tempLX = linesMat.get(j, 0)[0];
                    tempLY = linesMat.get(j, 0)[1];
                    tempRX = linesMat.get(j, 0)[2];
                    tempRY = linesMat.get(j, 0)[3];
                    linesMat.put(j, 0, linesMat.get(j + 1, 0)[0], linesMat.get(j + 1, 0)[1], linesMat.get(j + 1, 0)[2], linesMat.get(j + 1, 0)[3]);
                    linesMat.put(j + 1, 0, tempLX, tempLY, tempRX, tempRY);
                }
            }
        }
        System.out.println("Sorted stave");
    }
    
    private void calcWidth() {
        meanY = new double[linesNum];
        double[] yDif = new double[linesNum - 1];
        for (int i = 0; i < linesNum; i++) {
            meanY[i] = (linesMat.get(i, 0)[1] + linesMat.get(i, 0)[3]) / 2;
            if (i > 0) yDif[i - 1] = meanY[i] - meanY[i - 1];
        }
        
    }
    
    private void drawLines() {
        //double[] lines = new double[4];
        Imgproc.cvtColor(linedMat, linedMat, Imgproc.COLOR_GRAY2BGR);
        Scalar color = new Scalar(0, 255, 0);
        for (int i = 0; i < linesMat.rows(); i++) {
            //lines[i%4] = linesMat.get(i, 0)[0];
            Imgproc.line(linedMat, new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1]), new Point(linesMat.get(i, 0)[2], linesMat.get(i, 0)[3]), color);
            String lineNum = "";
            Imgproc.putText(linedMat, lineNum+i, new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1]), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 255, 255), 2);
        }
        System.out.println("Draw lines for detected stave");
    }
    
    private void removeStave() {
        Mat linesRoi;
        Point left;
        Point right;
        //double
        for (int i = 0; i < linesMat.rows(); i++) {
            left = new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1]);
            right = new Point(linesMat.get(i, 0)[2], linesMat.get(i, 0)[3]);
            
        }
    }
    
    public Mat getLinesMat() {
        return linesMat;
    }
    
    public Mat getLinedMat() {
        return linedMat;
    }
    
    public Mat getOutputMat() {
        return outputMat;
    }
}