
if (document.location.protocol == 'file:') {

/*!
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
 /*
 * Portions of this file are based on pieces of Yahoo User Interface Library
 * Copyright (c) 2007, Yahoo! Inc. All rights reserved.
 * YUI licensed under the BSD License:
 * http://developer.yahoo.net/yui/license.txt
 */
    Ext.lib.Ajax = function() {
        var activeX = ['MSXML2.XMLHTTP.3.0',
                       'MSXML2.XMLHTTP',
                       'Microsoft.XMLHTTP'],
            CONTENTTYPE = 'Content-Type';

        // private
        function setHeader(o) {
            var conn = o.conn,
                prop;

            function setTheHeaders(conn, headers){
                for (prop in headers) {
                    if (headers.hasOwnProperty(prop)) {
                        conn.setRequestHeader(prop, headers[prop]);
                    }
                }
            }

            if (pub.defaultHeaders) {
                setTheHeaders(conn, pub.defaultHeaders);
            }

            if (pub.headers) {
                setTheHeaders(conn, pub.headers);
                pub.headers = null;
            }
        }

        // private
        function createExceptionObject(tId, callbackArg, isAbort, isTimeout) {
            return {
                tId : tId,
                status : isAbort ? -1 : 0,
                statusText : isAbort ? 'transaction aborted' : 'communication failure',
                    isAbort: true,
                    isTimeout: true,
                argument : callbackArg
            };
        }

        // private
        function initHeader(label, value) {
            (pub.headers = pub.headers || {})[label] = value;
        }

/*
        // private
        function createResponseObject(o, callbackArg) {
            var headerObj = {},
                headerStr,
                conn = o.conn,
                t,
                s;

            try {
                headerStr = o.conn.getAllResponseHeaders();
                Ext.each(headerStr.replace(/\r\n/g, '\n').split('\n'), function(v){
                    t = v.indexOf(':');
                    if(t >= 0){
                        s = v.substr(0, t).toLowerCase();
                        if(v.charAt(t + 1) == ' '){
                            ++t;
                        }
                        headerObj[s] = v.substr(t + 1);
                    }
                });
            } catch(e) {}

            return {
                tId : o.tId,
                status : conn.status,
                statusText : conn.statusText,
                getResponseHeader : function(header){return headerObj[header.toLowerCase()];},
                getAllResponseHeaders : function(){return headerStr},
                responseText : conn.responseText,
                responseXML : conn.responseXML,
                argument : callbackArg
            };
        }
*/
        function createResponseObject(o, callbackArg) {
            var obj = {};
            var headerObj = {};

            try {
                var headerStr = o.conn.getAllResponseHeaders();
                var header = headerStr.split('\n');
                for (var i = 0; i < header.length; i++) {
                    var delimitPos = header[i].indexOf(':');
                    if (delimitPos != -1) {
                        headerObj[header[i].substring(0, delimitPos)] = header[i].substring(delimitPos + 2);
                    }
                }
            } catch(e) {}

            obj.tId = o.tId;
            obj.status = o.status.status;
            obj.statusText = o.status.statusText;
            obj.getResponseHeader = headerObj;
            obj.getAllResponseHeaders = headerStr;
            obj.responseText = o.conn.responseText;
            obj.responseXML = o.conn.responseXML;

            if (o.status.isLocal) {

                o.status.isOK = ((obj.status = o.status.status = ( !! obj.responseText.length) ? 200 : 404) == 200);

                if (o.status.isOK && (!obj.responseXML || obj.responseXML.childNodes.length == 0)) {

                    var xdoc = null;
                    try { //ActiveX may be disabled
                        if (typeof(DOMParser) == 'undefined') {
                            xdoc = new ActiveXObject("Microsoft.XMLDOM");
                            xdoc.async = "false";
                            xdoc.loadXML(obj.responseText);
                        } else {
                            var domParser = new DOMParser();
                            xdoc = domParser.parseFromString(obj.responseText, 'application/xml');
                            domParser = null;
                        }
                    } catch(ex) {
                        o.status.isError = true;

                    }

                    obj.responseXML = xdoc;

                    if (xdoc && typeof(obj.getResponseHeader['Content-Type']) == 'undefined' && !!xdoc.childNodes.length)
                    // Got valid nodes? then set the response header
                    {
                        obj.getResponseHeader['Content-Type'] == 'text/xml';

                    }

                }

            }

            if (typeof callbackArg !== undefined) {
                obj.argument = callbackArg;
            }

            return obj;
        }

        // private
        function releaseObject(o) {
            o.conn = null;
            o = null;
        }
/*
        // private
        function handleTransactionResponse(o, callback, isAbort, isTimeout) {
            if (!callback) {
                releaseObject(o);
                return;
            }

            var httpStatus, responseObject;

            try {
                if (o.conn.status !== undefined && o.conn.status != 0) {
                    httpStatus = o.conn.status;
                }
                else {
                    httpStatus = 13030;
                }
            }
            catch(e) {
                httpStatus = 13030;
            }

            if ((httpStatus >= 200 && httpStatus < 300) || (Ext.isIE && httpStatus == 1223)) {
                responseObject = createResponseObject(o, callback.argument);
                if (callback.success) {
                    if (!callback.scope) {
                        callback.success(responseObject);
                    }
                    else {
                        callback.success.apply(callback.scope, [responseObject]);
                    }
                }
            }
            else {
                switch (httpStatus) {
                    case 12002:
                    case 12029:
                    case 12030:
                    case 12031:
                    case 12152:
                    case 13030:
                        responseObject = createExceptionObject(o.tId, callback.argument, (isAbort ? isAbort : false), isTimeout);
                        if (callback.failure) {
                            if (!callback.scope) {
                                callback.failure(responseObject);
                            }
                            else {
                                callback.failure.apply(callback.scope, [responseObject]);
                            }
                        }
                        break;
                    default:
                        responseObject = createResponseObject(o, callback.argument);
                        if (callback.failure) {
                            if (!callback.scope) {
                                callback.failure(responseObject);
                            }
                            else {
                                callback.failure.apply(callback.scope, [responseObject]);
                            }
                        }
                }
            }

            releaseObject(o);
            responseObject = null;
        }
*/
        function handleTransactionResponse(o, callback, isAbort) {

            var responseObject;

            callback = callback || {};

            o.status = getHttpStatus(o.conn);

            if (!o.status.isError) {
                // create and enhance the response with proper status and XMLDOM if necessary
                responseObject = createResponseObject(o, callback.argument);
            }

            if (o.status.isError) {
                // checked again in case exception was raised - ActiveX was disabled during XML-DOM creation?
                responseObject = createExceptionObject(o.tId, callback.argument, (isAbort ? isAbort: false));
            }

            if (o.status.isOK && !o.status.isError) {
                if (callback.success) {
                    if (!callback.scope) {
                        callback.success(responseObject);
                    } else {
                        callback.success.apply(callback.scope, [responseObject]);
                    }
                }
            } else {

                if (callback.failure) {
                    if (!callback.scope) {
                        callback.failure(responseObject);
                    } else {
                        callback.failure.apply(callback.scope, [responseObject]);
                    }
                }

            }

            releaseObject(o);
            responseObject = null;
        }
        function getHttpStatus(reqObj) {

            var statObj = {
                status: 0,
                statusText: '',
                isError: false,
                isLocal: false,
                isOK: false
            };
            try {
                if (!reqObj) throw ('noobj');
                statObj.status = reqObj.status || 0;

                statObj.isLocal = !reqObj.status && location.protocol == "file:" || Ext.isSafari && reqObj.status == undefined;

                statObj.statusText = reqObj.statusText || '';

                statObj.isOK = (statObj.isLocal || (statObj.status > 199 && statObj.status < 300) || statObj.status == 304);

            } catch(e) {
                statObj.isError = true;
            } //status may not avail/valid yet.
            return statObj;

        }

        // private
        function handleReadyState(o, callback){
        callback = callback || {};
            var conn = o.conn,
                tId = o.tId,
                poll = pub.poll,
        cbTimeout = callback.timeout || null;

            if (cbTimeout) {
                pub.timeout[tId] = setTimeout(function() {
                    pub.abort(o, callback, true);
                }, cbTimeout);
            }

            poll[tId] = setInterval(
                function() {
                    if (conn && conn.readyState == 4) {
                        clearInterval(poll[tId]);
                        poll[tId] = null;

                        if (cbTimeout) {
                            clearTimeout(pub.timeout[tId]);
                            pub.timeout[tId] = null;
                        }

                        handleTransactionResponse(o, callback);
                    }
                },
                pub.pollInterval);
        }
/*
        // private
        function asyncRequest(method, uri, callback, postData) {
            var o = getConnectionObject() || null;

            if (o) {
                o.conn.open(method, uri, true);

                if (pub.useDefaultXhrHeader) {
                    initHeader('X-Requested-With', pub.defaultXhrHeader);
                }

                if(postData && pub.useDefaultHeader && (!pub.headers || !pub.headers[CONTENTTYPE])){
                    initHeader(CONTENTTYPE, pub.defaultPostHeader);
                }

                if (pub.defaultHeaders || pub.headers) {
                    setHeader(o);
                }

                handleReadyState(o, callback);
                o.conn.send(postData || null);
            }
            return o;
        }
*/

        function asyncRequest(method, uri, callback, postData) {
            var o = getConnectionObject() || null;

            if (!o) {
                return null;
            } else if (o) {
                // start
                try {
                    // origin
                    o.conn.open(method, uri, true);
                } catch(ex) {
                    handleTransactionResponse(o, callback);
                    return o;
                }
                // end

                if (pub.useDefaultXhrHeader) {
                    initHeader('X-Requested-With', pub.defaultXhrHeader);
                }

                if(postData && pub.useDefaultHeader && (!pub.headers || !pub.headers[CONTENTTYPE])){
                    initHeader(CONTENTTYPE, pub.defaultPostHeader);
                }

                if (pub.defaultHeaders || pub.headers) {
                    setHeader(o);
                }

                handleReadyState(o, callback);
                try {
                    o.conn.send(postData || null);
                } catch(ex) {
                    handleTransactionResponse(o, callback);
                }
            }
            return o;
        }

        // private
        function getConnectionObject() {
            var o;

            try {
                if (o = createXhrObject(pub.transactionId)) {
                    pub.transactionId++;
                }
            } catch(e) {
            } finally {
                return o;
            }
        }

/*
        // private
        function createXhrObject(transactionId) {
            var http;

            try {
                http = new XMLHttpRequest();
            } catch(e) {
                for (var i = 0; i < activeX.length; ++i) {
                    try {
                        http = new ActiveXObject(activeX[i]);
                        break;
                    } catch(e) {}
                }
            } finally {
                return {conn : http, tId : transactionId};
            }
        }
*/
        function createXhrObject(transactionId) {
            var obj, http;
            try {

                if (Ext.isIE7 && !!pub.forceActiveX) {
                    throw ("IE7forceActiveX");
                }

                http = new XMLHttpRequest();

                obj = {
                    conn: http,
                    tId: transactionId
                };
            } catch(e) {
                for (var i = 0; i < activeX.length; ++i) {
                    try {

                        http = new ActiveXObject(activeX[i]);

                        obj = {
                            conn: http,
                            tId: transactionId
                        };
                        break;
                    } catch(e) {}
                }
            } finally {
                return obj;
            }
        }

        var pub = {
            forceActiveX: true,
            request : function(method, uri, cb, data, options) {
                if(options){
                    var me = this,
                        xmlData = options.xmlData,
                        jsonData = options.jsonData,
                        hs;

                    Ext.applyIf(me, options);

                    if(xmlData || jsonData){
                        hs = me.headers;
                        if(!hs || !hs[CONTENTTYPE]){
                            initHeader(CONTENTTYPE, xmlData ? 'text/xml' : 'application/json');
                        }
                        data = xmlData || (Ext.isObject(jsonData) ? Ext.encode(jsonData) : jsonData);
                    }
                }
                return asyncRequest(method || options.method || "POST", uri, cb, data);
            },

            serializeForm : function(form) {
                var fElements = form.elements || (document.forms[form] || Ext.getDom(form)).elements,
                    hasSubmit = false,
                    encoder = encodeURIComponent,
                    element,
                    options,
                    name,
                    val,
                    data = '',
                    type;

                Ext.each(fElements, function(element) {
                    name = element.name;
                    type = element.type;

                    if (!element.disabled && name){
                        if(/select-(one|multiple)/i.test(type)){
                            Ext.each(element.options, function(opt) {
                                if (opt.selected) {
                                    data += String.format("{0}={1}&",
                                                         encoder(name),
                                                          (opt.hasAttribute ? opt.hasAttribute('value') : opt.getAttributeNode('value').specified) ? opt.value : opt.text);
                                }
                            });
                        } else if(!/file|undefined|reset|button/i.test(type)) {
                            if(!(/radio|checkbox/i.test(type) && !element.checked) && !(type == 'submit' && hasSubmit)){

                                data += encoder(name) + '=' + encoder(element.value) + '&';
                                hasSubmit = /submit/i.test(type);
                            }
                        }
                    }
                });
                return data.substr(0, data.length - 1);
            },

            useDefaultHeader : true,
            defaultPostHeader : 'application/x-www-form-urlencoded; charset=UTF-8',
            useDefaultXhrHeader : true,
            defaultXhrHeader : 'XMLHttpRequest',
            poll : {},
            timeout : {},
            pollInterval : 50,
            transactionId : 0,

//  This is never called - Is it worth exposing this?
//          setProgId : function(id) {
//              activeX.unshift(id);
//          },

//  This is never called - Is it worth exposing this?
//          setDefaultPostHeader : function(b) {
//              this.useDefaultHeader = b;
//          },

//  This is never called - Is it worth exposing this?
//          setDefaultXhrHeader : function(b) {
//              this.useDefaultXhrHeader = b;
//          },

//  This is never called - Is it worth exposing this?
//          setPollingInterval : function(i) {
//              if (typeof i == 'number' && isFinite(i)) {
//                  this.pollInterval = i;
//              }
//          },

//  This is never called - Is it worth exposing this?
//          resetDefaultHeaders : function() {
//              this.defaultHeaders = null;
//          },

            abort : function(o, callback, isTimeout) {
                var me = this,
                    tId = o.tId,
                    isAbort = false;

                if (me.isCallInProgress(o)) {
                    o.conn.abort();
                    clearInterval(me.poll[tId]);
                    me.poll[tId] = null;
                    if (isTimeout) {
                        me.timeout[tId] = null;
                    }

                    handleTransactionResponse(o, callback, (isAbort = true), isTimeout);
                }
                return isAbort;
            },

            isCallInProgress : function(o) {
                // if there is a connection and readyState is not 0 or 4
                return o.conn && !{0:true,4:true}[o.conn.readyState];
            }
        };
        return pub;
    }();
}





