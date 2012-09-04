package com.wixpress.testapp.controller;

import com.wixpress.testapp.domain.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * The controller of the Wix API sample application.
 * The controller implements the widget and settings endpoints of a Wix application.
 * In addition, it implements two versions of the endpoints for stand-alone testing.
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping("/test-app")
public class SampleAppController
{
    @Resource
    private SampleApp sampleApp;

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
                         @RequestParam String viewMode)
    {
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
                           @RequestParam String instance,
                           @RequestParam(required = false) Integer width,
                           @RequestParam String locale,
                           @RequestParam String origCompId,
                           @RequestParam String compId)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        return viewSettings(model, width, wixSignedInstance, locale, origCompId, compId);
    }

    private String viewWidget(Model model, String sectionUrl, String target, Integer width, WixSignedInstance wixSignedInstance, String compId, String viewMode) {
        SampleAppInstance appInstance = loadOrCreateAppInstance(wixSignedInstance);

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";
    }

    private String viewSettings(Model model, Integer width, WixSignedInstance wixSignedInstance, String locale, String origCompId, String compId) {
        SampleAppInstance appInstance = loadOrCreateAppInstance(wixSignedInstance);

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "settings";
    }

    private SampleAppInstance loadOrCreateAppInstance(WixSignedInstance wixSignedInstance) {
        SampleAppInstance appInstance = sampleApp.getAppInstance(wixSignedInstance);

        if(appInstance == null)
        {
            // new Instance created
            appInstance = sampleApp.addAppInstance(wixSignedInstance);
        }
        return appInstance;
    }


    ///// Update Controller /////

    //TODO - add security verification
    @RequestMapping(value = "/settingsupdate", method = RequestMethod.GET)
    @ResponseBody
    public Result<Void> widgetUpdate(@RequestParam(required = false) String instanceId,
                                     @RequestParam(required = false) String color,
                                     @RequestParam(required = false) String title)
    {
        SampleAppInstance appInstance = sampleApp.getAppInstance(UUID.fromString(instanceId));

        appInstance.update(color, title);
        sampleApp.update(appInstance);

        return success();
    }

    ///// For Testing purposes only !! /////

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
                                   @RequestParam(required = false, defaultValue = "site") String viewMode)
    {
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
                                     @RequestParam String instanceId,
                                     @RequestParam(required = false) String userId,
                                     @RequestParam(required = false) String permissions,
                                     @RequestParam(required = false, defaultValue = "400") Integer width,
                                     @RequestParam(required = false, defaultValue = "en") String locale,
                                     @RequestParam(required = false, defaultValue = "widgetCompId") String origCompId,
                                     @RequestParam(required = false, defaultValue = "sectionCompId") String compId)
    {
        WixSignedInstance wixSignedInstance = createTestSignedInstance(instanceId, userId, permissions);
        return viewSettings(model, width, wixSignedInstance, locale, origCompId, compId);
    }

    private WixSignedInstance createTestSignedInstance(String instanceId, @Nullable String userId, @Nullable String permissions) {
        try {
            UUID instanceUuid = UUID.fromString(instanceId);
            UUID userUuid = null;
            if (userId != null)
                userUuid = UUID.fromString(userId);
            return new WixSignedInstance(instanceUuid, new DateTime(), userUuid, permissions);
        } catch (Exception ignored) {
            throw new RuntimeException(String.format("failed parsing instanceId [%s] or userId [%s]. Valid values are GUIDs of the form aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa or nulls (for userId)", instanceId, userId));
        }
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
     * @return void result indicating success
     */
    @RequestMapping(value = "/sampleappupdate", method = RequestMethod.GET)
    @ResponseBody
    public Result<Void> sampleAppUpdate(@RequestParam String applicationID,
                                        @RequestParam String applicationSecret)
    {
        sampleApp.setApplicationID(applicationID);
        sampleApp.setApplicationSecret(applicationSecret);

        return success();
    }


    ///// Utils /////

    protected Result<Void> success()
    {
        return new Result<Void>();
    }
}
