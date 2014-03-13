package org.greengin.senseitweb.logic.project.senseit;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.greengin.senseitweb.utils.TimeValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class SenseItPlots {

	public static byte[] createPlot(String label, Vector<TimeValue> data) {
		if (data.size() > 0) {

			XYSeriesCollection dataset = new XYSeriesCollection();

			int nv = data.firstElement().v.length;
			long t0 = data.firstElement().t;

			for (int i = 0; i < nv; i++) {
				XYSeries series = new XYSeries(label + " " + (i + 1));
				for (TimeValue v : data) {
					series.add(v.t - t0, v.v[i]);
				}
				dataset.addSeries(series);
			}

			JFreeChart chart = ChartFactory.createXYLineChart(label, "Time", "", dataset);
			
			chart.getPlot().setOutlineVisible(false);
			chart.getPlot().setBackgroundAlpha(0.f);
			chart.getLegend().setBorder(0,  0,  0,  0);
			chart.setPadding(new RectangleInsets(0,  0,  0,  0));
			
			BufferedImage objBufferedImage = chart.createBufferedImage(360, 240);
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			try {
				ImageIO.write(objBufferedImage, "png", bas);
				byte[] imageData = bas.toByteArray();
				return imageData;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return null;
		}
	}
}
