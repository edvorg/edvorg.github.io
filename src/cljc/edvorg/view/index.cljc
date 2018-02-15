(ns edvorg.view.index
  (:require [rocks.clj.configuron.core :refer [env]]))

(defn view
  "Index page."
  [state]
  ;; Cursors are used for reactive rendering.
  ;; When data changes only the views that are affected by change will be re-rendered.
  [:div.playground
   "hello"])
