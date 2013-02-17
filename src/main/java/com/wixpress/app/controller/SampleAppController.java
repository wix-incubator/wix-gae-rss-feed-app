package com.wixpress.app.controller;

import com.wixpress.app.dao.AppSettings;
import com.wixpress.app.dao.SampleAppDao;
import com.wixpress.app.domain.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * The controller of the Wix API sample application.
 * The controller implements the widget and settings endpoints of a Wix application.
 * In addition, it implements two versions of the endpoints for stand-alone testing.
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping("/app")
public class SampleAppController {
    @Resource
    private SampleApp sampleApp;

    @Resource
    private SampleAppDao sampleAppDao;

    protected AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());

    /**
     * VIEW - Widget Endpoint
     * @link http://dev.wix.com/display/wixdevelopersapi/Widget+Endpoint
     * @param model - Spring MVC model used by the view template widget.vm
     * @param instance - The signed instance {@see http://dev.wix.com/display/wixdevelopersapi/The+Signed+Instance}
     * @param sectionUrl - The base URL of the application section, if present
     * @param target - The target attribute that must be added to all href anchors within the application frame
     * @param width - The width of the frame to render in pixels
     * @param compId - The id of the Wix component which is the host of the IFrame
     * @param viewMode - An indication whether the user is currently in editor / site
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(value = "section-url") String sectionUrl,
                         @RequestParam(required = false) String target,
                         @RequestParam Integer width,
                         @RequestParam String compId,
                         @RequestParam String viewMode) {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        return viewWidget(model, sectionUrl, target, width, wixSignedInstance, compId, viewMode);

    }

    /**
     * VIEW - Setting Endpoint
     * @param model - Spring MVC model used by the view template setting.vm
     * @param instance - The signed instance {@see http://dev.wix.com/display/wixdevelopersapi/The+Signed+Instance}
     * @param width - The width of the frame to render in pixels
     * @param locale - The language of the Wix editor
     * @param origCompId - The Wix component id of the caller widget/section
     * @param compId - The id of the Wix component which is the host of the IFrame
     * @return the template setting.vm name
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings(Model model,
                           HttpServletResponse response,
                           @RequestParam String instance,
                           @RequestParam(required = false) Integer width,
                           @RequestParam String locale,
                           @RequestParam String origCompId,
                           @RequestParam String compId) {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        response.addCookie(new Cookie("instanceId", wixSignedInstance.getInstanceId().toString()));
        return viewSettings(model, width, wixSignedInstance, locale, origCompId, compId);
    }

    /**
     * Saves changes from the settings dialog
     * @param instanceId - the app instanceId, read from a cookie placed by the settings controller view operations
     * @param color - the new entered color
     * @param title - the new entered title
     * @return AjaxResult written directly to the response stream
     */
    @RequestMapping(value = "/settingsupdate", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult widgetUpdate(@CookieValue() String instanceId,
                                   @RequestParam(required = false) String color,
                                   @RequestParam(required = false) String title) {
        try {
            UUID instanceIduuid = UUID.fromString(instanceId);
            AppSettings appSettings = sampleAppDao.getAppInstance(instanceIduuid);

            AppSettings newAppSettings = new AppSettings(
                    (title == null)?appSettings.getTitle():title,
                    (color == null)?appSettings.getColor():color);

            sampleAppDao.update(newAppSettings, instanceIduuid);
            return AjaxResult.ok();
        }
        catch (Exception e) {
            return AjaxResult.fail(e);
        }
    }

    /**
     * VIEW - Widget Endpoint for testing
     * This endpoint does not implement the Wix API. It can be used directly to test the application from any browser,
     * substituting the signed instance parameter with explicit values given as URL parameters
     *
     * @param model - model used by the view template widget.vm
     * @param instanceId - the instanceId member of the signed instance
     * @param userId - the uid member of the signed instance
     * @param permissions - the permissions member of the signed instance
     * @param sectionUrl - The base URL of the application section, if present
     * @param target - The target attribute that must be added to all href anchors within the application frame
     * @param width - The width of the frame to render in pixels
     * @param compId - The id of the Wix component which is the host of the IFrame
     * @param viewMode - An indication whether the user is currently in editor / site
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widgetstandalone", method = RequestMethod.GET)
    public String widgetStandAlone(Model model,
                                   @RequestParam String instanceId,
                                   @RequestParam(required = false) String userId,
                                   @RequestParam(required = false) String permissions,
                                   @RequestParam(value = "section-url", required = false, defaultValue = "/") String sectionUrl,
                                   @RequestParam(required = false, defaultValue = "_self") String target,
                                   @RequestParam(required = false, defaultValue = "200") Integer width,
                                   @RequestParam(required = false, defaultValue = "widgetCompId") String compId,
                                   @RequestParam(required = false, defaultValue = "site") String viewMode) {
        WixSignedInstance wixSignedInstance = createTestSignedInstance(instanceId, userId, permissions);
        return viewWidget(model, sectionUrl, target, width, wixSignedInstance, compId, viewMode);
    }

    /**
     * VIEW - Setting Endpoint for testing
     * This endpoint does not implement the Wix API. It can be used directly to test the application from any browser,
     * substituting the signed instance parameter with explicit values given as URL parameters
     *
     * @param model - model used by the view template setting.vm
     * @param instanceId - the instance id member of the signed instance
     * @param width - the width of the setting IFrame
     * @return the template setting.vm name
     */
    @RequestMapping(value = "/settingsstandalone", method = RequestMethod.GET)
    public String settingsStandAlone(Model model,
                                     HttpServletResponse response,
                                     @RequestParam String instanceId,
                                     @RequestParam(required = false) String userId,
                                     @RequestParam(required = false) String permissions,
                                     @RequestParam(required = false, defaultValue = "400") Integer width,
                                     @RequestParam(required = false, defaultValue = "en") String locale,
                                     @RequestParam(required = false, defaultValue = "widgetCompId") String origCompId,
                                     @RequestParam(required = false, defaultValue = "sectionCompId") String compId) {
        WixSignedInstance wixSignedInstance = createTestSignedInstance(instanceId, userId, permissions);
        response.addCookie(new Cookie("instanceId", instanceId));
        return viewSettings(model, width, wixSignedInstance, locale, origCompId, compId);
    }


    /**
     * AJAX - operation which allows to change the applicationId and applicationSecret of this app during runtime.
     * It can be used during app development to update the running app applicationId and applicationSecret after
     * you register your application with Wix.
     *
     * DELETE THIS OPERATION BEFORE SUBMITTING YOUR APPLICATION WITH WIX
     *
     * @param applicationID - the application id
     * @param applicationSecret - the application secret
     * @return AjaxResult written directly to the response stream
     */
    @RequestMapping(value = "/sampleappupdate", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult sampleAppUpdate(@RequestParam String applicationID,
                                      @RequestParam String applicationSecret)
    {
        sampleApp.setApplicationID(applicationID);
        sampleApp.setApplicationSecret(applicationSecret);

        return AjaxResult.ok();
    }

    /**
     * generic Spring MVC error handler
     * @link http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/mvc.html#mvc-ann-exceptionhandler
     * @param e - the exception
     * @return a view name
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e) {
        if (e instanceof InvalidSignatureException) {
            return new ModelAndView("invalid-secret");
        }
        else {
            ModelAndView mv = new ModelAndView("error-view");
            StringBuilder stackTrace = new StringBuilder();
            renderStackTrace(e, stackTrace);
            mv.addObject("exceptionMessage", e.getMessage());
            mv.addObject("exceptionStackTrace", stackTrace.toString());
            return mv;
        }
    }

    public void renderStackTrace(Throwable e, StringBuilder stackTrace) {
        for (StackTraceElement stackTraceElement: e.getStackTrace()) {
            stackTrace.append("<div class=\"stack-trace\">").append(stackTraceElement.toString()).append("</div>");
        }
        if (e.getCause() != null && e.getCause() != e) {
            stackTrace.append("<div class=\"caused-by\">").append("caused by ").append(e.getCause().getClass()).append(" - ").append(e.getCause().getMessage()).append("</div>");
            renderStackTrace(e.getCause(), stackTrace);
        }
    }

    private String viewWidget(Model model, String sectionUrl, String target, Integer width, WixSignedInstance wixSignedInstance, String compId, String viewMode) {
        AppSettings appSettings = loadOrCreateAppInstance(wixSignedInstance);

        model.addAttribute("wixSignedInstance", wixSignedInstance);
        model.addAttribute("appSettings", appSettings);
        model.addAttribute("width", width);

        return "widget";
    }

    private String viewSettings(Model model, Integer width, WixSignedInstance wixSignedInstance, String locale, String origCompId, String compId) {
        AppSettings appSettings = loadOrCreateAppInstance(wixSignedInstance);

        model.addAttribute("wixSignedInstance", wixSignedInstance);
        model.addAttribute("appSettings", appSettings);
        model.addAttribute("width", width);

        return "settings";
    }

    /**
     * the method loads the app settings, or creates a new app settings (new app instance) if the datastore does not
     * include this instanceId
     * @param wixSignedInstance - the unmarshaled signed instance
     * @return loaded or new app settings
     */
    private AppSettings loadOrCreateAppInstance(WixSignedInstance wixSignedInstance) {
        AppSettings appSettings = sampleAppDao.getAppInstance(wixSignedInstance.getInstanceId());

        if(appSettings == null) {
            appSettings = new AppSettings();
            sampleAppDao.addAppInstance(appSettings, wixSignedInstance.getInstanceId());
        }
        return appSettings;
    }

    private WixSignedInstance createTestSignedInstance(String instanceId, @Nullable String userId, @Nullable String permissions) {
        try {
            UUID instanceUuid = UUID.fromString(instanceId);
            UUID userUuid = null;
            if (userId != null)
                userUuid = UUID.fromString(userId);
            return new WixSignedInstance(instanceUuid, new DateTime(), userUuid, permissions);
        } catch (Exception original) {
            throw new ContollerInputException("Failed parsing instanceId [%s] or userId [%s].\nValid values are GUIDs of the form [aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa] or nulls (for userId)",
                    original, instanceId, userId);
        }
    }


}
