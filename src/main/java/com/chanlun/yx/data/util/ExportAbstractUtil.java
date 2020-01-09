package com.chanlun.yx.data.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.chanlun.yx.data.dto.HLDto;

/**
 * @author haozz
 * @date 2018/6/6 9:57
 * @description excel导出抽象工具类
 **/
public abstract class ExportAbstractUtil {

	public static void main(String[] args) {

		// 3.#.00表示保留后两位，它的处理方式是直接截掉不要的尾数，不四舍五入。
		DecimalFormat data = new DecimalFormat("#.0000");
		String str = data.format(30.666666);
		System.out.println(str);// 30.666666666666668

		// HLDto dto = new HLDto();
		// dto.setBuy(1);
		// dto.setCode("12");
		// dto.setDayNum(1);
		// dto.setEndTm("232");
		// dto.setStartTm("232");
		// dto.setYinliRate(2);
		// dto.setSale(1);
		//
		// List<HLDto> hls = new ArrayList<HLDto>();
		// hls.add(dto);
		// excelOutPut(hls,"60.xls");
	}
	
	public static String formatNum(double value){
		DecimalFormat data = new DecimalFormat("#.0000");
		String str = data.format(value);
		return str;
	}

	public static void excelOutPut(List<HLDto> hls, String fileName) {
		// 拼接数据start
		List<List<String>> lists = new ArrayList<List<String>>();
		String rows[] = { "code", "buy", "sale", "dayNum", "startTm", "endTm", "yinliRate", "preKLine", "preVolume",
				"prePriceStep", "afterKLine", "afterVolume", "afterPriceStep", "preDiff", "afterDiff" };
		List<String> rowsTitle = Arrays.asList(rows);
		lists.add(rowsTitle);
		for (int i = 0; i < hls.size(); i++) {
			HLDto dto = hls.get(i);
			String[] rowss = { dto.getCode(), formatNum(dto.getBuy()), formatNum(dto.getSale()), dto.getDayNum() + "",
					dto.getStartTm(), dto.getEndTm(), formatNum(dto.getYinliRate()), dto.getPreKLine()+"",
					formatNum(dto.getPreVolume()), formatNum(dto.getPrePriceStep()), dto.getAfterKLine()+"",
					formatNum(dto.getAfterVolume()), formatNum(dto.getAfterPriceStep()), formatNum(dto.getPreDiff()),
					formatNum(dto.getAfterDiff())};
			List<String> rowssList = Arrays.asList(rowss);
			lists.add(rowssList);
		}
		// 拼接数据end
		write(lists, fileName);
	}

	public static void write(Workbook workbook, String fileName) {
		pwrite(workbook, fileName);
	}

	public static void write(List<List<String>> lists, String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			fileName = UUID.randomUUID().toString() + ".xls";
		}
		SXSSFWorkbook workbook = new SXSSFWorkbook(lists.size());
		SXSSFSheet sheet = workbook.createSheet(fileName.substring(0, fileName.indexOf(".xls")));
		Integer rowIndex = 0;
		Row row = null;
		Cell cell = null;
		for (List<String> rowData : lists) {
			Integer columnIndex = 0;
			row = sheet.createRow(rowIndex++);
			for (String columnVal : rowData) {
				cell = row.createCell(columnIndex++);
				cell.setCellValue(columnVal);
			}
		}
		pwrite(workbook, fileName);
	}

	private static void pwrite(Workbook workbook, String fileName) {
		OutputStream out = null;
		try {
			out = new FileOutputStream("C://" + fileName + ".xls");
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}