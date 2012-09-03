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

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping(SampleApp.EndpointControllerUrl)
public class SampleAppController
{
    @Resource
    private SampleApp sampleApp;

    protected AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());

    /**
     * VIEW - Widget Endpoint
     * @param model - model used by the view template widget.vm
     * @param instance - the signed instance parameter
     * @param target - the target parameter
     * @param width - the width of the widget IFrame
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(required = false) String target,
                         @RequestParam Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        return viewWidget(model, width, wixSignedInstance);

    }

    /**
     * VIEW - Setting Endpoint
     * @param model - model used by the view template setting.vm
     * @param instance - the signed instance parameter
     * @param width - the width of the setting IFrame
     * @return the template setting.vm name
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings(Model model,
                           @RequestParam String instance,
                           @RequestParam(required = false) Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        return viewSettings(model, width, wixSignedInstance);
    }

    private String viewWidget(Model model, Integer width, WixSignedInstance wixSignedInstance) {
        SampleAppInstance appInstance = loadOrCreateAppInstance(wixSignedInstance);

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";
    }

    private String viewSettings(Model model, Integer width, WixSignedInstance wixSignedInstance) {
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
     * @param instanceId - the instance id member of the signed instance
     * @param width - the width of the widget IFrame
     * @return the template widget.vm name
     */
    @RequestMapping(value = "/widgetstandalone", method = RequestMethod.GET)
    public String widgetStandAlone(Model model,
                                   @RequestParam String instanceId,
                                   @RequestParam Integer width)
    {
        WixSignedInstance wixSignedInstance = createTestSignedInstance(instanceId);
        return viewWidget(model, width, wixSignedInstance);
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
                                     @RequestParam Integer width)
    {
        WixSignedInstance wixSignedInstance = createTestSignedInstance(instanceId);
        return viewSettings(model, width, wixSignedInstance);
    }

    private WixSignedInstance createTestSignedInstance(String instanceId) {
        try {
            UUID instanceUuid = UUID.fromString(instanceId);
            return new WixSignedInstance(instanceUuid, new DateTime(), null, "");
        } catch (Exception ignored) {
            throw new RuntimeException(String.format("failed parsing instanceId [%s]. Valid values are GUIDs of the form aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", instanceId));
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
