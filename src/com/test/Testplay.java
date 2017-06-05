package com.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

public class Testplay {
	private static HttpClient httpClient = new HttpClient();
	// 设置代理服务器
	static {
		// 设置代理服务器的 IP 地址和端口
		httpClient.getHostConfiguration().setProxy("10.7.250.253", 8888);
	}

	public static boolean downloadPage(String path) throws HttpException, IOException {
		InputStream input = null;
		OutputStream output = null;
		// 得到 post 方法
		PostMethod postMethod = new PostMethod(path);
		// 设置 post 方法的参数
//		NameValuePair[] postData = new NameValuePair[2];
//		postData[0] = new NameValuePair("name", "lietu");
//		postData[1] = new NameValuePair("password", "*****");
//		postMethod.addParameters(postData);
		// 执行，返回状态码
		int statusCode = httpClient.executeMethod(postMethod);
		// 针对状态码进行处理 (简单起见，只处理返回值为 200 的状态码)
		if (statusCode == HttpStatus.SC_OK) {
			input = postMethod.getResponseBodyAsStream();
			// 得到文件名
			
			String filename = path.substring(path.lastIndexOf('/') + 1);
			System.err.println(filename);
			// 获得文件输出流
			output = new FileOutputStream(filename);
			// 输出到文件
			int tempByte = -1;
			while ((tempByte = input.read()) > 0) {
				output.write(tempByte);
			}
			// 关闭输入输出流
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
			return true;
		}

		//// 处理返回值为 300 的状态码
		if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 读取新的 URL 地址
			Header header = postMethod.getResponseHeader("location");
			Header[] headers = postMethod.getRequestHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.err.println(headers[i]);
			}
			System.err.println();
			if (header != null) {
				String newUrl = header.getValue();
				if (newUrl == null || newUrl.equals("")) {
					newUrl = "/";
					// 使用 post 转向
					PostMethod redirect = new PostMethod(newUrl);
					// 发送请求，做进一步处理……
				}
			}
		}

		return false;
	}

	/**
	 * 测试代码
	 */
	public static void main(String[] args) {
		
		// 抓取 iwjw 首页，输出
		try {
			System.err.println(Testplay.downloadPage("http://www.sina.com"));
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
