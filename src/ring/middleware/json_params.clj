(ns ring.middleware.json-params
  (:require [clj-json.core :as json]))

(defn- json-request?
  [req]
  (if-let [#^String type (:content-type req)]
    (.startsWith type "application/json")))

(defn wrap-json-params [handler]
  (fn [req]
    (if-let [body (and (json-request? req) (:body req))]
      (let [bstr (slurp body)
            json-params (json/parse-string bstr)
            req* (assoc req
                   :json-params json-params
                   :params (merge (:params req) json-params))]
        (handler req*))
      (handler req))))
