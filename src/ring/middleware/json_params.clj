(ns ring.middleware.json-params
  (:require [clj-json.core :as json]))

(defn- json-request?
  [req]
  (if-let [#^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?json" type)))))

(defn wrap-json-params
  ([handler] (wrap-json-params handler nil))
  ([handler json-key]
     (fn [req]
       (if-let [body (and (json-request? req) (:body req))]
         (let [bstr (slurp body)
               json-params (try (json/parse-string bstr) (catch Exception e nil))
               json-params (if json-key {json-key json-params} json-params)
               req* (assoc req
                      :json-params json-params
                      :params (merge json-params (:params req)))]
           (handler req*))
         (handler req)))))