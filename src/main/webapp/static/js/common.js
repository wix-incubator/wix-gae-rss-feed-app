/**
 * This function init the settings object with default values or with values that were saved in the DB
 */
function applySettings() {
    // Colors
    rssModel.settings.styling.textColor = rssModel.settings.styling.textColor ||  "#000000";
    rssModel.settings.styling.titleTextColor = rssModel.settings.styling.titleTextColor ||  "#000000";
    rssModel.settings.styling.widgetBcgColor = rssModel.settings.styling.widgetBcgColor || '#FFF';
    rssModel.settings.styling.widgetBcgCB = rssModel.settings.styling.widgetBcgCB || false;
    rssModel.settings.styling.widgetBcgSlider = rssModel.settings.styling.widgetBcgSlider || 0.5;
    rssModel.settings.styling.feedBcgColor = rssModel.settings.styling.feedBcgColor || '#F8F8F8';
    rssModel.settings.styling.feedBcgCB = rssModel.settings.styling.feedBcgCB || false;
    rssModel.settings.styling.feedBcgSlider = rssModel.settings.styling.feedBcgSlider || 0.5;

    // RSS feed link
    rssModel.settings.feedInputUrl = rssModel.settings.feedInputUrl || "";

    // Number of entries in the RSS feed
    rssModel.settings.styling.numOfEntries = rssModel.settings.styling.numOfEntries || 4;
}






