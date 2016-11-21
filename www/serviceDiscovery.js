var serviceDiscovery = {
    getNetworkServices:function(service, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "serviceDiscovery", "getNetworkServices", [service]);
    }
};

module.exports = serviceDiscovery;