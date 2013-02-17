var _rssWidget = (function() {

    var sp = {
        widgetBody: $('.widget-body'),
        feedElement: $('#feedEntries'),
        title : $('#feedTitle'),
        scroll : $('#scrollbar1'),
        defaultURL : "http://rss.cnn.com/rss/edition.rss"
    }

    function loadFeed() {

        google.load("feeds", "1");

        function initialize() {

            initFeedUrl();

            // Create a feed instance that will grab Digg's feed.
            var feed = new google.feeds.Feed(rssModel.settings.feedInputUrl);

            // Set the number of feed entries that will be loaded by this feed
            feed.setNumEntries(rssModel.settings.numOfEntries);

            // Sets the result format
            feed.setResultFormat(google.feeds.Feed.JSON_FORMAT);

            feed.load(function(result) {
                if (!result.error) {
                    setFeedTitle(result.feed.title);
                    setFeed(result.feed.entries);
                }
            });
        }

        google.setOnLoadCallback(initialize);
    }

    function initFeedUrl(){
        // If the feed url is initilized than the user already inserted a url, otherwise a default value will be initilize so feed will
        // be displayed to website users
        if (!rssModel.settings.feedInputUrl || rssModel.settings.feedInputUrl == "") {
            rssModel.settings.feedInputUrl = sp.defaultURL
        }
    }

    function setFeedTitle(title){
        sp.title.html(title);
    }

    function setFeed(entries) {
        for (var i=0; i<entries.length; i++) {
            var feedElm = $("#feed");

            var data = entries[i];

            // get tempalte
            var feedEntry = feedElm.html();

            // compile template
            var newFeed = Handlebars.compile(feedEntry)({
                title: data.title,
                content: data.content,
                link: data.link

            });

            // append compiled template
            $('#feedEntries').append(newFeed);
        }

        $(".feed").css('color', rssModel.settings.textColor);

        // Remove the border from the last feed class element
        $(".feed:last").css('border-bottom', 'none');
    }

    function convertHexToRgba(hex, opacity) {
        hex = hex.replace('#','');
        r = parseInt(hex.substring(0,2), 16);
        g = parseInt(hex.substring(2,4), 16);
        b = parseInt(hex.substring(4,6), 16);

        result = 'rgba('+r+','+g+','+b+','+opacity+')';
        return result;
    }

    function applyStyle () {
        calcBodyHeight();
        applySettings();
    }

    function applySettings() {
        sp.title.css('color', rssModel.settings.titleTextColor);

        // Set widget background color - check if transparent is checked
        var widgetBackgroundColor = rssModel.settings.widgetBcgColor;
        if ( rssModel.settings.widgetBcgCB){
             var opacityVal = rssModel.settings.widgetBcgSlider;
             widgetBackgroundColor = convertHexToRgba(widgetBackgroundColor, opacityVal);
        }
        sp.widgetBody.css('background-color', widgetBackgroundColor);

        // Set feed background color - check if transparent is checked
        var feedBackgroundColor = rssModel.settings.feedBcgColor;
        if ( rssModel.settings.feedBcgCB){
            var opacityVal = rssModel.settings.feedBcgSlider;
            feedBackgroundColor = convertHexToRgba(feedBackgroundColor, opacityVal);
        }
        sp.feedElement.css('background-color', feedBackgroundColor);
    }

    function handleWindowResize() {
        $(window).resize(function (event) {
            calcBodyHeight();
        });
    }

    function calcBodyHeight() {
       $('.overview').height(($('.widget-body').height() - 60)+'px');
    }

    return {
        init: loadFeed,
        applyStyle : applyStyle,
        sp: sp,
        bindEvents: handleWindowResize
    }

}());


function loadWidget() {
    window.rssModel = {};

    // Getting newSettings that was set as parameter in settings.vm
    // Check that newSettings is initialized with value
    rssModel.settings = !!newSettings ? newSettings : {};

    applySettings();

    _rssWidget.init();

    $(document).ready(function() {
        _rssWidget.bindEvents();

//        // add scrollbar to the feed container
//        $('.viewport').height($('.widget-body').height() - 50);
//        _rssWidget.sp.scroll.tinyscrollbar({
//            axis: 'y'
//        });

        _rssWidget.applyStyle();
    });
};

loadWidget();


