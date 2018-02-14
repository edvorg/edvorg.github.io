(ns edvorg.middleware
  (:require [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.permacookie :refer [wrap-permacookie]]
            [rocks.clj.configuron.core :refer [env]]))

(defn visitor-id [_]
  (if (= :dev (:mode env))
    "dev"
    (str (java.util.UUID/randomUUID))))

(defn wrap-user-token [handler]
  (wrap-permacookie handler {:name (:user-token-key env)
                             :idfn visitor-id}))

(defn web-socket-middleware [handler]
  (-> handler
      wrap-user-token))

(defn dev-middleware
  "Development middleware with exception handling and hot reloading."
  [handler]
  (-> handler
      wrap-user-token
      (wrap-defaults site-defaults)
      wrap-exceptions
      wrap-reload))

(defn prod-middleware
  "Production middleware."
  [handler]
  (-> handler
      wrap-user-token
      (wrap-defaults site-defaults)))
