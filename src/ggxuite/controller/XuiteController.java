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
		XuiteUser xuite = new XuiteUser(apiKey, secretKey, oAuth);
		// xuiteService.save(xuite);
		log.info(oAuth);
		XuiteUtil xUtil = new XuiteUtil(xuite);
		// String oAuth = xUtil.getAuth();
		// xuite.setauth(oAuth);

		JSONArray jsonArray = xUtil.getList();
		if (jsonArray.length() == 2 && "1001010003".equals(jsonArray.get(0))) {
			jsonArray = xUtil.getList();
		}
		List<XuiteFile> fileList = new ArrayList<XuiteFile>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject j = jsonArray.getJSONObject(i);
			XuiteFile xFile = new XuiteFile(j.getString("key"),
					j.getString("parent"), j.getString("path"),
					j.getString("name"), new BigInteger(j.getString("size")),
					new Timestamp(j.getLong("mtime")), xuite);
			log.info(j.getString("key"));
			fileList.add(xFile);
		}
		xuite.setFiles(fileList);
		userService.save(xuite);

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

	protected String processIdUrl(XuiteUtil xUtil, String url,
			List<XuiteFile> fileList) {
		StringBuffer sb = new StringBuffer();

		for (XuiteFile f : fileList) {
			String u = new StringBuffer(url).append("/geturl/")
					.append(KeyFactory.keyToString(f.getId())).toString();
			String shortener = xUtil.getShortener(u);
			log.info(u + "~" + shortener);
			sb.append(shortener).append("|").append(f.getName())
					.append("<br/>");
		}
		return sb.toString();
	}

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
