/** 
 * 
 */
package ggxuite.controller;

import ggxuite.module.LoginUser;
import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;
import ggxuite.service.LoginUserService;
import ggxuite.service.XuiteFileService;
import ggxuite.service.XuiteUserService;
import ggxuite.util.XuiteUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import com.google.appengine.api.datastore.Key;
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

	@Autowired
	XuiteUserService userService;
	@Autowired
	XuiteFileService fileService;
	@Autowired
	LoginUserService loginUserService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public void process(@RequestParam("apiKey") String apiKey,
			@RequestParam("secretKey") String secretKey,
			@RequestParam("oAuth") String oAuth,
			@RequestParam("userId") String userId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (validateUserId(userId, response)) {
			XuiteUser xuite = null;
			List<XuiteFile> fileList = new ArrayList<XuiteFile>();
			XuiteUser oldXuiteUser = userService.findByApiKey(apiKey);
			Map<String, XuiteFile> oldFileMap = null;
			List<XuiteFile> oldFileList = null;
			if (oldXuiteUser != null) {
				oldFileMap = new LinkedHashMap<String, XuiteFile>();
				oldFileList = oldXuiteUser.getFiles();
				for (XuiteFile f : oldFileList) {
					oldFileMap.put(f.getXkey(), f);
				}
				oldXuiteUser.setoAuth(oAuth);
				oldXuiteUser.setSourceIP(request.getRemoteHost());
				oldXuiteUser
						.setLastUpdate(new Date(System.currentTimeMillis()));
				xuite = oldXuiteUser;
			} else {
				xuite = new XuiteUser(apiKey, secretKey, oAuth,
						request.getRemoteHost());
			}
			log.info("IP:" + xuite.getSourceIP());
			XuiteUtil xUtil = new XuiteUtil(xuite);

			JSONArray jsonArray = xUtil.getList();
			if (jsonArray.length() == 2
					&& "1001010003".equals(jsonArray.get(0))) {
				log.info("1001010003:" + jsonArray.getString(1));
				jsonArray = xUtil.getList();
			}
			if (jsonArray.length() == 2) {
				String msg = jsonArray.getString(1);
				if ("1001010004".equals(jsonArray.getString(0))) {
					msg += "Click \"Get oAuth\",please.";
				}
				log.info("ERROR MSG:" + msg);
				response.getWriter().print(msg);
				xUtil.close();
				return;
			}
			List<XuiteFile> newFileList = new ArrayList<XuiteFile>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				XuiteFile xFile = null;
				String xKey = j.getString("key");
				if (oldFileMap != null && oldFileMap.containsKey(xKey)) {
					// file exist
					xFile = oldFileMap.get(xKey);
					xFile.setParent(j.getString("parent"));
					xFile.setPath(j.getString("path"));
					xFile.setName(j.getString("name"));
					xFile.setSize(new BigInteger(j.getString("size")));
					xFile.setMtime(new Date(j.getLong("mtime")));
					// remove from old file list
					oldFileList.remove(xFile);
					log.info("file exist:" + xKey);
				} else {
					// new
					Key id = KeyFactory.createKey(
							XuiteFile.class.getSimpleName(), xKey);

					xFile = new XuiteFile(id, xKey, j.getString("parent"),
							j.getString("path"), j.getString("name"),
							new BigInteger(j.getString("size")), new Date(
									j.getLong("mtime")), xuite);
					newFileList.add(xFile);
				}
				log.info("file new:" + xKey);

				fileList.add(xFile);
			}
			xuite.setFiles(fileList);
			String url = request.getRequestURL().toString();
			String urls = processIdUrl(xUtil,
					url.substring(0, url.lastIndexOf("/")), fileList);
			log.info(urls);
			if (oldFileMap == null) {
				userService.save(xuite);
			} else {
				userService.saveUserAndNewFiles(xuite, newFileList);
				if (oldFileList != null && !oldFileList.isEmpty()) {
					// 移除不存在的舊檔
					fileService.delete(oldFileList);
				}
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");

			response.getWriter().print(urls);
			xUtil.close();
		}
	}

	@SuppressWarnings("static-access")
	private boolean validateUserId(String userId, HttpServletResponse response)
			throws IOException {
		LoginUser loginUser = loginUserService.findByUserId(userId);
		if (loginUser == null) {
			response.sendError(response.SC_EXPECTATION_FAILED,
					"Your User Id can't be found!");
			return false;
		}
		loginUser.setUpdateTime(new Date(System.currentTimeMillis()));
		loginUserService.save(loginUser);
		return true;
	}

	@RequestMapping
	public ModelAndView xuite(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("xuite");
	}

	@RequestMapping("/getoauth")
	public ModelAndView getOauth(HttpServletRequest request,
			HttpServletResponse response) {
		String error = request.getParameter("error");
		if (error != null) {
			return new ModelAndView("xuite", "msg", error);
		}
		Map<String, String> model = new HashMap<String, String>();
		String access_token = request.getParameter("access_token");
		model.put("oAuth", access_token);
		log.info("access_token:" + access_token);
		return new ModelAndView("xuite", model);
	}

	@RequestMapping(value = "/getoldfile", method = RequestMethod.GET)
	public void getOldFile(@RequestParam("apiKey") String apiKey,
			@RequestParam("userId") String userId, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JSONException {
		if (validateUserId(userId, response)) {
			XuiteUser oldUser = userService.findByApiKey(apiKey);
			JSONObject json = new JSONObject();
			if (oldUser != null) {
				json.put("secretKey", oldUser.getSecretKey());
				json.put("oAuth", oldUser.getoAuth());
				StringBuffer sb = new StringBuffer();
				for (XuiteFile file : oldUser.getFiles()) {
					sb.append(file.getShortLink()).append("|")
							.append(file.getName())
							.append(System.getProperty("line.separator"));
				}
				json.put("content", sb.toString());
			} else {
				json.put("msg", "This API-Key not found!");
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().print(json.toString());
		}
	}

	protected String processIdUrl(XuiteUtil xUtil, String url,
			List<XuiteFile> fileList) {
		StringBuffer sb = new StringBuffer();

		for (XuiteFile f : fileList) {
			log.info("Name:" + f.getName());
			String shortener = null;
			if (f.getShortLink() != null) {
				shortener = f.getShortLink();
			} else {
				// String u = new StringBuffer(url).append("/geturl/")
				// .append(KeyFactory.keyToString(f.getId())).toString();
				String u = new StringBuffer(url).append("/geturl/")
						.append(f.getXkey()).toString();
				shortener = u;// xUtil.getShortener(u);
				f.setShortLink(shortener);
			}
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
			List<XuiteFile> files = fileService.findByxKey(id);
			if (files == null || files.isEmpty()) {
				response.sendError(response.SC_EXPECTATION_FAILED,
						"id can't be found!");
				return;
			}
			XuiteFile file = files.get(0);
			XuiteUtil xUtil = new XuiteUtil(file.getUser());
			String redirectUrl = xUtil.getDirectURL(file.getXkey(),
					file.getParent());
			if (redirectUrl.startsWith("https://api.xuite.net")) {
				response.sendRedirect(redirectUrl);
			} else {
				response.sendError(response.SC_EXPECTATION_FAILED, redirectUrl);				
			}
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

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public void createUser(@PathVariable String userId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		LoginUser loginUser = loginUserService.findByUserId(userId);
		if (loginUser == null) {
			Date now = new Date(System.currentTimeMillis());
			loginUser = new LoginUser();
			loginUser.setUserId(userId);
			loginUser.setCreateTime(now);
			loginUser.setUpdateTime(now);
			loginUserService.save(loginUser);
			response.setStatus(response.SC_OK);
		} else {
			response.sendError(response.SC_EXPECTATION_FAILED, userId
					+ " has already existed!! Please choose anther one.");
		}
	}

}
