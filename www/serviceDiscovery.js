var exec = require('cordova/exec');

var serviceDiscovery = {
    getNetworkServices:function(service, addConfigXML, successCallback, errorCallback) {
		exec(successCallback, errorCallback, "serviceDiscovery", "getNetworkServices", [service]);
    }
};

module.exports = splashscreen;