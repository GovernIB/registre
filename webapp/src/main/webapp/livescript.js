/**
 * Copyright 2002 Bob Lee (http://www.crazybob.org/)
 * This product is licensed under the GPL 
 * (http://www.gnu.org/licenses/gpl.txt).
 */

/**
 * LiveScript Javascript library.
 * author Bob Lee (crazybob@crazybob.org)
 * version 1.0
 */

/** 
 * Invocation context.
 *
 * Parameters:
 *   path: Path to LiveScript Servlet.
 *
 * Methods:
 *   invoke(arg1, arg2, ...): Invoke LiveScript Servlet.
 *
 * Events:
 *   onresult: Fired when an invocation returns. Accepts one parameter,
 *     the Servlet's return value.
 *   onerror: Fired in the event an error ocurrs. Accepts one parameter,
 *     the error message.
 */
function InvocationContext(path) {
  this.ls_path = path;
}

// gets a cookie value.
function ls_getCookie(name) {
  // workaround to ensure that '+' is decoded to ' '.
  function ls_decode(value) {
    return decodeURIComponent(value.replace(/\+/, ' '));
  }

  var c = document.cookie;
  var start = c.indexOf(name + '=');
  if (start == -1) return null;
  start += name.length + 1;
  var end = c.indexOf(';', start);
  if (end == -1) end = c.length;
  return ls_decode(c.substring(start, end));
}

// invokes the LiveScript Servlet.
InvocationContext.prototype.invoke = function() {
  var id = ls_uniqueId();
  var img = new Image();

  // handles result from the server.
  img.onload = function() {
    var result = ls_getCookie(this.id);
    ls_deleteCookie(this.id);
    var context = this.invocationContext;
    // if an error ocurred, invoke the context error handler.
    if (result != null && result.startsWith('LIVE_SCRIPT_EXCEPTION=')) {
      if (context.onerror) 
        context.onerror(result.substring(22, result.length));
    }
    // otherwise, delegate to the context result handler.
    else if (context.onresult) context.onresult(result);
  };

  // handles server errors.
  img.onerror = function() {
    ls_deleteCookie(this.id);
    // delegate to the context error handler.
    if (this.invocationContext.onerror)
      this.invocationContext.onerror('A server error ocurred.');
  };

  img.invocationContext = this;
  img.id = id;
  var queryString = '?id=' + id;
  var args = this.invoke.arguments;
  for (i=0; i < args.length; i++)
    queryString += '&arg=' + escape(args[i]);
  img.src = this.ls_path + queryString;
}



// deletes a cookie.
var ls_expireDate;
function ls_deleteCookie(name) {
  if (!ls_expireDate) ls_expireDate = new Date(0).toGMTString()
  document.cookie = name + "=" + "; path=/; expires=" + ls_expireDate;
}

// generates unique ID.
var ls_id;
var ls_winId;
function ls_uniqueId() {
  if (!ls_id) ls_id = 1;
  if (!ls_winId) ls_winId = new Date().getTime();
  // format: LS_[counter]_[time of first invocation]
  return ('LS_' + (ls_id++) + '_' + ls_winId);
}
  
// adds startsWith() to String objects.
String.prototype.startsWith = function(s) {
  if (this.length < s.length) return false;
  if (s.length <= 0) return false;
  if (this.substring(0, s.length) != s) return false;
  return true;
}
