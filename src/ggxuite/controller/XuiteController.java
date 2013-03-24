/** 
 * 
 */
package ggxuite.controller;

import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;
import ggxuite.service.XuiteFileService;
import ggxuite.service.XuiteUserService;
import ggxuite.util.XuiteUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Rick
 * 
 */
@Controller
@RequestMapping("/xuite")
public class XuiteController extends GgXuiteAbstractController {
	private static final Logger log = Logger.getLogger(XuiteController.class
			.getName());
	protected final String DEFAULT_CODE = "UTF-8";
	// DI via Spring
	String message;

	@Autowired
	XuiteUserService userService;
	@Autowired
	XuiteFileService fileService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public void process(@RequestParam("apiKey") String apiKey,
			@RequestParam("secretKey") String secretKey,
			@RequestParam("oAuth") String oAuth, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		XuiteUser xuite = null;
		List<XuiteFile> fileList = null;
		List<XuiteFile> oldFileList = null;
		List<String> fileKeyList = new ArrayList<String>();
		XuiteUser oldXuiteUser = userService.findByApiKey(apiKey);
		xuite = new XuiteUser(apiKey, secretKey, oAuth,
				request.getRemoteHost());
		if (oldXuiteUser != null) {
			oldFileList = oldXuiteUser.getFiles();
			for (XuiteFile f : oldFileList) {
				fileKeyList.add(f.getXkey());
			}
		}			
		
		if (fileList == null) {
			fileList = new ArrayList<XuiteFile>();
		}
		log.info("IP:" + xuite.getSourceIP());
		XuiteUtil xUtil = new XuiteUtil(xuite);

		JSONArray jsonArray = xUtil.getList();
		if (jsonArray.length() == 2 && "1001010003".equals(jsonArray.get(0))) {
			log.info("1001010003:" + jsonArray.getString(1));
			jsonArray = xUtil.getList();
		}
		if (jsonArray.length() == 2) {
			String msg = jsonArray.getString(1);
			if("1001010004".equals(jsonArray.getString(0))){
				msg +="Click \"Get oAuth\",please.";
			}
			log.info("ERROR MSG:" + msg);
			response.getWriter().print(msg);
			xUtil.close();
			return;
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject j = jsonArray.getJSONObject(i);
			XuiteFile xFile = null;
			String xKey = j.getString("key");
			if (fileKeyList.contains(xKey)) {
				// file exist
				xFile = oldFileList.get(fileKeyList.indexOf(xKey));
				log.info("file exist:" + xKey);
			} else {
				// new
				xFile = new XuiteFile(xKey, j.getString("parent"),
						j.getString("path"), j.getString("name"),
						new BigInteger(j.getString("size")), new Timestamp(
								j.getLong("mtime")), xuite);
				log.info("file new:" + xKey);
			}
			fileList.add(xFile);
		}
		xuite.setFiles(fileList);
		userService.deleteAndInsert(oldXuiteUser,xuite);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String url = request.getRequestURL().toString();
		String urls = processIdUrl(xUtil,
				url.substring(0, url.lastIndexOf("/")), fileList);
		log.info(urls);
		response.getWriter().print(urls);
		xUtil.close();
	}

	@RequestMapping
	public ModelAndView xuite(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("xuite");
	}

	@RequestMapping("/getoauth")
	public ModelAndView getOauth(
			@RequestParam("access_token") String access_token,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("access_token:" + access_token);
		return new ModelAndView("xuite", "oAuth", access_token);
	}

	@RequestMapping(value = "/getoldfile", method = RequestMethod.GET)
	public void getOldFile(@RequestParam("apiKey") String apiKey,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		XuiteUser oldUser = userService.findByApiKey(apiKey);
		JSONObject json = new JSONObject();
		if (oldUser != null) {
			XuiteUtil xUtil = new XuiteUtil(oldUser);
			json.put("secretKey", oldUser.getSecretKey());
			json.put("oAuth", oldUser.getoAuth());
			json.put(
					"content",
					new StringBuffer(processIdUrl(xUtil, request
							.getRequestURL().toString(), oldUser.getFiles()))
							.toString());
			xUtil.close();
		} else {
			json.put("msg", "This API-Key not found!");
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().print(json.toString());

	}

	protected String processIdUrl(XuiteUtil xUtil, String url,
			List<XuiteFile> fileList) {
		StringBuffer sb = new StringBuffer();

		for (XuiteFile f : fileList) {
			String u = new StringBuffer(url).append("/geturl/")
					.append(KeyFactory.keyToString(f.getId())).toString();
			String shortener = xUtil.getShortener(u);
			log.info(u + "~" + shortener);
			sb.append(shortener).append("|").append(f.getName())
					.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/geturl/{id}", method = RequestMethod.GET)
	public void getUrl(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			XuiteFile file = fileService.findById(URLDecoder.decode(id,
					DEFAULT_CODE));
			if (file == null) {
				response.sendError(response.SC_EXPECTATION_FAILED,
						"id can't be found!");
			}
			XuiteUtil xUtil = new XuiteUtil(file.getUser());

			response.sendRedirect(xUtil.getDirectURL(file.getXkey(),
					file.getParent()));
			xUtil.close();
		} catch (ClientProtocolException e) {
			log.throwing(getClass().toString(), "getUrl", e.fillInStackTrace());
			e.printStackTrace();
		} catch (IOException e) {
			log.throwing(getClass().toString(), "getUrl", e.fillInStackTrace());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			log.throwing(getClass().toString(), "getUrl", e.fillInStackTrace());
			e.printStackTrace();
		} catch (JSONException e) {
			log.throwing(getClass().toString(), "getUrl", e.fillInStackTrace());
			e.printStackTrace();
		}
	}
}
