Ext.tree.DWRTreeLoader = function(config) {
    Ext.tree.DWRTreeLoader.superclass.constructor.call(this, config);
};

Ext.extend(Ext.tree.DWRTreeLoader, Ext.tree.TreeLoader, {
    requestData : function(node, callback) {
        if (this.fireEvent("beforeload", this, node, callback) !== false) {

            //todo
            //var params = this.getParams(node);

            var callParams = new Array();
            var success = this.handleResponse.createDelegate(this, [node, callback], 1);
            var error = this.handleFailure.createDelegate(this, [node, callback], 1);
            callParams.push(node.id);
            callParams.push({callback:success, errorHandler:error});

            //todo: do we need to set this to something else?
            this.transId = true;
            this.dataUrl.apply(this, callParams);
        } else {
            // if the load is cancelled, make sure we notify
            // the node that we are done
            if (typeof callback == "function") {
                // alert(callback);
                callback();
            }
        }
    },

    processResponse : function(response, node, callback){
        try {
            for(var i = 0; i < response.length; i++){
                var n = this.createNode(response[i]);
                if(n){
                    node.appendChild(n);
                }
            }
            if(typeof callback == "function"){
                callback(this, node);
            }
        }catch(e){
            this.handleFailure(response);
        }
    },

    handleResponse : function(response, node, callback){
        this.transId = false;
        this.processResponse(response, node, callback);
        this.fireEvent("load", this, node, response);
    },

    handleFailure : function(response, node, callback){
        this.transId = false;
        this.fireEvent("loadexception", this, node, response);
        if(typeof callback == "function"){
            callback(this, node);
        }
    }

});

