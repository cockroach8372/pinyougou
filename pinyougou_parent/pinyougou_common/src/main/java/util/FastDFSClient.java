package util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

public class FastDFSClient {

	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private StorageClient1 storageClient = null;
	
	public FastDFSClient(String conf) throws Exception {
		if (conf.contains("classpath:")) {
			conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
		}
		ClientGlobal.init(conf);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileName 文件全路径
	 * @param extName 文件扩展名，不包含（.）
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result = storageClient.upload_file1(fileName, extName, metas);
		return result;
	}
	
	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}
	
	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileContent 文件的内容，字节数组
	 * @param extName 文件扩展名
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
		
		String result = storageClient.upload_file1(fileContent, extName, metas);
		return result;
	}
	
	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}
}
//动态获取ip地址
//	public String uploadFile(byte[] fileContent, String extName) throws Exception {
//		String[] strings = storageClient.upload_file(fileContent, extName, null);
//		if(strings != null && strings.length > 0){
//			//查询上传后图片保存到哪个Storage上。
//			FileInfo file_info = storageClient.get_file_info(strings[0], strings[1]);
//			String sourceIpAddr = file_info.getSourceIpAddr();
//
//			StringBuilder sb = new StringBuilder();
//			sb.append("http://");
//			sb.append(sourceIpAddr+"/");
//			sb.append(strings[0]+"/");
//			sb.append(strings[1]);
//			return sb.toString();
//			//  http://192.168.25.133/group1/M00/00/00/wKgZhV0dshGAQMmFAAEpSXLcp1E792.jpg
//		}
//
//		return null;
//	}