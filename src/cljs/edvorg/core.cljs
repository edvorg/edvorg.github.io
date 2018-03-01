(ns edvorg.core
  (:require [reagent.core :as reagent]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [chord.client :as chord]
            [cljs.core.async :refer [<! >!]]
            [taoensso.timbre :as timbre]
            [edvorg.view.index]
            [edvorg.view.editor]
            [edvorg.state :as state]
            [rocks.clj.transit.core :as transit]
            [rocks.clj.configuron.core :refer [get-env]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; -------------------------
;; Views

(defn current-page []
  (session/get :current-page))

;; -------------------------
;; Routes

;; TODO refactor into isomorphic routes

(secretary/defroute "/" []
  (session/put! :current-page [#'edvorg.view.index/view state/state]))

(secretary/defroute "/editor" []
  (session/put! :current-page [#'edvorg.view.editor/view state/state]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn get-encoded-data [id]
  (-> (.getElementById js/document id)
      (.getAttribute "transit")
      (transit/from-transit)))

(defonce init? (atom false))

(defn init! []
  (go
    (let [env (<! (get-env))]
      ;; decode application state from html
      (->> (get-encoded-data "state")
           (reset! state/state))
      (case (:mode env)
        :uberjar (set! *print-fn* (fn [& _]))
        :dev (enable-console-print!))
      (accountant/configure-navigation! {:nav-handler secretary/dispatch!
                                         :path-exists? secretary/locate-route})
      (accountant/dispatch-current!)
      (mount-root))))

(when-not @init?
  (reset! init? true)
  (init!))
