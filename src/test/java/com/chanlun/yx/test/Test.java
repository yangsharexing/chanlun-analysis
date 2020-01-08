package com.chanlun.yx.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;
import com.chanlun.yx.data.util.BeiChiUtils;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.data.util.ZhongShuUtils;
import com.chanlun.yx.redis.RedisUtils;

public class Test {
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		for(int i = 1;i<20;i++){
			System.out.println("----------------------------------"+i);
			test(1000+i*50);
		}
	}
	
	public static void test(int end) throws IllegalAccessException, InvocationTargetException {
		
	
		
		List<String> codes = RedisUtils.getAllKeys();
		int i= 1;
		int k =0;
		for (String code : codes) {
			
			if(code.contains("day")){
				continue;
			}
			
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			
			if(end>list.size()){
				continue;
			}
			list = list.subList(0, end);

			List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
//			System.out.println("合并后k线" + list2.size());

			List<Point> point = BiLineUtils.contructBiLines(list2);
//			System.out.println("笔数点为" + point.size());

			List<Point> point2 = LineUtils.bi2Line(point);
//			System.out.println("线段点为" + point2.size());
			
			
//			List<Point> point2 = testData();
			
			List<TrendType> ttlist = ZhongShuUtils.findZhongShu(point2);
			
			if(ttlist!=null && ttlist.size()>4){
				
				TrendType last = ttlist.get(ttlist.size()-1);
				
				if(last instanceof Line){
					
					Line l = (Line)last;
					if(l.getDirect()==1){
						//向上的忽略
						continue;
					}
					
					TrendType selast = ttlist.get(ttlist.size()-3);
					Line sl = (Line)selast;
					if(sl.getDirect()==1){
						//向上
						continue;
						
					}
					//比较背驰
					//计算两者真实的价格区间
					ZhongShu sez= (ZhongShu)ttlist.get(ttlist.size()-4);
					ZhongShu z= (ZhongShu)ttlist.get(ttlist.size()-2);
					double preDiff = sl.getStartPoint().getPrice()-sl.getEndPoint().getPrice() - (sl.getStartPoint().getPrice() -sez.getDd());
					double afterDiff = l.getStartPoint().getPrice()-l.getEndPoint().getPrice() - (l.getStartPoint().getPrice() -z.getDd());
					
					//计算几个锚点
					double price  = l.getStartPoint().getPrice();
					
					if(preDiff/price>0.03 && afterDiff/price>0.03){
						
						System.out.println("1111  "+preDiff/price+"  :   "+afterDiff/price +"  "+code);
					}
					
					
					
				}else{
					ZhongShu zs = (ZhongShu)last;
					double currentPri = point2.get(point2.size()-1).getPrice();
					if(currentPri>=zs.getDd()){
						//中枢最后一笔比dd高
						continue;
					}
					
					TrendType selast = ttlist.get(ttlist.size()-2);
					Line sl = (Line)selast;
					if(sl.getDirect()==1){
						//向上
						continue;
					}
					ZhongShu sez= (ZhongShu)ttlist.get(ttlist.size()-3);
					//计算两者真实的价格区间
					double preDiff = sl.getStartPoint().getPrice()-sl.getEndPoint().getPrice() - (sl.getStartPoint().getPrice() -sez.getDd());
					double afterDiff = currentPri-zs.getDd();
					
					if(preDiff/currentPri>0.03 && afterDiff/currentPri>0.03){
						System.out.println("2222  "+preDiff/currentPri+"  :   "+afterDiff/currentPri+"  "+code);
//						if(BeiChiUtils.isBeichi(sl, point, list2)){
//							
//						
//						}
						
					}
					
					
					
					
				}
			}
			
		}
	}

	private static List<Point> testData() {
		Point p_1 = new Point("-1", 20, 1);
		Point p_2 = new Point("-2", 15, 0);
		Point p_3 = new Point("-3", 20, 1);
		Point p_4 = new Point("-4", 15, 0);
		Point p_5 = new Point("-5", 15, 1);
		Point p_6 = new Point("-6", 15, 0);
		
		
		Point p1 = new Point("1", 20, 1);
		Point p2 = new Point("2", 9, 0);
		Point p3 = new Point("3", 11, 1);
		Point p4 = new Point("4", 9, 0);
		Point p5 = new Point("5", 10, 1);
		Point p6 = new Point("6", 7, 0);
		Point p7 = new Point("7", 8, 1);
		Point p8 = new Point("8", 6, 0);
		Point p9 = new Point("9", 8, 1);
		Point p10 = new Point("10", 5, 0);
		Point p11 = new Point("11", 5.5, 1);
		
		List<Point> points = new ArrayList<Point>();
		points.add(p_1);
		points.add(p_2);
		points.add(p_3);
		points.add(p_4);
		points.add(p_5);
		points.add(p_6);
		
		
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		points.add(p9);
		points.add(p10);
		return points;
	}

//	public static void writeFile(String text) {
//		  try {
//	            File writeName = new File("C:\\output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
//	            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
//	            try (FileWriter writer = new FileWriter(writeName,true);
//	                 BufferedWriter out = new BufferedWriter(writer)
//	            ) {
//	                out.write(text); // \r\n即为换行
//	                out.write("\r\n");
//	                out.flush(); // 把缓存区内容压入文件
//	            }
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//		
//	}

}
