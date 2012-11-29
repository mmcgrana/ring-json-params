(ns ring.middleware.json-params
  (:require [clojure.data.json :as json])
  (:use [clojure.string :only [blank?]]))

(defn- json-request?
  [req]
  (if-let [#^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?json" type)))))

(defn wrap-json-params [handler]
  (fn [req]
    (if-let [body (and (json-request? req) (:body req))]
      (let [body-str (slurp body)]
        (if-not (blank? body-str)
          (let [json-params (json/read-json body-str)
                req* (assoc req
                  :json-params json-params
                  :params (merge (:params req) json-params))]
            (handler req*))
          (handler req)))
      (handler req))))
