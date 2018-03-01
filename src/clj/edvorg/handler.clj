(ns edvorg.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found resources]]
            [rocks.clj.configuron.core :refer [env config-handler]]
            [edvorg.middleware
             :refer
             [dev-middleware prod-middleware web-socket-middleware]]
            [rocks.clj.transit.core :as transit]
            edvorg.view.index
            edvorg.view.editor
            [immutant.web.async :as async]
            [org.httpkit.server :as httpkit]
            [reagent.core :as reagent]
            [ring.util.response :as response]
            [taoensso.timbre :as timbre]
            [edvorg.defaults  :as defaults]
            [hiccup.page :refer [include-js include-css html5]]
            [rocks.clj.configuron.core :refer [env get-client-config]]
            [rocks.clj.transit.core :as transit]
            [rocks.clj.reagent-handler.core :refer [render]]))

(defn head
  "Default head that is injected to any page that is rendered by server."
  []
  [:head
   [:meta {:charset "utf-8"}]
   (include-css (case (:mode env)
                  :dev "/css/site.css"
                  :uberjar "/css/site.min.css"))])

(defn wrap-page
  "Renders page component by wrapping it into template hiccup structure."
  [[view state :as page]]
  (html5
    (head)
    [:body {:class "body-container"}
     [:div#config {:transit (get-client-config)}]
     [:div#state {:transit (transit/to-transit @state)}]
     [:div#app
      (render page)]
     (include-js "/js/app.js")]))

(defn web-socket-handler [{:keys [cookies server-port visitor-id] :as req}]
  (let [on-close-fn (fn [channel {:keys [code reason]}])
        on-message-fn (fn [channel msg]
                        (let [msg (transit/from-transit msg)]
                          (timbre/debug ":visitor-id" visitor-id
                                        "processing message"
                                        ":msg" msg)))]
    (if (= (get-in env [:figwheel :server-port]) server-port)
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
           wrap-page
           (response/response)
           (response/content-type "text/html")))

  (GET "/editor" {:keys [cookies visitor-id]}
       (-> [edvorg.view.editor/view (atom defaults/state)]
           wrap-page
           (response/response)
           (response/content-type "text/html")))

  (GET "/ws" [] (web-socket-middleware #'web-socket-handler))

  (GET "/environ" [] #'config-handler)

  (resources "/")

  (not-found "Not Found"))

(def prod-routes (prod-middleware #'routes))

(def dev-routes (dev-middleware #'routes))
