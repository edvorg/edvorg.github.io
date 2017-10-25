(ns edvorg.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found resources]]
            [edvorg.config :as config]
            [edvorg.middleware
             :refer
             [dev-middleware prod-middleware web-socket-middleware]]
            [edvorg.transit :as transit]
            edvorg.view.index
            edvorg.view.editor
            [immutant.web.async :as async]
            [org.httpkit.server :as httpkit]
            [reagent.core :as reagent]
            [ring.util.response :as response]
            [taoensso.timbre :as timbre]
            [edvorg.defaults  :as defaults]))

(defn web-socket-handler [{:keys [cookies server-port visitor-id] :as req}]
  (let [on-close-fn (fn [channel {:keys [code reason]}])
        on-message-fn (fn [channel msg]
                        (let [msg (transit/from-transit msg)]
                          (timbre/debug ":visitor-id" visitor-id
                                        "processing message"
                                        ":msg" msg)))]
    (if (= (get-in @config/env [:figwheel :server-port]) server-port)
      (httpkit/with-channel req channel {:format :str}
        ;; (clients/register visitor-id {:channel channel
        ;;                               :respond! #(httpkit/send! channel %)})
        (httpkit/on-close channel (partial on-close-fn nil))
        (httpkit/on-receive channel (partial on-message-fn nil)))
      (async/as-channel req
                        :on-open (fn [channel]
                                   ;; (clients/register visitor-id {:channel channel
                                   ;;                               :respond! #(async/send! channel %)})
                                   )
                        :on-close on-close-fn
                        :on-message on-message-fn))))

;; TODO refactor into isomorphic routes

(declare routes)

(defroutes routes
  (GET "/" {:keys [cookies visitor-id]}
       (-> [edvorg.view.index/view (atom defaults/state)]
           (reagent/wrap-page)
           (response/response)
           (response/content-type "text/html")))

  (GET "/editor" {:keys [cookies visitor-id]}
       (-> [edvorg.view.editor/view (atom defaults/state)]
           (reagent/wrap-page)
           (response/response)
           (response/content-type "text/html")))

  (GET "/ws" [] (web-socket-middleware #'web-socket-handler))

  (resources "/")

  (not-found "Not Found"))

(def prod-routes (prod-middleware #'routes))

(def dev-routes (dev-middleware #'routes))
