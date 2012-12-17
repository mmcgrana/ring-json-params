(ns ring.middleware.json-params
  (:require [clojure.data.json :as json]))

(defn- json-request?
  [req]
  (if-let [#^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?json" type)))))

;;json params middleware
(defn wrap-json-params [handler]
  (fn [req]
    (if-let [body (and (json-request? req) (:body req))]
      (let [bstr (slurp body)
            ]
        (if (not-empty bstr)
          (let [            json-params (json/read-str bstr)
                req* (assoc req
                       :json-params json-params
                       :params (merge (:params req) json-params))]
            (handler req*))
          (handler req)))
      (handler req))))
