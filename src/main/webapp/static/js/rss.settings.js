var sp ={
    connectButton :  $('#connectBtn'),
    feedInputUrlElm : $('#rssFeedUrl'),
    numOfEntriesInput : $('#numOfEntries'),
    disconnectAccountElm : $('#disconnectAccount'),
    sliders: {widgetBcgCB: {}, feedBcgCB: {}},
    feedLink : $('#feedLink')
}

function initColorPickers() {
    $('#titleTextColor').ColorPicker({startWithColor : rssModel.settings.titleTextColor});
    $('#textColor').ColorPicker({startWithColor : rssModel.settings.textColor});
    $('#widgetBcgColor').ColorPicker({startWithColor : rssModel.settings.widgetBcgColor});
    $('#feedBcgColor').ColorPicker({startWithColor : rssModel.settings.feedBcgColor});
}

function initSliders() {
    sp.sliders['widgetBcgCB'] = $('#widgetBcgSlider').Slider({
                                    type: "Value",
                                    value: rssModel.settings.widgetBcgSlider
                                });
    sp.sliders['feedBcgCB'] = $('#feedBcgSlider').Slider({
                                    type: "Value",
                                    value: rssModel.settings.feedBcgSlider
                                });
}

function initCheckboxes() {
    $('#widgetBcgCB').Checkbox({ checked: rssModel.settings.widgetBcgCB });
    if (!rssModel.settings.widgetBcgCB) {
        sp.sliders['widgetBcgCB'].data('plugin_Slider').disable();
    }

    $('#feedBcgCB').Checkbox({ checked: rssModel.settings.feedBcgCB });
    if (!rssModel.settings.feedBcgCB){
        sp.sliders['feedBcgCB'].data('plugin_Slider').disable();
    }
}

function initInputElms() {
    sp.numOfEntriesInput.val(rssModel.settings.numOfEntries);
}

function bindEvents () {
    $(document).on('colorChanged', function(ev, data) {
        updateSettingsProperty(data.type, data.selected_color, true);
    });

    $(document).on('slider.change', function(ev, data) {
        updateSettingsProperty(data.type, data.value, true);
    });

    $(document).on('checkbox.change', function(ev, data) {
        if (data.checked) {
            sp.sliders[data.type].data('plugin_Slider').enable();
        }else {
            sp.sliders[data.type].data('plugin_Slider').disable();
        }

        updateSettingsProperty(data.type, data.checked, true);
    });

    sp.connectButton.click( function(){
        rssModel.settings.feedInputUrl = sp.feedInputUrlElm.val();
        var val = rssModel.settings.feedInputUrl;

        // hide guest description and show connected description
        displayHeader();

        updateSettingsProperty("feedInputUrl", rssModel.settings.feedInputUrl, true);
    });

    sp.disconnectAccountElm.click(function(){
        updateSettingsProperty("feedInputUrl", "", true);

        sp.feedInputUrlElm.val("");
        sp.feedInputUrlElm.focus();

        // hide guest description and show connected description
        $('.guest').toggleClass('hidden');
        $('.user').toggleClass('hidden');
    });

    sp.numOfEntriesInput.change( function(){
        updateSettingsProperty(sp.numOfEntriesInput.attr("id"), sp.numOfEntriesInput.val(), true);
    });
}

function displayHeader() {
    // If a user is connected to the widget (inserted a RSS feed link) the user section will be displayed,
    // otherwise the guest section will be displayed
    var guestSection = $('.guest');
    var userSection = $('.user');

    // If the feed url is initilized than the user already inserted a url, otherwise the guest section will
    // be displayed and the user will be able to insert a new feed url
    if (!rssModel.settings.feedInputUrl || rssModel.settings.feedInputUrl === "") {
        guestSection.removeClass('hidden');
        userSection.addClass('hidden');
    }
    else {
        sp.feedLink.html(rssModel.settings.feedInputUrl);
        sp.feedLink.attr('href', rssModel.settings.feedInputUrl);
//        loadFeedTitleAndDescription();
        guestSection.addClass('hidden');
        userSection.removeClass('hidden');
    }
}

function loadFeedTitleAndDescription(updateSettings) {
    google.load("feeds", "1");

    function initialize() {

        // Create a feed instance that will grab Digg's feed.
        var feed = new google.feeds.Feed(rssModel.settings.feedInputUrl);

        // Sets the result format
        feed.setResultFormat(google.feeds.Feed.JSON_FORMAT);

        feed.load(function(result) {
            if (!result.error) {
                $('#feedLink').attr('href', result.feed.link);
                $('#feedLink').html(result.feed.title);
                $('.feed-description').html(result.feed.description);
              }
        });
    };

    google.setOnLoadCallback(initialize);
}


function initPlugins () {

    // Init accordion
    $('.accordion').Accordion();

    // Init color pickers
    initColorPickers();

    initSliders();

    initCheckboxes();
}

function getQueryParameter(parameterName) {
    var url = window.document.URL.toString();

    var index = url.indexOf('?');

    var queryString = url.substring(index + 1, url.length-1);

    var queryArray = queryString.split('&');
    var queryMap = {};
    queryArray.forEach(function(element) {
        var parts = element.split('=');
        queryMap[parts[0]] = decodeURIComponent(parts[1]);
    });

    return queryMap[parameterName] || null;
}

function updateSettingsProperty(key, value, refresh) {
    var settings = rssModel.settings;
    settings[key] = value;
    updateSettings(settings, refresh);
}

function updateSettings(settingsJson, refresh) {
    var compId = Wix.Utils.getOrigCompId();

           $.ajax({
            'type': 'post',
            'url': "/app/settingsupdate",
            'dataType': "json",
            'contentType': 'application/json; chatset=UTF-8',
            'data': JSON.stringify({compId: Wix.Utils.getOrigCompId(), settings: settingsJson}),
            'cache': false,
            'success': function(res) {
                console.log("update setting completed");
                rssModel.settings = settingsJson;
                refresh ? Wix.Settings.refreshAppByCompIds(compId) : false;
            },
            'error': function(res) {
                console.log('error updating data with message ' + res.responseText);
            }
        });
}

function loadSettings() {
    window.rssModel = {};

    // Getting newSettings that was set as parameter in settings.vm
    // Check that newSettings is initialized with value
    rssModel.settings = !!newSettings ? newSettings : {};

    applySettings();

    $(document).ready(function() {
        displayHeader();

        bindEvents();

        // Init all plugins
        initPlugins();

        initInputElms();
    })
}

loadSettings();



