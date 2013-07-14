package me.yongbo.robot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.yongbo.bean.QiubaiObj;
import me.yongbo.robot.util.HttpUtil;


public class QiubaiRobot extends WebRobot {
	
	private final static String HOST = "www.qiushibaike.com";
	private final static String REFERER = "www.qiushibaike.com";
	private final static String POINT_URL = "http://www.qiushibaike.com/%1$s/page/%2$d?s=4579109";

	private int startPage;
	private int endPage;
	private String category;
	
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(int startPage,String category) {
		this(startPage, -1, category);
	}
	
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param endPage 结束页码
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(int startPage,int endPage,String category) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.startPage = startPage;
		this.endPage = endPage;
		this.category = category;
	}
	
	@Override
	public void run() {
		while (doAgain) {
			if(endPage != -1 && startPage > endPage){
				break;
			}
			doWork();
		}
	}

	public List<QiubaiObj> doWork() {
		System.out.println("开始抓取第 " + startPage + "页的数据");
		String rp;
		List<QiubaiObj> events = null;
		try {
			rp = getResponseString(String.format(POINT_URL, category, startPage));
			Document doc = Jsoup.parse(rp);
			Elements eles = doc.select(".block .content");
			if(eles.isEmpty()){
				doAgain = false;
				return null;
			}
			for(Element ele : eles){
				System.out.println(ele.text());
			}
			startPage++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			doAgain = false;
			e.printStackTrace();
		}
		return events;
	}
	
	private static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		return param;
	}
}
