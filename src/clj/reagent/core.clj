(ns reagent.core
  (:require [rocks.clj.configuron.core :refer [env get-client-config]]
            [edvorg.transit :as transit]
            [hiccup.page :refer [include-js include-css html5]])
  (:refer-clojure :exclude [atom]))

;; these are utilities to support server side rendering with react/reagent

(defn atom
  "Reactive atom placeholder for server-side rendering"
  [value]
  (clojure.core/atom value))

(defn cursor
  "Reactive cursor placeholder for server-side rendering."
  [state path]
  (atom (get-in @state path)))

(defn normalize
  "Makes sure all components have opts map as second element."
  [component]
  (if (map? (second component))
    component
    (into [(first component) {}] (rest component))))

(defn render
  "Generates plain hiccup structure from reagent-style hiccup structure."
  ([root component] (render root [0] component))
  ([root id component]
   (cond
     (fn? component)
     (render root (component))

     (not (coll? component))
     component

     (coll? (first component))
     (map-indexed #(render false (conj id %1) %2) component)

     (keyword? (first component))
     (let [[tag opts & body] (normalize component)
           opts (cond-> opts
                  root (assoc :data-reactroot true))]
       (->> body
            (map-indexed #(render false (conj id %1) %2))
            (into [tag opts])))

     (fn? (first component))
     (render root id (apply (first component) (rest component))))))

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
     (render true page)]
    (include-js "/js/app.js")]))
