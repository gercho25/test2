var exec = require('cordova/exec');

var serviceDiscovery = {
    getNetworkServices:function(service, addConfigXML, successCallback, errorCallback) {
			var processResponse = function(data) {
				successCallback(data);
			}
    	};
        exec(processResponse, errorCallback, "serviceDiscovery", "getNetworkServices", [service]);
    }
};

module.exports = serviceDiscovery;