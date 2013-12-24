/**
 * This function init the settings object with default values or with values that were saved in the DB
 */
function applySettings() {
    // RSS feed link
    rssModel.settings.feedInputUrl = rssModel.settings.feedInputUrl || "";

    // Number of entries in the RSS feed
    rssModel.settings.styling.numOfEntries = rssModel.settings.styling.numOfEntries || 4;
}






