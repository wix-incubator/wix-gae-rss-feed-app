package com.wixpress.app.controller;

import com.wixpress.app.dao.AppSettings;
import com.wixpress.app.dao.SampleAppDao;
import com.wixpress.app.domain.AppInstance;
import com.wixpress.app.domain.AuthenticationResolver;
import com.wixpress.app.domain.InvalidSignatureException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * The controller of the Rss Feed application.
 * The controller implements the widget and settings endpoints of a Wix application.
 * In addition, it implements two versions of the endpoints for stand-alone testing.
 */

@Controller
@RequestMapping("/app")
public class SampleAppController {

    @Resource
    private SampleAppDao sampleAppDao;

    @Resource
    private ObjectMapper objectMapper;

    protected AuthenticationResolver authenticationResolver = new AuthenticationResolver();

    /**
     * VIEW - Widget Endpoint
     * @link http://dev.wix.com/docs/display/DRAF/App+Endpoints#AppEndpoints-WidgetEndpoint
     * @param model - Spring MVC model used by the view template widget.vm
     * @param instance - The signed instance {@see http://dev.wix.com/display/wixdevelopersapi/The+Signed+Instance}
     * @param compId - The id of the Wix component which is the host of the IFrame
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam String compId) throws IOException
    {
        AppInstance appInstance = authenticationResolver.unsignInstance(objectMapper, instance);
        return viewWidget(model, appInstance.getInstanceId().toString(), compId);
    }

    /**
     * VIEW - Settings Endpoint
     * @link http://dev.wix.com/docs/display/DRAF/App+Endpoints#AppEndpoints-SettingsEndpoint
     * @param model - Spring MVC model used by the view template setting.vm
     * @param instance - The signed instance {@see http://dev.wix.com/display/wixdevelopersapi/The+Signed+Instance}
     * @param origCompId - The Wix component id of the caller widget/section
     * @return the template setting.vm name
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings(Model model,
                           HttpServletResponse response,
                           @RequestParam String instance,
                           @RequestParam String origCompId) throws IOException {
        AppInstance appInstance = authenticationResolver.unsignInstance(objectMapper, instance);
        response.addCookie(new Cookie("instance", instance));
        return viewSettings(model, appInstance.getInstanceId().toString(), origCompId);
    }

    /**
     * Saves changes from the settings dialog
     * @param instance - the app instance, read from a cookie placed by the settings controller view operations
     * @param settingsUpdate - the new settings selected by the user and the widgetId
     * @return AjaxResult written directly to the response stream
     */
    @RequestMapping(value = "/settingsupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AjaxResult> settingsUpdate(@CookieValue() String instance,
                                   @RequestBody SettingsUpdate settingsUpdate) {
        try {
            AppInstance appInstance = authenticationResolver.unsignInstance(objectMapper, instance);
            sampleAppDao.updateAppSettings(appInstance.getInstanceId().toString(), settingsUpdate.getCompId(), settingsUpdate.getSettings());
            return AjaxResult.ok();
        }
        catch (Exception e) {
            return AjaxResult.internalServerError(e);
        }
    }

    /**
     * VIEW - Widget Endpoint for testing
     * This endpoint does not implement the Wix API. It can be used directly to test the application from any browser,
     * substituting the signed instance parameter with explicit values given as URL parameters
     *
     * @param model - model used by the view template widget.vm
     * @param instanceId - the instanceId member of the signed instance
     * @param compId - The id of the Wix component which is the host of the IFrame
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widgetstandalone", method = RequestMethod.GET)
    public String widgetStandAlone(Model model,
                                   @RequestParam String instanceId,
                                   @RequestParam(required = false, defaultValue = "widgetCompId") String compId) throws IOException
    {
        AppInstance appInstance = createTestAppInstance(instanceId);
        return viewWidget(model, appInstance.getInstanceId().toString(), compId);
    }

    /**
     * VIEW - Setting Endpoint for testing
     * This endpoint does not implement the Wix API. It can be used directly to test the application from any browser,
     * substituting the signed instance parameter with explicit values given as URL parameters
     *
     * @param model - model used by the view template setting.vm
     * @param instanceId - the instance id member of the signed instance
     * @return the template setting.vm name
     */
    @RequestMapping(value = "/settingsstandalone", method = RequestMethod.GET)
    public String settingsStandAlone(Model model,
                                     HttpServletResponse response,
                                     @RequestParam String instanceId,
                                     @RequestParam(required = false, defaultValue = "widgetCompId") String origCompId) throws IOException {
        AppInstance appInstance = createTestAppInstance(instanceId);
        response.addCookie(new Cookie("instanceId", instanceId));
        return viewSettings(model, appInstance.getInstanceId().toString(), origCompId);
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

    // Set widget.vm
    private String viewWidget(Model model, String instanceId, String compId) throws IOException {
        AppSettings appSettings = getSettings(instanceId, compId);

        model.addAttribute("settings", objectMapper.writeValueAsString(appSettings));

        return "widget";
    }

    // Set setting.vm
    private String viewSettings(Model model, String instanceId, String origCompId) throws IOException {
        AppSettings appSettings = getSettings(instanceId, origCompId);

        model.addAttribute("settings", objectMapper.writeValueAsString(appSettings));

        return "settings";
    }

    /**
     * Get settings from the DB if exists, otherwise return empty settings
     * @param instanceId - the instance id
     * @param compId - the app comp Id
     * @return app settings
     */
    private AppSettings getSettings(String instanceId, String compId) {
        AppSettings appSettings = sampleAppDao.getAppSettings(instanceId, compId);

        if(appSettings == null) {
            appSettings = new AppSettings(objectMapper);
        }
        return appSettings;
    }

    private AppInstance createTestAppInstance(String instanceId) throws RuntimeException {
        try {
            UUID instanceUuid = UUID.fromString(instanceId);

            return new AppInstance(instanceUuid);
        } catch (Exception original) {
            throw new ContollerInputException("Failed parsing instanceId [%s].\nValid values are GUIDs of the form [aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa] or nulls (for userId)",
                    original, instanceId);
        }
    }


}
