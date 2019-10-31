package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.chanlun.yx.data.dto.BiLine;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.SimpleRecord;

public class BiLineUtils {

	public static List<Point> contructBiLine(List<SimpleRecord> records) {

		List<Point> newRecords = new ArrayList<Point>();
		// 默认第一个为确认的底分型
		Point currentPoint = new Point();
		Point tempPoint = null;
		currentPoint.setType(0);
		currentPoint.setPrice(0);
		currentPoint.setTime("0");

		int i = 1;// 三条确认一个分型，分型的中心为i
		while (i < records.size() - 1) {

			// 遇到顶分型
			if (records.get(i).getHigh() > records.get(i - 1).getHigh()
					&& records.get(i).getHigh() > records.get(i + 1).getHigh()) {

				// 最后一个确认的分型为底分型
				if (currentPoint.getType() == 0) {

					// 是否存在临时顶分型，以及本次分型大于临时分型
					if (tempPoint != null && records.get(i).getHigh() > tempPoint.getPrice()) {

						// 找到一个顶分型，存入临时节点
						tempPoint.setPrice(records.get(i).getHigh());
						tempPoint.setType(1);
						tempPoint.setTime(records.get(i).getTime());

					}

					if (tempPoint == null && records.get(i).getHigh() > currentPoint.getPrice()) {

						// 找到一个顶分型，存入临时节点
						tempPoint = new Point();
						tempPoint.setPrice(records.get(i).getHigh());
						tempPoint.setType(1);
						tempPoint.setTime(records.get(i).getTime());
					}
				} else {

					if (records.get(i).getHigh() > tempPoint.getPrice()) {// 低分型确认

						newRecords.add(tempPoint);
						BeanUtils.copyProperties(tempPoint, currentPoint);

						tempPoint = new Point();
						tempPoint.setType(1);
						tempPoint.setPrice(records.get(i).getHigh());
						tempPoint.setTime(records.get(i).getTime());

					}
				}
			}

			if (records.get(i).getLow() < records.get(i - 1).getLow()
					&& records.get(i).getLow() < records.get(i + 1).getLow()) {
				// 底分型
				if (currentPoint.getType() == 0) {// 原来为底分型，说明临时的都是顶分型，需要遇到下一个底分型才确认

					// 第一个底分型，且底分型比临时定分型低则确认临时顶分型为顶分型点
					if (tempPoint != null && records.get(i).getLow() < tempPoint.getPrice()) {

						// 顶分型确认
						newRecords.add(tempPoint);
						BeanUtils.copyProperties(tempPoint, currentPoint);
						tempPoint = new Point();
						tempPoint.setPrice(records.get(i).getLow());
						tempPoint.setType(0);
						tempPoint.setTime(records.get(i).getTime());

					}
				} else {
					if (tempPoint != null && records.get(i).getLow() < tempPoint.getPrice()) {
						tempPoint = new Point();
						tempPoint.setPrice(records.get(i).getLow());
						tempPoint.setTime(records.get(i).getTime());
						tempPoint.setType(0);

					}
				}
			}

			i = i + 2;

		}
		return newRecords;
	}
}
